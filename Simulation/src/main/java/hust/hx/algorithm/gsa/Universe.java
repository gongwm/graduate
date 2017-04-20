package hust.hx.algorithm.gsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * gsa算法：充满粒子的宇宙。<br/>
 * 宇宙：只有一种物质（粒子），三个属性（质量，力，运动）的小宇宙。<br/>
 * 自然法则：评价粒子好坏的标准（只和粒子当前的位置有关）。<br/>
 * 宇宙的属性：寿命（lifeSpan）、初始引力常数（initialGravityConstant）、老化系数（agingRatio）
 * 和creatureCount个粒子。<br/>
 * 参考：引力搜索算法的相关文献。<br/>
 * 参考：A Gravitational Search Algorithm, Information sciences, vol. 179, no. 13,
 * pp. 2232-2248, 2009.
 * 
 * @version 2016-7-22 10:13:57 引入了多线程，以并行计算适应度。
 * @author hx
 */
public class Universe {
	private NaturalLaw law;// 输入参数
	private List<Range> spaces;

	private int lifeSpan = 1000;// 控制参数
	private double initialGravityConstant = 100.0;
	private double agingRatio = 20.0;
	private int creatureCount = 10;

	private int age;// 全局变量
	private final int dimension;
	private List<Particle> particles;

	private double gravityConstant;// 缓存
	private double bestFitness;
	private double worstFitness;
	private double totalMass;

	private Mode mode = Mode.SMALL_BETTER;

	enum Mode {
		SMALL_BETTER, BIG_BETTER
	}

	private int kbest = creatureCount;
	private int finalPercent = 2;

	private ExecutorService executor = null;
	public static final int RECOMMENDED_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

	/**
	 * 构造方法。
	 * 
	 * @param law
	 *            自然法则。传入计算适应度的函数。
	 * @param spaces
	 *            空间限制。Range的个数决定空间的维数，范围决定某维数字的取值范围
	 */
	public Universe(NaturalLaw law, List<Range> spaces) {
		this.law = law;
		this.spaces = spaces;
		this.dimension = spaces.size();
		this.age = 0;
		init();
	}

	public Universe(NaturalLaw law, Range... spaces) {
		this(law, Arrays.asList(spaces));
	}

	/**
	 * 算法配置方法。在计算前调用可以进行配置。不配置则采取默认策略。
	 * 
	 * @param lifeSpan
	 *            寿命，决定了迭代次数
	 * @param initialGravityConstant
	 *            初始引力常数，决定了步长
	 * @param agingRatio
	 *            老化系数，决定了步长变小的速率
	 * @param creatureCount
	 *            搜索粒子数，决定搜索的规模
	 */
	public void configure(int lifeSpan, double initialGravityConstant, double agingRatio, int creatureCount) {
		this.lifeSpan = lifeSpan;
		this.initialGravityConstant = initialGravityConstant;
		this.agingRatio = agingRatio;
		this.creatureCount = creatureCount;
		this.kbest = creatureCount;
		init();
	}

	public void configure(int lifeSpan, int creatureCount) {
		this.lifeSpan = lifeSpan;
		this.creatureCount = creatureCount;
		this.kbest = creatureCount;
		init();
	}

	private void init() {
		particles = new ArrayList<Particle>();
		for (int i = 0; i < creatureCount; ++i) {
			particles.add(new Particle());
		}
	}

	public void configueThreadsCount(int threadsCount) {
		if (threadsCount < 2) {
			threadsCount = 0;
		}
		executor = null;
		if (threadsCount != 0) {
			executor = Executors.newFixedThreadPool(threadsCount);
		}
	}

	public void rockAndRoll() {
		while (age < lifeSpan) {
			evolveOnce();
			++age;
		}
		if (executor != null) {
			executor.shutdown();
		}
	}

	private void evolveOnce() {
		calculateFitness();
		agine();
		choose();

		System.out.println("age: " + age + " cordinate: " + bestOne() + " fitness: " + bestFitness);

		calculateKBest();
		sortParticlesIndescendingOrder();
		move();
	}

	private void calculateFitness() {
		if (executor != null) {
			List<Future<Particle>> result = new ArrayList<>();
			for (final Particle p : particles) {
				result.add(executor.submit(new Callable<Particle>() {
					@Override
					public Particle call() throws Exception {
						p.judgeFitness();
						return p;
					}
				}));
			}
			for (Future<Particle> f : result) {
				try {
					f.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		} else {
			for (Particle p : particles) {
				p.judgeFitness();
			}
		}
	}

	private void sortParticlesIndescendingOrder() {
		Collections.sort(particles);
		Collections.reverse(particles);
	}

	private void agine() {// 宇宙老化一次
		this.gravityConstant = initialGravityConstant * Math.exp(-agingRatio * age / lifeSpan);
	}

	private void choose() {// 求最好适应度和最差适应度
		this.bestFitness = Collections.max(particles).fitness;
		this.worstFitness = Collections.min(particles).fitness;
	}

	private void move() {
		calculateMass();
		totalMass();
		for (Particle p : particles) {
			p.currentInertiaMass();
			p.acceleration();
		}
		for (Particle p : particles) {
			p.move();
		}
	}

	private void calculateMass() {
		for (Particle p : particles) {
			p.currentMass();
		}
	}

	private void totalMass() {// 总质量
		totalMass = 0;
		for (Particle particle : particles) {
			totalMass += particle.mass;
		}
	}

	private void calculateKBest() {
		double percent = finalPercent + (1 - 1.0 * age / lifeSpan) * (100 - finalPercent);
		kbest = (int) Math.round(creatureCount * percent / 100);
	}

	public RealVector bestOne() {
		return Collections.max(particles).cordinate;
	}

	public double bestFitness() {
		return bestFitness;
	}

	/**
	 * 自然法则。规定如何判断当前坐标的适应度。适应度决定了质量、惯性质量，从而决定了粒子对其他粒子引力的大小。
	 * 引力大的粒子会吸引其他粒子以某种随机的路径向自己靠拢。
	 * 
	 * @author hx
	 * 
	 */
	@FunctionalInterface
	public interface NaturalLaw {
		double judgeFitness(double[] cordinate);
	}

	/**
	 * 宇宙中唯一的东西：粒子。<br/>
	 * 粒子的属性：坐标、速度、加速度、质量、惯性质量、适应度
	 * 
	 * @author hx
	 * 
	 */
	private class Particle implements Comparable<Particle> {
		private RealVector cordinate;

		private RealVector velocity;
		private double fitness;
		private static final double A_SMALL_DOUBLE = 2.2204e-16;

		private double mass;
		private double inertiaMass;
		private RealVector acceleration;

		Particle() {
			this.cordinate = generateRandomLocation(spaces);
			this.velocity = new ArrayRealVector(dimension);
		}

		private void currentMass() {// 粒子当前质量
			this.mass = (fitness - worstFitness) / (bestFitness - worstFitness);
		}

		private void currentInertiaMass() {// 当前惯性质量
			double im = mass / totalMass;
			this.inertiaMass = im == 0 ? A_SMALL_DOUBLE : im;
		}

		private void judgeFitness() {// 粒子当前适应度
			this.fitness = law.judgeFitness(cordinate.toArray());
		}

		private void acceleration() {// 求加速度
			this.acceleration = totalForce().mapDivide(inertiaMass);
		}

		private void move() {// 在其他粒子的合力作用下移向下个坐标
			RealVector rv = cordinate.add(nextVelocity());
			for (int i = 0; i < dimension; ++i) {
				rv.setEntry(i, spaces.get(i).checkRange(rv.getEntry(i)));
			}
			cordinate = rv;
		}

		private RealVector nextVelocity() {// 引力作用下的速度
			RealVector randVector = generateRandomVector(dimension);
			velocity = velocity.ebeMultiply(randVector).add(acceleration);
			return velocity;
		}

		private RealVector totalForce() {// 合力
			RealVector vector = new ArrayRealVector(dimension);
			for (int i = 0; i < kbest; ++i) {
				Particle particle = particles.get(i);
				if (particle != this) {
					vector = vector.add(this.forceFrom(particle).ebeMultiply(generateRandomVector(dimension)));
				}
			}
			return vector;
		}

		private RealVector forceFrom(Particle particle) {// 分力
			return particle.cordinate.subtract(cordinate).mapMultiply(gravityConstant * inertiaMass
					* particle.inertiaMass / (cordinate.getDistance(particle.cordinate) + A_SMALL_DOUBLE));
		}

		@Override
		public int compareTo(Particle o) {// 以适应度作为比较标准
			int result = Double.compare(fitness, o.fitness);
			if (mode == Mode.BIG_BETTER) {
				return result;
			} else {
				return -1 * result;
			}
		}
	}

	public static class Range {
		public final double low;
		public final double high;

		private static Random rand = new Random();

		public Range(double low, double high) {
			if (low > high) {
				throw new IllegalArgumentException("low must be less than or equal high");
			}
			this.low = low;
			this.high = high;
		}

		public static Range of(double low, double high) {
			return new Range(low, high);
		}

		public double checkRange(double currentValue) {
			if (currentValue <= low) {
				return low;
			}
			if (currentValue >= high) {
				return high;
			}
			if (Double.isNaN(currentValue) || Double.isInfinite(currentValue)) {
				return generate();
			}
			return currentValue;
		}

		public double generate() {
			return rand.nextDouble() * (high - low) + low;
		}
	}

	private RealVector generateRandomLocation(List<Range> ranges) {
		RealVector vector = new ArrayRealVector(ranges.size());
		for (int i = 0; i < ranges.size(); ++i) {
			vector.setEntry(i, ranges.get(i).generate());
		}
		return vector;
	}

	private RealVector generateRandomVector(int dimension) {
		Random rand = new Random();
		RealVector randVector = new ArrayRealVector(dimension);
		for (int i = 0; i < dimension; ++i) {
			randVector.setEntry(i, rand.nextDouble());
		}
		return randVector;
	}

	public static void main(String[] args) {// 计算根号3。
		Universe u = new Universe(new NaturalLaw() {
			@Override
			public double judgeFitness(double[] cordinate) {
				double d = cordinate[0];
				return Math.abs((d * d - 3));
			}
		}, new Range(1, 2));
		u.rockAndRoll();

		System.out.println(u.bestOne());
		System.out.println("fitness: " + u.bestFitness());
		System.out.println("real: " + Math.sqrt(3));
	}
}

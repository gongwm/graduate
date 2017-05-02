package hust.hx.algorithm.gsa;

import java.util.List;

/**
 * gsa算法：充满粒子的宇宙。<br/>
 * 
 * @version 2016-7-22 10:13:57 引入了多线程，以并行计算适应度。
 * @author hx
 */
public class WeighedGSA extends ClassicGSA {
	private double cmax = 0.9;
	private double cmin = 0.6;

	private double maxInertiaMass;
	private double minInertiaMass;

	public WeighedGSA(NaturalLaw law, List<Range> spaces) {
		super(law, spaces);
	}

	@Override
	protected void init() {
		for (int i = 0; i < creatureCount; ++i) {
			particles.add(new ModifiedParticle());
		}
	}

	@Override
	protected void calculateMass() {
		super.calculateMass();
		weighedInertiaMass();
	}

	private void weighedInertiaMass() {
		this.maxInertiaMass = particles.stream().mapToDouble(p -> p.inertiaMass).max().getAsDouble();
		this.minInertiaMass = particles.stream().mapToDouble(p -> p.inertiaMass).min().getAsDouble();
		particles.stream().forEach(p -> ((ModifiedParticle) p).weighedInertiaMass());
	}

	class ModifiedParticle extends ClassicGSA.Particle {
		void weighedInertiaMass() {
			double alpha = ((cmax - cmin) * super.inertiaMass + (cmin * minInertiaMass - cmax * maxInertiaMass))
					/ (minInertiaMass - maxInertiaMass);
			super.inertiaMass = Math.pow(super.inertiaMass, alpha);
		}
	}
}

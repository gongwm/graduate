package hust.hx.simulation.demo.block;

/**
 * fixed time step config.
 * 
 * @author hx
 *
 */
public class Config {
	static final Config DEFAULT_CONFIG = new Config();

	double T = 0.01;
	double t = 0.0;
	double tt = 10;

	private int n = (int) (tt / T) + 1;

	double[] time = new double[n];

	public static Config of(double step, double totalTime) {
		Config c = new Config();
		c.config(step, 0.0, totalTime);
		return c;
	}

	void config(double T, double t, double tt) {
		this.T = T;
		this.t = t;
		this.tt = tt;
		n = (int) (tt / T) + 1;
		time = new double[n];
	}

	void reset() {
		this.t = 0.0;
	}

	void iterate(Once once) {
		t += T; // start from 1*T
		for (int i = 1; i < n; ++i) {
			time[i] = t;
			once.step();
			t += T;
		}
	}

	public double[] getTime() {
		return time;
	}

	@Override
	public String toString() {
		return String.format("step: %fs, total time: %fs, total step: %d", T, tt, n);
	}

	public static void main(String[] args) {
		Config c = Config.of(0.1, 30);
		System.out.println(c);
	}
}

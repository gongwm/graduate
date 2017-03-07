package zte.hx.simulation.demo.block;

/**
 * fixed time step config.
 * 
 * @author hx
 *
 */
public class Config {
	static final Config DEFAULT_CONFIG = new Config();

	double T = 0.001;
	double t = 0.0;
	double tt = 10;

	private int n = (int) (tt / T) + 1;

	double[] time = new double[n];

	void config(double T, double t, double tt) {
		this.T = T;
		this.t = t;
		this.tt = tt;
		n = (int) (tt / T) + 1;
		time = new double[n];
	}

	void iterate(Once once) {
		for (int i = 1; i < n; ++i) {
			t += T;
			once.step(i, t);
			time[i] = t;
		}
	}

	public double[] getTime() {
		return time;
	}

	@Override
	public String toString() {
		return String.format("step: %fs, total time: %fs, total step: %d", T,
				tt, n);
	}
}

package zte.hx.simulation.test.block;

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

	void config(double T, double t, double tt) {
		this.t = T;
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
}
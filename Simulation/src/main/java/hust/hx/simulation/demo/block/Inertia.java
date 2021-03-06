package hust.hx.simulation.demo.block;

import static java.lang.Math.pow;

/**
 * 
 * k/(1+t*s)
 * 
 * @author hx
 *
 */
public class Inertia extends BaseBlock implements LinearBlock {
	private Config config = Config.DEFAULT_CONFIG;

	double k = 1.0;
	double t = 0.1;

	private double c1, c2;

	Inertia(double k, double t) {
		this.k = k;
		this.t = t;
		initRatio();
	}

	private void initRatio() {
		c1 = pow(e, -config.T / t);
		c2 = k * (1 - pow(e, -config.T / t));
	}

	public void next(double input) {
		next = c1 * current + c2 * input;
	}

	void setConfig(Config config) {
		this.config = config;
		initRatio();
	}

	void config(double k, double t) {
		this.k = k;
		this.t = t;
		initRatio();
	}

	@Override
	public String toString() {
		return String.format("block inertia. k: %f, t: %f", k, t);
	}
}

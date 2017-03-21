package hust.hx.simulation.demo.block;

import static java.lang.Math.pow;

public class Inertia extends BaseBlock implements LinearBlock {
	private Config config = Config.DEFAULT_CONFIG;

	double k = 1.0;
	double t = 0.1;

	private double c1;
	private double c2;

	Inertia() {
		initRatio();
	}

	LinearBlock config(double... ds) {
		k = ds[0];
		t = ds[1];
		initRatio();
		return this;
	}

	private void initRatio() {
		c1 = pow(e, -config.T / t);
		c2 = k * (1 - pow(e, -config.T / t));
	}

	public void next(double input) {
		newOutput = c1 * lastOutput + c2 * input;
	}

	void setConfig(Config config) {
		this.config = config;
		initRatio();
	}

	@Override
	public String toString() {
		return String.format("block inertia. k: %f, t: %f", k, t);
	}
}

package zte.hx.simulation.test.block;

import static java.lang.Math.pow;

public class Inertia implements LinearBlock {
	Config config;

	double k = 1.0;
	double t = 0.1;

	private double output = 0.0;

	private double c1;
	private double c2;

	Inertia(Config config) {
		this.config = config;
		c1 = pow(e, -config.T / t);
		c2 = k * (1 - pow(e, -config.T / t));
	}

	Block config(double... ds) {
		k = ds[0];
		t = ds[1];
		return this;
	}

	public void next(double input) {
		output = c1 * output + c2 * input;
	}

	public double getOutput() {
		return output;
	}
}

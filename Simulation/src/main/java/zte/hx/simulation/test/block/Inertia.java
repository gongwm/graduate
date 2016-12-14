package zte.hx.simulation.test.block;

import static java.lang.Math.pow;

public class Inertia implements ControlBlock {
	Config config;

	double k = 1.0;
	double t = 0.1;

	private double lastOut = 0.0;

	private double c1;
	private double c2;

	Inertia(Config config) {
		this.config = config;
		c1 = pow(e, -config.T / t);
		c2 = k * (1 - pow(e, -config.T / t));
	}

	public void next(double input) {
		double out = c1 * lastOut + c2 * input;
		lastOut = out;
	}

	public double getLastOut() {
		return lastOut;
	}
}

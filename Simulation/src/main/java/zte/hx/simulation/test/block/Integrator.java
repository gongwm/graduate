package zte.hx.simulation.test.block;

public class Integrator implements ControlBlock {
	private Config config;
	private double k = 2;

	private double output = 0.0;

	private double c;

	Integrator(Config config) {
		this.config = config;
		c = k * this.config.T;
	}

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		output = output + c * input;
	}
}

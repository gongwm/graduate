package zte.hx.simulation.demo.block;

public class Integrator implements ControlBlock {
	Config config = Config.DEFAULT_CONFIG;
	private double k = 2;

	private double output = 0.0;

	private double c;

	Integrator() {
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

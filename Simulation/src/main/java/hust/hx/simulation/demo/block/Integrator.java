package hust.hx.simulation.demo.block;

public class Integrator extends BaseBlock implements ControlBlock {
	Config config = Config.DEFAULT_CONFIG;
	private double k = 2;

	private double c;

	Integrator() {
		c = k * this.config.T;
	}

	@Override
	public double getLastOutput() {
		return lastOutput;
	}

	@Override
	public void next(double input) {
		newOutput = lastOutput + c * input;
	}

	@Override
	public void moveOn() {
		lastOutput = newOutput;
	}
}

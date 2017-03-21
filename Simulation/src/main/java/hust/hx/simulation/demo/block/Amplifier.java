package hust.hx.simulation.demo.block;

public class Amplifier implements LinearBlock {
	double k;

	private double lastOutput, newOutput;

	Amplifier(double k) {
		this.k = k;
	}

	@Override
	public double getLastOutput() {
		return lastOutput;
	}

	@Override
	public void next(double input) {
		newOutput = k * input;
	}

	@Override
	public void moveOn() {
		lastOutput = newOutput;
	}
}

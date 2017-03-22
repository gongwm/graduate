package hust.hx.simulation.demo.block;

abstract class BaseBlock implements Block {

	protected double lastOutput = 0.0, newOutput;

	public BaseBlock() {
	}

	@Override
	public double getLastOutput() {
		return lastOutput;
	}

	@Override
	public double getCurrentOutput() {
		return newOutput;
	}

	@Override
	public void moveOn() {
		lastOutput = newOutput;
	}

	@Override
	public void setInitValue(double input) {
		lastOutput = 0.0;
	}
}

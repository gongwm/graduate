package hust.hx.simulation.demo.block;

abstract class BaseBlock implements Block {

	protected double lastOutput, newOutput;

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

}

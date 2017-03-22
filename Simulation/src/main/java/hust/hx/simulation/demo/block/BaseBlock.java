package hust.hx.simulation.demo.block;

abstract class BaseBlock implements Block {

	protected double current = 0.0, next;

	public BaseBlock() {
	}

	@Override
	public double getCurrent() {
		return current;
	}

	@Override
	public double getNext() {
		return next;
	}

	@Override
	public void moveOn() {
		current = next;
	}

	@Override
	public void setInitValue(double input) {
		current = 0.0;
	}
}

package hust.hx.simulation.demo.block;

public class Amplifier extends BaseBlock implements LinearBlock {
	double k;

	Amplifier(double k) {
		this.k = k;
	}

	@Override
	public void next(double input) {
		next = k * input;
	}

	@Override
	public void setInitValue(double initValue) {
		next(initValue);
		moveOn();
	}
}

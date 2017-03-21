package hust.hx.simulation.demo.block;

public class Insensitor extends BaseBlock implements NonlinearBlock {
	private double c;

	Insensitor(double c) {
		this.c = c;
	}

	@Override
	public void next(double input) {
		if (input > c) {
			newOutput = input - c;
		} else if (input < -c) {
			newOutput = input + c;
		} else {
			newOutput = 0;
		}
	}
}

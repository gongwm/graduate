package zte.hx.simulation.test.block;

public class Insensitor implements NonlinearBlock {
	private double c;

	double output;

	Insensitor(double c) {
		this.c = c;
	}

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		if (input > c) {
			output = input - c;
		} else if (input < -c) {
			output = input + c;
		} else {
			output = 0;
		}
	}

}

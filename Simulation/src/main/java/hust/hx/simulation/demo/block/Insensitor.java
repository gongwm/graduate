package hust.hx.simulation.demo.block;

public class Insensitor implements NonlinearBlock {
	private double c;

	private double lastOutput;
	private double newOutput;

	Insensitor(double c) {
		this.c = c;
	}

	@Override
	public double getLastOutput() {
		return lastOutput;
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

	@Override
	public void moveOn() {
		lastOutput = newOutput;
	}

}

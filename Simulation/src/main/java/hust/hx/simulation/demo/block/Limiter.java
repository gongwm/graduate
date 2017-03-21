package hust.hx.simulation.demo.block;

public class Limiter implements NonlinearBlock {
	private double upperBound, lowerBound;

	private double lastOutput, newOutput;

	Limiter(double upperBound, double lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}

	Limiter(double bound) {
		this.upperBound = bound;
		this.lowerBound = -bound;
	}

	@Override
	public double getLastOutput() {
		return lastOutput;
	}

	@Override
	public void next(double input) {
		if (input > upperBound) {
			newOutput = upperBound;
		} else if (input < lowerBound) {
			newOutput = lowerBound;
		} else {
			newOutput = input;
		}
	}

	@Override
	public void moveOn() {
		lastOutput = newOutput;
	}

}

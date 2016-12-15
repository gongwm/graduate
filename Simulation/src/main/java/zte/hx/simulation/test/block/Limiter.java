package zte.hx.simulation.test.block;

public class Limiter implements NonlinearBlock {
	private double upperBound;
	private double lowerBound;

	double output;

	Limiter(double upperBound, double lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}

	Limiter(double bound) {
		this.upperBound = bound;
		this.lowerBound = -bound;
	}

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		if (input > upperBound) {
			output = upperBound;
		} else if (input < lowerBound) {
			output = lowerBound;
		} else {
			output = input;
		}
	}

}

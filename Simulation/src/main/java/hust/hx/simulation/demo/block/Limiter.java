package hust.hx.simulation.demo.block;

public class Limiter extends BaseBlock implements NonlinearBlock {
	private double upperBound, lowerBound;

	Limiter(double upperBound, double lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}

	Limiter(double bound) {
		this.upperBound = bound;
		this.lowerBound = -bound;
	}

	@Override
	public void next(double input) {
		if (input > upperBound) {
			next = upperBound;
		} else if (input < lowerBound) {
			next = lowerBound;
		} else {
			next = input;
		}
	}
}

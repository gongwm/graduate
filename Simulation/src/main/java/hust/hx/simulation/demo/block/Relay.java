package hust.hx.simulation.demo.block;

public class Relay implements NonlinearBlock {
	private double y0;

	double output;

	Relay(double y0) {
		this.y0 = y0;
	}

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		if (input > 0.0) {
			output = y0;
		} else if (input < 0.0) {
			output = -y0;
		} else {
			output = 0.0;
		}
	}

}

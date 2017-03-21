package hust.hx.simulation.demo.block;

public class Relay extends BaseBlock implements NonlinearBlock {
	private double y0;

	Relay(double y0) {
		this.y0 = y0;
	}

	@Override
	public void next(double input) {
		if (input > 0.0) {
			newOutput = y0;
		} else if (input < 0.0) {
			newOutput = -y0;
		} else {
			newOutput = 0.0;
		}
	}
}

package hust.hx.simulation.demo.block;

public class Integrator extends BaseBlock implements ControlBlock {
	Config config = Config.DEFAULT_CONFIG;
	private double k = 2;

	private double c;

	Integrator(double k) {
		this.k = k;
		initRatio();
	}

	private void initRatio() {
		c = k * config.T;
	}

	@Override
	public void next(double input) {
		next = current + c * input;
	}
}

package hust.hx.simulation.demo.block;

public class Integrator extends BaseBlock implements ControlBlock {
	Config config = Config.DEFAULT_CONFIG;
	double k = 2;

	double c;

	Integrator(double k) {
		this.k = k;
		initRatio();
	}

	private void initRatio() {
		c = k * config.T;
	}

	void config(double k) {
		this.k = k;
		initRatio();
	}
	
	void setConfig(Config config){
		this.config=config;
		initRatio();
	}

	@Override
	public void next(double input) {
		next = current + c * input;
	}
}

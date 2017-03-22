package hust.hx.simulation.demo.block;

public class StepSource extends BaseBlock implements Source {
	public double getCurrent() {
		return 1.0;
	}

	@Override
	public void next() {
		next = getCurrent();
	}
}

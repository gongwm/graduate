package hust.hx.simulation.demo.block;

public class StepSource extends BaseBlock implements Source {
	public double getLastOutput() {
		return 1.0;
	}

	@Override
	public void next() {
	}
}

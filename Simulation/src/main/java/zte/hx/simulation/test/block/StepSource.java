package zte.hx.simulation.test.block;

public class StepSource implements Source {
	public double getOutput() {
		return 1.0;
	}

	@Override
	public void next(int k, double T) {
	}
}

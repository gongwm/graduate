package zte.hx.simulation.test.block;

public class Amplifier implements LinearBlock {
	double k;

	private double output;
	
	Amplifier(double k) {
		this.k = k;
	}

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		output = k * input;
	}
}

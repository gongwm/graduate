package hust.hx.simulation.demo.block;

public interface Block {

	double e = Math.E;

	void setInitValue(double input);

	double getLastOutput();

	double getCurrentOutput();

	void moveOn();
}

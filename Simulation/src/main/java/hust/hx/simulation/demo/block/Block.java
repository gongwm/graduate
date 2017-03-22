package hust.hx.simulation.demo.block;

public interface Block {

	double e = Math.E;

	void setInitValue(double input);

	double getCurrent();

	double getNext();

	void moveOn();
}

package hust.hx.simulation.demo.block;

public interface Block {

	double e = Math.E;

	double getLastOutput();
	
	double getCurrentOutput();

	void moveOn();
}

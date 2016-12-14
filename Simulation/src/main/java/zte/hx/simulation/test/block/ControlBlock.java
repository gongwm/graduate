package zte.hx.simulation.test.block;

public interface ControlBlock extends Block {
	void next(double input);
	
	double getLastOut();
}

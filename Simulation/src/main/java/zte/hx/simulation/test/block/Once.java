package zte.hx.simulation.test.block;

@FunctionalInterface
public interface Once {
	void step(int k, double t);
}

package hust.hx.simulation.demo.block;

@FunctionalInterface
public interface Once {
	/**
	 * @param k
	 *            current index
	 * @param t
	 *            current time in sec
	 */
	void step(int k, double t);
}

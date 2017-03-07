package zte.hx.simulation.demo.block;

import zte.hx.util.TestUtil;

public class SimpleTest {
	public static void main(String[] args) {
		Config config = Config.DEFAULT_CONFIG;

		Block b1 = new StepSource();
		Block b2 = new Inertia();
		Block b3 = new Scope();

		Line l1 = new Line(b1, b2);
		Line l2 = new Line(b2, b3);

		config.iterate((i, k) -> {
			l1.push(i, k);
			l2.push(i, k);
		});

		TestUtil.print(b3);
	}

}

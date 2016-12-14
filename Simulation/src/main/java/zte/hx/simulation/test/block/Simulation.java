package zte.hx.simulation.test.block;

import java.util.ArrayList;
import java.util.List;

import zte.hx.util.TestUtil;

public class Simulation {
	public static void main(String[] args) {
		Config config = new Config();

		ControlBlock inertia = new Inertia(config);
		Source step = new StepSource();

		Line line = new Line(step, inertia);
		List<Double> out = new ArrayList<>();

		TestUtil.timeIt(() -> {
			config.iterate((i, k) -> {
				line.push(0.0);
				out.add(inertia.getLastOut());
			});
		});
		TestUtil.printRange(out, 10);
	}
}

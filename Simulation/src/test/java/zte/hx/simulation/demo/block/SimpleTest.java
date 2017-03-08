package zte.hx.simulation.demo.block;

import zte.hx.simulation.util.PrintUtil;
import zte.hx.util.LangUtil;
import zte.hx.util.TestUtil;

public class SimpleTest {
	public static void main(String[] args) {
		Config config = Config.DEFAULT_CONFIG;

		Block b1 = new StepSource();
		Block b2 = new Inertia().config(1, 1);
		Block b3 = new Scope();

		Line l1 = new Line(b1, b2);
		Line l2 = new Line(b2, b3);

		config.iterate((i, k) -> {
			l1.push(i, k);
			l2.push(i, k);
		});

		TestUtil.print(b3);

		Scope s = (Scope) b3;
		double[] time = config.getTime();
		TestUtil.print(time.length);
		TestUtil.print(s.getData().size());

		PrintUtil.print(pw -> {
			LangUtil.zipList(LangUtil.toList(time), s.getData()).forEach(p -> {
				pw.println(String.format("%f %f", p.first, p.second));
			});
		});
	}
}

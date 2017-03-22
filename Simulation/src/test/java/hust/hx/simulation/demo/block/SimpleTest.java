package hust.hx.simulation.demo.block;

import hust.hx.simulation.demo.block.Block;
import hust.hx.simulation.demo.block.Config;
import hust.hx.simulation.demo.block.Inertia;
import hust.hx.simulation.demo.block.Line;
import hust.hx.simulation.demo.block.Scope;
import hust.hx.simulation.demo.block.StepSource;
import hust.hx.simulation.util.PrintUtil;
import hust.hx.util.LangUtil;
import hust.hx.util.TestUtil;

public class SimpleTest {
	public static void main(String[] args) {
		Config config = Config.DEFAULT_CONFIG;

		Block b1 = new StepSource();
		Block b2 = new Inertia(1, 0.1);
		Block b3 = new Scope();

		Line l1 = new Line(b1, b2);
		Line l2 = new Line(b2, b3);

		config.iterate(() -> {
			l1.push();
			l2.push();

			b1.moveOn();
			b2.moveOn();
			b3.moveOn();
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

package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hust.hx.simulation.util.PrintUtil;
import hust.hx.util.TestUtil;

public class ExciterTest {
	public static void main(String[] args) {
		Config config = Config.DEFAULT_CONFIG;

		Source stepSource = new StepSource();
		Adder j1 = new Adder();
		Block b1 = new Inertia(40, 0.1);
		Block b2 = new Limiter(30);
		Adder j2 = new Adder();
		Block b3 = new Inertia(-20, -10);
		Block b4 = new Amplifier(0.01);
		Block b5 = new Inertia(1, 1);
		Block b6 = new Inertia(0.05, 0.05);

		Line l1 = new Line(stepSource, j1);
		Line l2 = new Line(j1, b1);
		Line l3 = new Line(b1, b2);
		Line l4 = new Line(b2, j2);
		Line l5 = new Line(j2, b3);
		Line l6 = new Line(b3, b4);
		Line l7 = new Line(b4, j2);
		Line l8 = new Line(b3, b5);
		Line l9 = new Line(b5, b6);
		Line l10 = new Line(b6, j1);

		j1.addLine(l1, Adder.ADD);
		j1.addLine(l10, Adder.SUB);

		j2.addLine(l4, Adder.ADD);
		j2.addLine(l7, Adder.SUB);

		List<Double> out = new ArrayList<>();
		out.add(b5.getCurrent());

		List<Line> lines = Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10);
		List<Block> blocks = Arrays.asList(stepSource, j1, b1, b2, j2, b3, b4, b5, b6);

		TestUtil.timeIt(() -> {
			config.iterate(() -> {
				lines.forEach(l -> l.push());
				blocks.forEach(b -> b.moveOn());
				out.add(b5.getNext());
			});
		});
		TestUtil.printRange(out, 10);

		PrintUtil.print(pw -> {
			for (int i = 0; i < out.size(); ++i) {
				pw.println(String.format("%f %f", config.time[i], out.get(i)));
			}
		});
	}
}

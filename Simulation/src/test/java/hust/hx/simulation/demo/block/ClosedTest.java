package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

import hust.hx.simulation.demo.block.Config;
import hust.hx.simulation.demo.block.ControlBlock;
import hust.hx.simulation.demo.block.Inertia;
import hust.hx.simulation.demo.block.Integrator;
import hust.hx.simulation.demo.block.Joint;
import hust.hx.simulation.demo.block.Line;
import hust.hx.simulation.demo.block.Source;
import hust.hx.simulation.demo.block.StepSource;
import hust.hx.simulation.util.PrintUtil;
import hust.hx.util.TestUtil;

/**
 * <pre>
 * step->joint->inertia->integrator--->
 *         ^                        |
 *         |________________________|
 *  </pre>       
 * @author hx
 *
 */
public class ClosedTest {
	public static void main(String[] args) {
		Config config = Config.DEFAULT_CONFIG;

		Source step = new StepSource();
		Joint joint = new Joint();
		ControlBlock inertia = new Inertia();
		ControlBlock integrator = new Integrator();

		Line line1 = new Line(step, joint);
		Line line2 = new Line(joint, inertia);
		Line line3 = new Line(inertia, integrator);
		Line line4 = new Line(integrator, joint);

		joint.addLine(line1, Joint.ADD);
		joint.addLine(line4, Joint.SUB);

		List<Double> out = new ArrayList<>();
		TestUtil.timeIt(() -> {
			config.iterate((i, k) -> {
				line1.push(i, k);
				line2.push(i, k);
				line3.push(i, k);
				line4.push(i, k);
				out.add(integrator.getOutput());
			});
		});
		TestUtil.print(out.size());
		TestUtil.print(config.time.length);
		TestUtil.printRange(out, 60);

		PrintUtil.print(pw -> {
			for (int i = 0; i < out.size(); ++i) {
				pw.println(String.format("%f %f", config.time[i], out.get(i)));
			}
		});
	}
}

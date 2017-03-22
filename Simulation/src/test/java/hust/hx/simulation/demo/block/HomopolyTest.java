package hust.hx.simulation.demo.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import hust.hx.simulation.util.PrintUtil;
import hust.hx.util.LangUtil;

public class HomopolyTest {
	private Block step, hp, sc;
	private Line l1, l2;

	@Before
	public void setUp() {
		step = new StepSource();
		hp = new Homopoly(1.0, 0.01, 0.1);
		sc = new Scope();
		l1 = new Line(step, hp);
		l2 = new Line(hp, sc);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalC() {
		new Homopoly(1.0, 1.0, 0.0);
	}

	@Test
	public void testSimulate() {
		Config c = Config.DEFAULT_CONFIG;

		c.iterate(() -> {
			l1.push();
			l2.push();

			step.moveOn();
			hp.moveOn();
			sc.moveOn();
		});

		Scope s = (Scope) sc;
		assertEquals(1001, s.getData().size());
		assertNotEquals(0.0, hp.getLastOutput());

		double[] time = c.getTime();
		double[] data = LangUtil.toPrimitiveDoubleArray(s.getData());
		PrintUtil.print(pw -> {
			LangUtil.zipDoubleArray(time, data).forEach(p -> {
				pw.println(String.format("%f %f", p.first, p.second));
			});
		});
	}
}

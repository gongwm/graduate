package hust.hx.simulation.demo.block;

import java.math.BigDecimal;

import hust.hx.util.TestUtil;

public class Homopoly implements LinearBlock {
	private ControlBlock amp1, amp2, inertia;
	private Adder adder;
	private Line l1, l2, l3, l4;

	public Homopoly(double a, double b, double c) {
		if (c == 0) {
			throw new IllegalArgumentException("Homopoly: c != 0.0");
		}

		double x = calculateX(a, b, c), y = a - x;

		amp1 = new Amplifier(a);
		amp2 = new Amplifier(x);
		inertia = new Inertia().config(y, c);
		adder = new Adder();

		l1 = new Line(amp1, amp2);
		l2 = new Line(amp1, inertia);
		l3 = new Line(amp2, adder);
		l4 = new Line(inertia, adder);

		adder.addLine(l3, Adder.ADD);
		adder.addLine(l4, Adder.ADD);
	}

	private double calculateX(double a, double b, double c) {
		BigDecimal aa = BigDecimal.valueOf(a);
		BigDecimal bb = BigDecimal.valueOf(b);
		BigDecimal cc = BigDecimal.valueOf(c);
		return aa.pow(2).multiply(bb).divide(cc).doubleValue();
	}

	@Override
	public void next(double input) {
		amp1.next(input);
		l1.push();
		l2.push();
		l3.push();
		l4.push();

		TestUtil.print(inertia.getCurrentOutput());
	}

	@Override
	public double getLastOutput() {
		return adder.getLastOutput();
	}

	@Override
	public void moveOn() {
		amp1.moveOn();
		amp2.moveOn();
		inertia.moveOn();
		adder.moveOn();
	}

	@Override
	public double getCurrentOutput() {
		return adder.getCurrentOutput();
	}

	@Override
	public void setInitValue(double initValue) {
		amp1.setInitValue(initValue);
		amp2.setInitValue(amp1.getLastOutput());
		inertia.setInitValue(amp1.getLastOutput());
		adder.setInitValue(initValue);
		moveOn();
	}
}

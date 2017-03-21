package hust.hx.simulation.demo.block;

public class Homopoly implements LinearBlock {
	private ControlBlock amp1, amp2, inertia;
	private Adder adder;
	private Line l1, l2, l3, l4;

	public Homopoly() {
	}

	public Homopoly config(double... data) {
		assert data.length == 3;
		double a = data[0], b = data[1], c = data[2];
		assert c != 0.0;

		double x = b / c, y = 1 - x;

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

		return this;
	}

	@Override
	public void next(double input) {
		amp1.next(input);
		l1.push();
		l2.push();
		l3.push();
		l4.push();
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

}

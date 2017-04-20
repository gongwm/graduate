package hust.hx.simulation.demo.block;

public class Homopoly1 implements LinearBlock {
	private ControlBlock amp1, amp2, inertia;
	private Adder adder;
	private Line l1, l2, l3, l4;

	public Homopoly1(double a, double b) {
		init(a, b);
	}

	private void init(double a, double b) {
		if (b == 0) {
			//throw new IllegalArgumentException("Homopoly1: b!=0");
			b=0.0000001;
		}

		amp1 = new Amplifier(a / b);
		amp2 = new Amplifier(1.0);
		inertia = new Inertia(1.0, b);
		adder = new Adder();

		l1 = Line.of(amp1, amp2);
		l2 = Line.of(amp1, inertia);
		l3 = Line.of(amp2, adder);
		l4 = Line.of(inertia, adder);

		adder.addLine(l3, Adder.ADD);
		adder.addLine(l4, Adder.SUB);
	}

	void config(double a, double b) {
		init(a, b);
	}

	void setConfig(Config config) {
		((Inertia) this.inertia).setConfig(config);
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
	public void setInitValue(double input) {
		amp1.setInitValue(input);
		amp2.setInitValue(amp1.getCurrent());
		inertia.setInitValue(0.0);
		adder.setInitValue(0.0);
		moveOn();
	}

	@Override
	public double getCurrent() {
		return adder.getCurrent();
	}

	@Override
	public double getNext() {
		return adder.getNext();
	}

	@Override
	public void moveOn() {
		amp1.moveOn();
		amp2.moveOn();
		inertia.moveOn();
		adder.moveOn();
	}

}

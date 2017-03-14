package hust.hx.simulation.demo.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
		if (end instanceof Scope) {
			((Scope) end).next(start.getOutput());
		}
	}

	void push(int k, double T) {
		if (start instanceof Source) {
			((Source) start).next(k, T);
		}
		if (end instanceof ControlBlock) {
			((ControlBlock) end).next(start.getOutput());
		}
	}

	boolean isPreLine(Line line) {
		return this.end == line.start;
	}

	@Override
	public String toString() {
		return String.format("connect line. from: %s, to: %s", start, end);
	}
}

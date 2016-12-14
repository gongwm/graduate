package zte.hx.simulation.test.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
	}

	void push(int i, double k) {
		if (start instanceof Source) {
			((Source) start).next(i, k);
		}
		if (end instanceof ControlBlock) {
			((ControlBlock) end).next(start.getOutput());
		}
	}

	boolean isPreLine(Line line) {
		return this.end == line.start;
	}
}

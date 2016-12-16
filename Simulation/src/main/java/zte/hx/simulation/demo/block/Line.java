package zte.hx.simulation.demo.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
	}

	void push(int k, double T) {
		if (start instanceof Source) {
			((Source) start).next(k, T);
		} else if (end instanceof ControlBlock) {
			((ControlBlock) end).next(start.getOutput());
		}
	}

	boolean isPreLine(Line line) {
		return this.end == line.start;
	}
}

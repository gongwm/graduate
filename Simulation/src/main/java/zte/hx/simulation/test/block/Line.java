package zte.hx.simulation.test.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
	}

	void push(double input) {
		double startOut;
		if (start instanceof Source) {
			startOut = ((Source) start).next();
		} else if (start instanceof ControlBlock) {
			ControlBlock cb = (ControlBlock) start;
			cb.next(input);
			startOut = cb.getLastOut();
		} else {
			throw new IllegalStateException("check left block");
		}
		ControlBlock right = (ControlBlock) end;
		right.next(startOut);
	}

	boolean isPreLine(Line line) {
		return this.end == line.start;
	}
}

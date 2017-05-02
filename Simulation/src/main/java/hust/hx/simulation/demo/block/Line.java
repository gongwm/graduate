package hust.hx.simulation.demo.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
		init();
	}

	void init() {
		end.setInitValue(start.getCurrent());
	}

	public static Line of(Block start, Block end) {
		return new Line(start, end);
	}

	void push() {
		if (start instanceof Source) {
			((Source) start).next();
		}
		if (end instanceof ControlBlock) {
			ControlBlock cb = (ControlBlock) end;
			cb.next(start.getNext());
		}
		if (end instanceof Joint) {
			((Joint) end).next();
		}
	}

	@Override
	public String toString() {
		return String.format("connect line. from: %s, to: %s", start, end);
	}
}

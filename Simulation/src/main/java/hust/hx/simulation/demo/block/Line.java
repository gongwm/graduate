package hust.hx.simulation.demo.block;

public class Line {
	final Block start;
	final Block end;

	public Line(Block start, Block end) {
		super();
		this.start = start;
		this.end = end;
		end.setInitValue(start.getLastOutput());
		if (end instanceof Scope) {
			Scope s = (Scope) end;
			s.next(start.getLastOutput());
			s.moveOn();
		}
	}

	void push() {
		if (start instanceof Source) {
			((Source) start).next();
		}
		if (end instanceof ControlBlock) {
			if (end instanceof Scope) {
				((Scope) end).next(start.getCurrentOutput());
			} else {
				((ControlBlock) end).next(start.getLastOutput());
			}
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

package zte.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

public class Joint implements Block {
	private List<Line> lines = new ArrayList<>();
	private List<Character> formats = new ArrayList<>();

	static final char ADD = '+';
	static final char SUB = '-';

	public Joint() {
		super();
	}

	public void addLine(Line line, char operator) {
		checkValidation(line, operator);
		lines.add(line);
		formats.add(operator);
	}

	@Override
	public double getOutput() {
		double result = 0.0;
		for (int i = 0; i < lines.size(); ++i) {
			double output = lines.get(i).start.getOutput();
			switch (formats.get(i)) {
			case ADD:
				result += output;
				break;
			case SUB:
				result -= output;
			}
		}
		return result;
	}

	private void checkValidation(Line line, char operator) {
		if (line.end != this || (operator != ADD && operator != SUB)) {
			throw new IllegalArgumentException("wrong line or format");
		}
	}
}

package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

public class Adder extends BaseBlock implements Joint {
	static final char ADD = '+';
	static final char SUB = '-';

	private List<Line> lines = new ArrayList<>();
	private List<Character> formats = new ArrayList<>();

	public Adder() {
		super();
	}

	public void addLine(Line line, char operator) {
		checkValidation(line, operator);
		lines.add(line);
		formats.add(operator);
	}

	private void checkValidation(Line line, char operator) {
		if (line.end != this || (operator != ADD && operator != SUB)) {
			throw new IllegalArgumentException("wrong line or format");
		}
		if (lines.contains(line)) {
			throw new IllegalArgumentException("line already exists");
		}
	}

	@Override
	public void next() {
		double result = 0.0;
		for (int i = 0; i < lines.size(); ++i) {
			double output = lines.get(i).start.getNext();
			switch (formats.get(i)) {
			case ADD:
				result += output;
				break;
			case SUB:
				result -= output;
				break;
			}
		}
		next = result;
	}

	@Override
	public void setInitValue(double initValue) {
		next();
		moveOn();
	}
}

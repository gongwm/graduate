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
	}

	@Override
	public void next() {
		for (int i = 0; i < lines.size(); ++i) {
			double output = lines.get(i).start.getLastOutput();
			switch (formats.get(i)) {
			case ADD:
				newOutput += output;
				break;
			case SUB:
				newOutput -= output;
			}
		}
	}
}

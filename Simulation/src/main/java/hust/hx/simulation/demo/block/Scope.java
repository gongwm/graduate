package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

import hust.hx.util.LangUtil;

public class Scope implements ControlBlock {
	private List<Double> data = new ArrayList<>();

	private double lastOutput, newOutput;

	@Override
	public double getLastOutput() {
		return lastOutput;
	}

	@Override
	public void next(double input) {
		newOutput = input;
	}

	public List<Double> getData() {
		return data;
	}

	@Override
	public String toString() {
		return LangUtil.concatWithComma(LangUtil.toPrimitiveDoubleArray(data));
	}

	@Override
	public void moveOn() {
		lastOutput = newOutput;
		data.add(newOutput);
	}
}

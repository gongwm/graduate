package zte.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

import zte.hx.util.LangUtil;

public class Scope implements ControlBlock {
	private List<Double> data = new ArrayList<>();

	private double output;

	@Override
	public double getOutput() {
		return output;
	}

	@Override
	public void next(double input) {
		output = input;
		data.add(input);
	}

	public List<Double> getData() {
		return data;
	}

	@Override
	public String toString() {
		return LangUtil.concatWithComma(LangUtil.toPrimitiveDoubleArray(data));
	}
}

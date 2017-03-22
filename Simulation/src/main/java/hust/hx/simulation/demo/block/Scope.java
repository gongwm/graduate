package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.List;

import hust.hx.util.LangUtil;

public class Scope extends BaseBlock implements ControlBlock {
	private List<Double> data = new ArrayList<>();

	@Override
	public void next(double input) {
		next = input;
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

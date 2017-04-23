package hust.hx.simulation.demo.block;

import java.util.List;

import hust.hx.util.TestUtil;

public class Paper2Test {

	static double fitness(List<Double> origin, List<Double> output) {
		double res = 0.0;
		for (int i = 0; i < output.size(); ++i) {
			res += Math.pow(output.get(i) - origin.get(i), 2);
		}
		return res;
	}

	public static void main(String[] args) {
		RegularSys rs = new RegularSys(0.8, 3.36, 0.2, 0.05);
		TestUtil.timeIt(() -> rs.simulate());
	}

}

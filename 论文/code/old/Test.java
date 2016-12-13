package bll.simulation.danji;

import bll.Condition;
import bll.simulation.SimulationInput;
import util.TestUtil;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Condition condition = Condition.PUMP_OUTAGE;
		SimulationInput input = new SimulationInput("danji", condition, "");
		OneUnitTransient rejection1 = new OneUnitTransient(input);
		// rejection1.loopiter(rejection1);
		// double[] a = rejection1.getProcessCurve().getDraftPressure()[0];
		// int j = 0;
		// for (int i = 0; i < a.length; ++i) {
		// System.out.print(a[i] + " ");
		// ++j;
		// if (j > 100) {
		// System.out.println("");
		// j = 0;
		// }
		// }

	}

}

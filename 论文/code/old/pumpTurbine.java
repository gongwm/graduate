package bll.simulation.danji;

public class pumpTurbine {
	private static double cpr2;
	private static double cnr3;

	public static void updateTurbineQH(Pipeline L2, Pipeline L3, double HeadTemp) {

		cpr2 = L2.QCurrent.getEntry(L2.note - 1) + L2.Ca * L2.HCurrent.getEntry(L2.note - 1)
				- L2.Cf * L2.QCurrent.getEntry(L2.note - 1) * Math.abs(L2.QCurrent.getEntry(L2.note - 1));
		cnr3 = L3.QCurrent.getEntry(1) - L3.Ca * L3.HCurrent.getEntry(1)
				- L3.Cf * L3.QCurrent.getEntry(1) * Math.abs(L3.QCurrent.getEntry(1));

		updatePipe2(L2, L3, HeadTemp);
		updatePipe3(L2, L3, HeadTemp);
	}

	public static void updatePipe2(Pipeline L2, Pipeline L3, double HeadTemp) {
		L2.QNext.setEntry(L2.note, (cpr2 / L2.Ca + cnr3 / L3.Ca - HeadTemp) / (1 / L2.Ca + 1 / L3.Ca));
		L2.HNext.setEntry(L2.note, (cpr2 - L2.QNext.getEntry(L2.note)) / L2.Ca);
	}

	public static void updatePipe3(Pipeline L2, Pipeline L3, double HeadTemp) {
		L3.QNext.setEntry(0, L2.QNext.getEntry(L2.note));
		L3.HNext.setEntry(0, (L3.QNext.getEntry(0) - cnr3) / L3.Ca);
	}

	
	
	
	public static void updateTurbineQH(Pipeline L2, Pipeline L3) {
		cpr2 = L2.QCurrent.getEntry(L2.note - 1) + L2.Ca * L2.HCurrent.getEntry(L2.note - 1)
				- L2.Cf * L2.QCurrent.getEntry(L2.note - 1) * Math.abs(L2.QCurrent.getEntry(L2.note - 1));
		cnr3 = L3.QCurrent.getEntry(1) - L3.Ca * L3.HCurrent.getEntry(1)
				- L3.Cf * L3.QCurrent.getEntry(1) * Math.abs(L3.QCurrent.getEntry(1));
		updatePipe2(L2, L3);
		updatePipe3(L2, L3);
	}

	
	public static void updatePipe2(Pipeline L2, Pipeline L3) {
		L2.QNext.setEntry(L2.note, 0);
		L2.HNext.setEntry(L2.note, (cpr2 - L2.QNext.getEntry(L2.note)) / L2.Ca);
	}

	
	public static void updatePipe3(Pipeline L2, Pipeline L3) {
		L3.QNext.setEntry(0, L2.QNext.getEntry(L2.note));
		L3.HNext.setEntry(0, (L3.QNext.getEntry(0) - cnr3) / L3.Ca);
	}

}

package bll.simulation.danji;

public class Surge {
	private double sectionArea1 = 12.57;
	private double sectionArea2 = 63.62;
	private double sectionArea3 = 63.62;
	private double inCoff = 0.001083;
	private double outCoff = 0.001475;
	private double[] Qs1;
	private double[] Hs1;
	private double Cp;
	private double Cm;
	private double Bp;
	private double Bm;
	private double Fs;
	private double timeStep;
	private Pipeline L1;
	private Pipeline L2;
	private int surgeFlag = 0;

	public Surge(int surgeFlag, OneUnitTransient loadRejection) {
		// TODO Auto-generated constructor stub
		Qs1 = new double[loadRejection.getIterNumber()];
		Hs1 = new double[loadRejection.getIterNumber()];
		this.L1 = loadRejection.getL1();
		this.L2 = loadRejection.getL2();
		Hs1[0] = L1.HCurrent.getEntry(L1.note);
		Qs1[0] = 0;
		this.timeStep = loadRejection.getTimeStep();
		
		this.surgeFlag = surgeFlag;
	}

	public Surge(OneUnitTransient loadRejection) {
		// TODO Auto-generated constructor stub
		sectionArea1 = 15.90;
		sectionArea2 = 95.03;
		sectionArea3 = 519.98;
		inCoff = 0.0009217;
		outCoff = 0.0006767;
		Qs1 = new double[loadRejection.getIterNumber()];
		Hs1 = new double[loadRejection.getIterNumber()];
		this.L1 = loadRejection.getL3();
		this.L2 = loadRejection.getL4();
		Hs1[0] = L1.HCurrent.getEntry(L1.note);
		Qs1[0] = 0;
		this.timeStep = loadRejection.getTimeStep();

	}

	public void sugerModel(int iter) {
		calcuPara();
		if (surgeFlag > 0) {
			calcuSectionArea(iter, surgeFlag);
		} else {
			calcuSectionArea(iter);
		}
		surgeQ(iter);
		surgePineQH(iter);

	}

	public void calcuPara() {
		double B1 = L1.Cspeed / (9.81 * L1.F);
		double R1 = L1.lossCoefficient * L1.Length / (L1.note) / (2 * 9.81 * L1.d * L1.F * L1.F);
		double B2 = L2.Cspeed / (9.81 * L2.F);
		double R2 = L2.lossCoefficient * L2.Length / (L2.note) / (2 * 9.81 * L2.d * L2.F * L2.F);
		Cp = L1.HCurrent.getEntry(L1.note-1) + B1 * L1.QCurrent.getEntry(L1.note - 1);
		Cm = L2.HCurrent.getEntry(1) - B2 * L2.QCurrent.getEntry(1);
		Bp = B1 + R1 * Math.abs(L1.QCurrent.getEntry(L1.note-1));
		Bm = B2 + R2 * Math.abs(L2.QCurrent.getEntry(1));

	}

	public void calcuSectionArea(int iter, int upSuger) {
		if (Hs1[iter - 1] < 688) {
			Fs = sectionArea1;
		} else {
			Fs = sectionArea2;
		}
	}

	public void calcuSectionArea(int iter) {
		if (Hs1[iter - 1] < 130) {
			Fs = sectionArea1;
		} else if (Hs1[iter - 1] < 189) {
			Fs = sectionArea2;
		} else {
			Fs = sectionArea3;
		}

	}

	public void surgeQ(int iter) {
		double Qst = Qs1[iter - 1];
		Qs1[iter] = Qs1[iter - 1] >= 0
				? (Cp / Bp + Cm / Bm - (1 / Bp + 1 / Bm) * (Hs1[iter - 1] + timeStep / (2 * Fs) * Qs1[iter - 1]))
						/ ((1 / Bp + 1 / Bm) * outCoff * Math.abs(Qst) + (1 / Bp + 1 / Bm) * timeStep / (2 * Fs) + 1)
				: (Cp / Bp + Cm / Bm - (1 / Bp + 1 / Bm) * (Hs1[iter - 1] + timeStep / (2 * Fs) * Qs1[iter - 1]))
						/ ((1 / Bp + 1 / Bm) * inCoff * Math.abs(Qst) + (1 / Bp + 1 / Bm) * timeStep / (2 * Fs) + 1);

		while (Math.abs(Qs1[iter] - Qst) >= 1e-5) {
			Qst = 0.5 * (Qs1[iter] + Qst);
			Qs1[iter] = Qst >= 0
					? (Cp / Bp + Cm / Bm - (1 / Bp + 1 / Bm) * (Hs1[iter - 1] + timeStep / (2 * Fs) * Qs1[iter - 1]))
							/ ((1 / Bp + 1 / Bm) * outCoff * Math.abs(Qst) + (1 / Bp + 1 / Bm) * timeStep / (2 * Fs)
									+ 1)
					: (Cp / Bp + Cm / Bm - (1 / Bp + 1 / Bm) * (Hs1[iter - 1] + timeStep / (2 * Fs) * Qs1[iter - 1]))
							/ ((1 / Bp + 1 / Bm) * inCoff * Math.abs(Qst) + (1 / Bp + 1 / Bm) * timeStep / (2 * Fs)
									+ 1);
		}
	}

	public void surgePineQH(int iter) {

		L1.HNext.setEntry(L1.note, (Cp / Bp + Cm / Bm - Qs1[iter]) / (1 / Bp + 1 / Bm));
		L2.HNext.setEntry(0, L1.HNext.getEntry(L1.note));
		L1.QNext.setEntry(L1.note, (Cp - L1.HNext.getEntry(L1.note)) / Bp);
		L2.QNext.setEntry(0, (L2.HNext.getEntry(0) - Cm) / Bm);
		Qs1[iter] = L1.QNext.getEntry(L1.note) - L2.QNext.getEntry(0);
		Hs1[iter] = Hs1[iter - 1] + timeStep / (2 * Fs) * (Qs1[iter] + Qs1[iter - 1]);
	}

	public double[] getHs1() {
		return Hs1;
	}

}

package bll.simulation.danji;

import bll.Condition;
import bll.Interpolator;
import bll.simulation.ExtremeValue;
import bll.simulation.SimulationInput;
import bll.simulation.TransientSimulation;
import bll.simulation.bean.ProcessCurve;
import bll.simulation.bean.SurgeSpecialValue;
import bll.simulation.bean.UnitSpecialValue;
import dao.TextTurbineCurveDAO;

public class OneUnitTransient implements TransientSimulation {

	private double Hr = 540;
	private double Qr = 62.09;
	protected double Q0;

	private double timeStep = 0.08;
	private double stableTime = 0;
	private double totalTime = 100;
	private int iter = 1;
	private final double pi = 3.1415926;
	private int iterNumber = (int) Math.round(totalTime / timeStep);
	private double[] time = new double[iterNumber];
	private double[] uintSpeed = new double[iterNumber];
	private double[] torque = new double[iterNumber];

	private double headwork;
	private double qTurbine;
	private double[] guideVane = new double[iterNumber];
	private double y0;
	private double y;

	private double[] volutePressure = new double[iterNumber];// 蜗壳压力
	private double[] draftPressure = new double[iterNumber];// 尾水压力

	private double ks1 = 10;// ImproveSuter
	private double ks2 = 0.9;
	private double cy = 0.2;
	private double ch = 0.5;

	private double Ta = 8.503;// Generator
	private double en = 0;

	private Pipeline L = new Pipeline();// pipeline
	private Pipeline1 L1 = new Pipeline1();
	private Pipeline2 L2 = new Pipeline2();
	private Pipeline3 L3 = new Pipeline3();
	private Pipeline4 L4 = new Pipeline4();
	PIDAndActuator pidAndActuator = new PIDAndActuator();
	private double[][] recordResults;

	private Condition condition;
	private Surge upSurge;
	private Surge downSurge;
	private final static Interpolator interpolatorWH;
	private final static Interpolator interpolatorWM;

	static {
		TextTurbineCurveDAO dao = new TextTurbineCurveDAO();
		double[][] WH = dao.wh();
		double[][] WM = dao.wm();
		double[] X = dao.x();
		double[] Y = dao.y();
		interpolatorWH = Interpolator.interpolate(X, Y, WH);
		interpolatorWM = Interpolator.interpolate(X, Y, WM);
	}

	public OneUnitTransient(SimulationInput input) {
		condition = input.getCondition();
		if (condition == Condition.SINGLE_REJECTION) {
			uintSpeed[0] = 1.0;
			torque[0] = 1.01;
			y0 = 1;
			qTurbine = 1.011;
			Q0 = qTurbine * Qr;
			recordResults = new double[iterNumber][7];
			headwork = 1.015136;
		}
		if (condition == Condition.TURBINE_START) {
			uintSpeed[0] = 0;
			torque[0] = 0;
			y0 = 0;
			qTurbine = 0;
			Q0 = qTurbine * Qr;
			pidAndActuator.setPID(2, 4, 0.1, 1);
			recordResults = new double[iterNumber][7];
			headwork = 1.02675925925926;
		}
		if (condition == Condition.PUMP_START) {
			uintSpeed[0] = -1;
			torque[0] = 0.1239;
			y0 = 0;
			qTurbine = 0;
			Q0 = qTurbine * Qr;

			stableTime = 150;
			recordResults = new double[iterNumber][7];
			headwork = 1.026172435049002;
		}
		if (condition == Condition.PUMP_OUTAGE) {
			uintSpeed[0] = -1;
			torque[0] = 0.9321;
			y0 = 0.92;
			qTurbine = -0.7662;
			Q0 = qTurbine * Qr;
			recordResults = new double[iterNumber][7];
			headwork = 1.033834;
		}
		loopiter(this);
	}

	private void loopiter(OneUnitTransient rejection1) {
		guideVane[0] = y0 * 0.7873;
		L.iniPineQH(Q0, L1, L2, L3, L4);
		recordResults[iter - 1][0] = uintSpeed[iter - 1];
		recordResults[iter - 1][1] = qTurbine;
		recordResults[iter - 1][2] = torque[iter - 1];
		recordResults[iter - 1][3] = headwork;
		recordResults[iter - 1][4] = y0 * 0.7873;
		recordResults[iter - 1][5] = L2.HCurrent.getEntry(L2.note);
		recordResults[iter - 1][6] = L3.HCurrent.getEntry(0) - 93;

		volutePressure[iter - 1] = L2.HCurrent.getEntry(L2.note);
		draftPressure[iter - 1] = L3.HCurrent.getEntry(0) - 93;

		upSurge = new Surge(1, rejection1);
		downSurge = new Surge(rejection1);

		while (iter < iterNumber) {
			time[iter - 1] = (iter) * timeStep;
			System.out.println("迭代次数" + " " + iter);
			if (condition == Condition.SINGLE_REJECTION) {
				y = GuideVaneLaw.rejection(time[iter - 1], stableTime, y0);
			}
			if (condition == Condition.TURBINE_START) {
				y = GuideVaneLaw.turbineStart(pidAndActuator, time[iter - 1],
						uintSpeed[iter - 1], y0);
			}
			if (condition == Condition.PUMP_START) {
				y = GuideVaneLaw.pumpTurbineStart(pidAndActuator,
						time[iter - 1]);
			}
			if (condition == Condition.PUMP_OUTAGE) {
				y = GuideVaneLaw.pumpDuanDian(time[iter - 1], stableTime, y0);
			}
			if (y >= 0) {
				vaneIsNotClosed();
			} else {
				vaneIsClosed();
			}

			update(upSurge, downSurge);

			recordResults[iter][0] = uintSpeed[iter];
			recordResults[iter][1] = qTurbine;
			recordResults[iter][2] = torque[iter];
			recordResults[iter][3] = headwork;
			recordResults[iter][4] = y;
			recordResults[iter][5] = L2.HCurrent.getEntry(L2.note);
			recordResults[iter][6] = L3.HCurrent.getEntry(0) - 93;
			guideVane[iter] = y;

			volutePressure[iter] = L2.HCurrent.getEntry(L2.note);
			draftPressure[iter] = L3.HCurrent.getEntry(0) - 93;
			iter++;

		}
		OutTxt.WriteTxt(recordResults);
		time[iterNumber - 1] = totalTime;
	}

	@Override
	public ProcessCurve getProcessCurve() {
		return new ProcessCurve(time, new double[][] { uintSpeed, uintSpeed },
				new double[][] { guideVane, guideVane }, new double[][] {
						volutePressure, volutePressure }, new double[][] {
						draftPressure, draftPressure }, upSurge.getHs1(),
				downSurge.getHs1());
	}

	@Override
	public UnitSpecialValue getSpecialValue1() {
		return new UnitSpecialValue(ExtremeValue.min(volutePressure),
				ExtremeValue.max(volutePressure), ExtremeValue.max(uintSpeed),
				ExtremeValue.min(draftPressure),
				ExtremeValue.max(draftPressure));
	}

	@Override
	public SurgeSpecialValue getUpSurgeSpecialValue() {
		double[] d = upSurge.getHs1();
		return new SurgeSpecialValue(d[0], ExtremeValue.min(d),
				ExtremeValue.max(d));
	}

	@Override
	public SurgeSpecialValue getDownSurgeSpecialValue() {
		double[] d = downSurge.getHs1();
		return new SurgeSpecialValue(d[0], ExtremeValue.min(d),
				ExtremeValue.max(d));
	}

	public void update(Surge upSurge, Surge downSurge) {
		L1.updatePipeline();
		upSurge.sugerModel(iter);
		L2.updatePipeline();
		L3.updatePipeline();
		downSurge.sugerModel(iter);
		L4.updatePipeline();

		L1.NextToCurrent();
		L2.NextToCurrent();
		L3.NextToCurrent();
		L4.NextToCurrent();
	}

	public void vaneIsNotClosed() {

		headwork = (L2.HCurrent.getEntry(L2.note) - L3.HCurrent.getEntry(0))
				/ Hr;
		qTurbine = L2.QCurrent.getEntry(L2.note) / Qr;
		for (int flagSpeed = 1; flagSpeed < 10;) {
			QTurbineCycle();
			flagSpeed = speedTurbineCycle(flagSpeed);
		}
	}

	public void vaneIsClosed() {
		qTurbine = L2.QCurrent.getEntry(L2.note) / Qr;
		pumpTurbine.updateTurbineQH(L2, L3);
		headwork = (L2.HNext.getEntry(L2.note) - L3.HNext.getEntry(0)) / Hr;
		for (int flagSpeed = 1; flagSpeed < 10;) {
			flagSpeed = speedTurbineCycle(flagSpeed);
		}
	}

	public void QTurbineCycle() {

		for (int k = 0; k < 10;) {
			pumpTurbine.updateTurbineQH(L2, L3, getHeadTemp(improveSuter()));
			double qTurbineTemp = L2.QNext.getEntry(L2.note) / Qr;
			if (Math.abs(qTurbineTemp - qTurbine) > 8e-4) {
				k++;
				qTurbine = qTurbine + 0.5 * (qTurbineTemp - qTurbine);
			} else {
				break;
			}

		}
	}

	public int speedTurbineCycle(int flagSpeed) {
		headwork = (L2.HNext.getEntry(L2.note) - L3.HNext.getEntry(0)) / Hr;
		updateSpeed(improveSuter());
		if (Math.abs(uintSpeed[iter] - uintSpeed[iter - 1]) >= 2e-2) {
			flagSpeed++;
			uintSpeed[iter - 1] = uintSpeed[iter - 1] + 0.5
					* (uintSpeed[iter] - uintSpeed[iter - 1]);
		} else {
			flagSpeed = 10;
		}
		return flagSpeed;
	}

	public double improveSuter() {
		double xqal;
		if (uintSpeed[iter - 1] > 0) {
			xqal = Math.atan((qTurbine + ks2 * Math.pow(headwork, 0.5))
					/ uintSpeed[iter - 1]);
		} else if (uintSpeed[iter - 1] < 0) {
			xqal = pi
					+ Math.atan((qTurbine + ks2 * Math.pow(headwork, 0.5))
							/ uintSpeed[iter - 1]);

		} else {
			xqal = pi / 2;
		}

		return xqal;
	}

	public double getHeadTemp(double xqal) {
		double wh;

		wh = interpolatorWH.value(xqal, y);
		return wh
				* (Math.pow(qTurbine, 2) + Math.pow(uintSpeed[iter - 1], 2) + ch
						* headwork) / Math.pow(y + cy, 2) * Hr;

	}

	public void updateSpeed(double xqal) {
		double wm;
		double mg;
		wm = interpolatorWM.value(xqal, y);
		torque[iter] = wm
				* (Math.pow(qTurbine, 2) + Math.pow(uintSpeed[iter - 1], 2) + ch
						* headwork) / Math.pow(y + cy, 2) - ks1 * headwork;
		if (time[iter - 1] <= stableTime) {
			mg = (torque[iter] + torque[iter - 1]) / 2;
		} else {
			mg = 0;
		}
		uintSpeed[iter] = (2 * Ta / timeStep - en) * uintSpeed[iter - 1]
				/ (2 * Ta / timeStep + en)
				+ (torque[iter] + torque[iter - 1] - 2 * mg)
				/ (2 * Ta / timeStep + en);

	}

	public double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(double timeStep) {
		this.timeStep = timeStep;
	}

	public int getIter() {
		return iter;
	}

	public int getIterNumber() {
		return iterNumber;
	}

	public void setIterNumber(int iterNumber) {
		this.iterNumber = iterNumber;
	}

	public Pipeline1 getL1() {
		return L1;
	}

	public Pipeline2 getL2() {
		return L2;
	}

	public Pipeline3 getL3() {
		return L3;
	}

	public Pipeline4 getL4() {
		return L4;
	}

	public double[] getTime() {
		return time;
	}

	public void setTime(double[] time) {
		this.time = time;
	}

	@Override
	public UnitSpecialValue getSpecialValue2() {
		return getSpecialValue1();
	}

}

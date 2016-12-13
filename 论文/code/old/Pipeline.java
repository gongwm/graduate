package bll.simulation.danji;



import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Pipeline {
	protected double Hu = 735.45;
	protected double Hd = 181;
	protected double Qr=62.09;
	protected double Length = 444.23;
	protected double Cspeed = 1110.575;
	protected double F = 30.1578;
	protected double d = 6.19662;
	protected double lossCoefficient = 0.01489;

	protected int note;
	protected double Ca;
	protected double Cf;
	protected double timeStep = 0.08;

	
	protected double kr;

	protected RealVector HCurrent;
	protected RealVector HNext;
	protected RealVector QCurrent;
	protected RealVector QNext;

	protected void initial() {
		
		note = (int) Math.round(Length / Cspeed / timeStep);
		Ca = 9.81 * F / Cspeed;
		Cf = lossCoefficient * timeStep / (2 * d * F);

		HCurrent = new ArrayRealVector(note + 1);
		HNext = new ArrayRealVector(note + 1);
		QCurrent = new ArrayRealVector(note + 1);
		QNext = new ArrayRealVector(note + 1);

		
	}

	public void iniPineQH(double Q0,Pipeline L1, Pipeline L2, Pipeline L3, Pipeline L4) {
		
		L1.kr = L1.lossCoefficient * L1.Length * Math.abs(Q0)*Q0 / (2 * 9.81 * L1.d * L1.F * L1.F) / (L1.note);
		L2.kr = L2.lossCoefficient * L2.Length * Math.abs(Q0)*Q0 / (2 * 9.81 * L2.d * L2.F * L2.F) / (L2.note);
		L3.kr = L3.lossCoefficient * L3.Length * Math.abs(Q0)*Q0 / (2 * 9.81 * L3.d * L3.F * L3.F) / (L3.note);
		L4.kr = L4.lossCoefficient * L4.Length * Math.abs(Q0)*Q0 / (2 * 9.81 * L4.d * L4.F * L4.F) / (L4.note);
		for (int i = 0; i <=L1.note; i++) {
			L1.HCurrent.setEntry(i, Hu - i * L1.kr);
		}
		for (int i = 0; i <=L2.note; i++) {
			L2.HCurrent.setEntry(i, L1.HCurrent.getEntry(L1.note) - i * L2.kr);
		}
		for (int i = 0; i <=L4.note; i++) {
			L4.HCurrent.setEntry(i, Hd + (L4.note - i) * L4.kr);
		}
		for (int i = 0; i <=L3.note; i++) {
			L3.HCurrent.setEntry(i, L4.HCurrent.getEntry(0) + (L3.note - i) * L3.kr);
		}

		L1.QCurrent = L1.QCurrent.mapAdd(Q0);
		L2.QCurrent = L2.QCurrent.mapAdd(Q0);
		L3.QCurrent = L3.QCurrent.mapAdd(Q0);
		L4.QCurrent = L4.QCurrent.mapAdd(Q0);
	}
	
	public RealVector mapAbs(RealVector vector) {
		double[] v = vector.toArray();
		for (int i = 0; i < v.length; i++) {
			v[i] = Math.abs(v[i]);
		}
		return new ArrayRealVector(v);
	}

	public void updatePipelineQH() {
		RealVector cp;
		RealVector cn;
		cp = HCurrent.getSubVector(0, note - 1).mapMultiply(Ca).add(QCurrent.getSubVector(0, note - 1))
				.subtract(QCurrent.getSubVector(0, note - 1).mapMultiply(Cf)
						.ebeMultiply(mapAbs(QCurrent.getSubVector(0, note - 1))));

		cn = QCurrent.getSubVector(2, note - 1).subtract(HCurrent.getSubVector(2, note - 1).mapMultiply(Ca))
				.subtract(QCurrent.getSubVector(2, note - 1).mapMultiply(Cf)
						.ebeMultiply(mapAbs(QCurrent.getSubVector(2, note - 1))));
		QNext.setSubVector(1, cp.add(cn).mapDivide(2));
		HNext.setSubVector(1, cp.subtract(cn).mapDivide(2 * Ca));

	}
	public void NextToCurrent() {
		HCurrent.setSubVector(0, HNext);
		QCurrent.setSubVector(0, QNext);
	}

}

class Pipeline1 extends Pipeline {
	Pipeline1() {
		// TODO Auto-generated constructor stub
		Length = 444.23;
		Cspeed = 1110.575;
		F = 30.1578;
		d = 6.19662;
		lossCoefficient = 0.014892649;
		initial();
	}

	public void updatePipeline() {
		updatePipeline1FirstNote();
		updatePipelineQH();
	}

	public void updatePipeline1FirstNote() {
		HNext.setEntry(0, Hu);
		QNext.setEntry(0, QCurrent.getEntry(1) - Ca * HCurrent.getEntry(1)
				- Cf * QCurrent.getEntry(1) * Math.abs(QCurrent.getEntry(1)) + Ca * HNext.getEntry(0));

	}

}

class Pipeline2 extends Pipeline {
	Pipeline2() {
		// TODO Auto-generated constructor stub
		Length = 983.55;
		Cspeed = 1229.4375;
		F = 14.9867;
		d = 4.368256;
		lossCoefficient = 0.026043832;
		initial();

	}
	public void updatePipeline() {
		updatePipelineQH();
	}
}

class Pipeline3 extends Pipeline {
	Pipeline3() {
		// TODO Auto-generated constructor stub
		Length = 170.4;
		Cspeed = 1065;
		F = 14.5465;
		d = 4.303624;
		lossCoefficient = 0.010132643;
		initial();
	}
	public void updatePipeline() {
		updatePipelineQH();
	}
}

class Pipeline4 extends Pipeline {
	Pipeline4() {
		// TODO Auto-generated constructor stub
		Length = 1065.2;
		Cspeed = 1024.230769;
		F = 33.9712;
		d = 6.576737;
		lossCoefficient = 0.014787462;
		initial();
	}
	public void updatePipeline() {
		updatePipelineQH();
		updatePipeline4lastNote();
	}
	public void updatePipeline4lastNote() {
		HNext.setEntry(note, Hd);
		QNext.setEntry(note, QCurrent.getEntry(note-1) + Ca * HCurrent.getEntry(note-1)
				- Cf * QCurrent.getEntry(note-1) * Math.abs(QCurrent.getEntry(note-1)) - Ca * HNext.getEntry(note));

	}
}

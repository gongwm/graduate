package zte.hx.simulation.test;

public class PidTestDemo {
	private double[] pOut;
	private double[] iOut;
	private double[] dOut;

	private double[] time;

	public void simulate() {
		double dt = 0.01;// time interval

		double totalTime = 60;// s

		int n = (int) (totalTime / dt);

		time = new double[n];// time
		double[] error = new double[n];// error
		for (int i = 0; i < n; ++i) {
			time[i] = i * dt;
			error[i] = 1.0;
		}

		pOut = new double[n];// p model
		double kp = 1.0;

		iOut = new double[n];// i model
		double ki = 1.0;

		dOut = new double[n];// d model
		double kd = 1.0;
		double t1d = 1.0;

		pOut[0] = kp * error[0];
		iOut[0] = 0.0;
		dOut[0] = error[0];

		pOut[1] = pOut[0] + kp * (error[1] - error[0]);
		iOut[1] = iOut[0] + ki * dt * error[1];
		dOut[1] = ((t1d / (t1d + dt) * dOut[0]
				+ 1.0 / (t1d + dt) * kd * (error[1] - error[0])));

		for (int k = 2; k < n; ++k) {
			double e = error[k];// current error
			double e1 = error[k - 1];// last error
			double e2 = error[k - 2];

			dOut[k] = t1d / (t1d + dt) * dOut[k - 1]
					+ 1 / (t1d + dt) * kd * (e - 2 * e1 + e2);
			pOut[k] = pOut[k - 1] + kp * (e - e1);
			iOut[k] = iOut[k - 1] + ki * dt * e;
		}
	}

	public double[] getpOut() {
		return pOut;
	}

	public double[] getiOut() {
		return iOut;
	}

	public double[] getdOut() {
		return dOut;
	}

	public double[] getTime() {
		return time;
	}
}

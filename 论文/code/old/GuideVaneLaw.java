package bll.simulation.danji;


public class GuideVaneLaw {
	
	
		
	

	public static double rejection(double timeiter, double stableTime, double y0) {
		double yper;
		if (timeiter < stableTime) {
			yper = y0;
		} else if (timeiter <= ((y0 - 0.05) / 0.02857 + stableTime)) {
			yper = y0 - 0.02857 * (timeiter - stableTime);
		} else {
			yper = 0.05 - 0.005 * (timeiter - (y0 - 0.05) / 0.02857 - stableTime);
		}

		if (yper <= 0) {
			yper = 0;
		}
		return yper * 0.7873;

	}
	public static double pumpDuanDian(double timeiter, double stableTime, double y0) {
		double yper;
		if (timeiter < stableTime) {
			yper = y0;
		} else if (timeiter <= ((y0 - 0.0432) / 0.02415 + stableTime)) {
			yper = y0 - 0.02415 * (timeiter - stableTime);
		} else {
			yper = 0.0432 - 0.0046 * (timeiter - (y0 - 0.0432) / 0.02415 - stableTime);
			
		}

		if (yper <= 0) {
			yper = 0;
		}
		return yper * 0.7873;

	}
	

	public static double turbineStart(PIDAndActuator pidAndActuator, double timeiter, double w, double y0) {
		return pidAndActuator.turbineStar(timeiter, w, y0);

	}
	
	public static double pumpTurbineStart(PIDAndActuator pidAndActuator, double timeiter) {
		return pidAndActuator.pumpStart(timeiter);

	}
	
	public static double pumpTranferToTurbine(PIDAndActuator pidAndActuator, double timeiter,double w,double y0) {
		
		return pidAndActuator.pumpTranferToTurbine(timeiter,w,y0);

	}
}

	class PIDAndActuator {
		private double u;
		private double cn_1 = 1;
		private double wn_1 = 0.9;
		private double en_1 = 0.1;
		private double en_2 = 0.1;
		private double dud_1;
		private double Ts = 0.08;
		private double kp;
		private double Ti;
		private double kid;
		private double Tid;
		private double T=0.1;

		private double y1_1;
		private double y_1;
		private double yinit;

		public double turbineStar(double timeiter, double w, double y0) {
			double yper;
			if (w <= 0.9) {
				yper = y0 + 0.26 / 30 * (timeiter - 0);
				yinit = yper;
				y_1 = yper;

			} else {
				incrementalPID(w);
				yper = actuator();
			}
			return yper * 0.7873;
		}
		
		public double pumpStart(double timeiter) {
			double yper;
			if (timeiter <= 10) {
				u=0;

			} else {
				u=0.9267;
			}
			yper = actuator();
			return yper * 0.7873;
		}
		
		
		

		public double pumpTranferToTurbine(double timeiter, double w,double y0) {
			double yper;
			if (timeiter<10) {
				u=y0;
			}else if (w<=-0.05) {
				u=0.343+0.0067;
			}else if (w<=0.6) {
				u=0.343;
			}else if (w<=0.95) {
				 u=0.22;
			}else {
				incrementalPID(w);
			}
			yper = actuator();
			return yper * 0.7873;
		}
		

		public void incrementalPID(double w) {
			double cn, wn, en, dup, dui, dud, du;
			double C = 1;
			cn = cn_1 + Ts / T * C;
			cn = saturationCalc(cn, 1, 0);
			wn = wn_1 + Ts / T * (w - wn_1);
			en = deadzoneCalc(cn - wn, 0.0006);

			dup = kp * (en - en_1);
			dui = kp / Ti * Ts * en;
			dud = Tid / (Tid + Ts) * dud_1 + Ts / (Tid + Ts) * kid * Tid * (en - 2 * en_1 + en_2);
			du = dup + dui + dud;

			u = u + du;
			cn_1 = cn;
			wn_1 = wn;
			en_2 = en_1;
			en_1 = en;
			dud_1 = dud;

		}

		public double actuator() {
			double k0 = 7;
			double Tyb = 0.05;
			double Ty = 0.3;
			double y2 = u - y_1 + yinit;
			double y2n = deadzoneCalc(y2, 0.0067);
			double u1 = k0 * y2n - y1_1;
			double y1 = y1_1 + Ts / Tyb * u1;
			double y1s = saturationCalc(y1, 0.01244, -0.00747);
			double dy = y_1 - yinit + Ts / Ty * y1s;
			double yper = saturationCalc(dy + yinit, 1.12, 0);
			y1_1 = y1;
			y_1 = yper;
			return yper;

		}

		public double saturationCalc(double u, double upperlimit, double lowerlimit) {
			double u1;
			if (u < lowerlimit) {
				u1 = lowerlimit;
			} else if (u < upperlimit) {
				u1 = u;
			} else {
				u1 = upperlimit;
			}
			return u1;

		}

		public double deadzoneCalc(double u, double eZone) {
			double u1;
			if (u < -eZone) {
				u1 = u + eZone;
			} else if (u < eZone) {
				u1 = 0;
			} else {
				u1 = u - eZone;
			}
			return u1;
		}
		
		public void setPID(double kp,double Ti,double kid,double Tid) {
			this.kp=kp;
			this.Ti=Ti;
			this.kid=kid;
			this.Tid=Tid;
			
		}
		
		public void setY_1(double wn_1,double en_1,double en_2,double y_1) {
			this.wn_1=wn_1;
			this.en_1=en_1;
			this.en_2=en_2;
			this.y_1 = y_1;
		}

	}


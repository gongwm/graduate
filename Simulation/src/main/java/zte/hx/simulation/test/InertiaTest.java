package zte.hx.simulation.test;

import java.io.File;
import java.io.PrintWriter;

import zte.hx.util.TestUtil;

/**
 * inertia block test.<br/>
 * G=k/(1+t*s)
 * 
 * @author hx
 *
 */
public class InertiaTest {
	private static double[] time;
	private static double[] out;

	public static void main(String[] args) throws Exception {
		TestUtil.timeIt(() -> doIt());

		TestUtil.print(time.length);

		// save to compare with simulink.
		PrintWriter pw = new PrintWriter(new File(
				"C:\\Users\\Administrator.WIN7U-20131225W\\Desktop\\inertiaOut.txt"));
		for (int i = 0; i < time.length; ++i) {
			pw.println(String.format("%f %f", time[i], out[i]));
		}
		pw.flush();
		pw.close();
//		TestUtil.printFirst(out, 50);
	}

	private static void doIt() {
		double dt = 0.01;// simulation related config
		double totalTime = 600;

		int n = (int) (totalTime / dt);

		double[] time = new double[n];
		double[] in = new double[n];
		for (int i = 0; i < n; ++i) {
			time[i] = i * dt;
			in[i] = 1.0;
		}

		double K = 1.0;// block related config, two parameters to figure it.
		double T = 1.0;

		double[] out = new double[n];// out iteration.
		out[0] = 0;
		for (int k = 1; k < n; ++k) {
			out[k] = T / (T + dt) * out[k - 1] + K * dt / (T + dt) * in[k];
		}

		InertiaTest.time = time;
		InertiaTest.out = out;
	}
}

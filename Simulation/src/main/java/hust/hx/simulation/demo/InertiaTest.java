package hust.hx.simulation.demo;

import hust.hx.simulation.util.PrintUtil;
import hust.hx.util.TestUtil;

public class InertiaTest {
	private static double[] time;
	private static double[] out;

	public static void main(String[] args) {
		TestUtil.timeIt(() -> simulate()); // 计时
		TestUtil.printFirst(out, 10);

		PrintUtil.printTo("D:\\out.txt", w -> { // 打印
			for (int i = 0; i < time.length; ++i) {
				w.println(String.format("%f %f", time[i], out[i]));
			}
		});
	}

	private static void simulate() {
		double dt = 0.01; // 仿真配置
		double totalTime = 300; // seconds

		int n = (int) (totalTime / dt);

		time = new double[n];
		double[] in = new double[n];
		for (int i = 0; i < n; ++i) {
			time[i] = i * dt;
			in[i] = 1.0;
		}

		double K = 1.0;
		double T = 1.0;

		out = new double[n];
		out[0] = 0;

		double c1 = T / (T + dt); // 缓存系数
		double c2 = K * dt / (T + dt);
		for (int k = 1; k < n; ++k) { // 迭代
			out[k] = c1 * out[k - 1] + c2 * in[k];
		}
	}
}

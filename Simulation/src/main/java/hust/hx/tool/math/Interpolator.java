package hust.hx.tool.math;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator;
import org.apache.commons.math3.analysis.interpolation.PiecewiseBicubicSplineInterpolator;

/**
 * 二元插值器。注意采用了工厂模式。使用工厂方法会实例化一个对象。
 * 
 * @author hx
 * 
 */
public class Interpolator {
	private BivariateFunction bf;

	private Interpolator() {
		super();
	}

	public double value(double x0, double y0) {
		return bf.value(x0, y0);
	}

	/**
	 * 二元内插器的工厂方法。
	 * 
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @param z
	 *            z[i][j]=f(x[i],y[j])
	 * @return 二元内插函数
	 */
	public static Interpolator interpolate(double[] x, double[] y, double[][] z) {
		double[][] zz = new double[x.length][y.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < y.length; j++) {
				zz[i][j] = z[j][i];
			}
		}
		Interpolator interpolator = new Interpolator();
		BivariateGridInterpolator bgi = new PiecewiseBicubicSplineInterpolator();
		interpolator.bf = bgi.interpolate(x, y, zz);
		return interpolator;
	}

}

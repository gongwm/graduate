package zte.hx.util;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * 常用的数学计算。主要功能：<br/>
 * <ul>
 * <li>计算加权和</li>
 * <li>计算double数组中的最值</li>
 * <li>计算分贝</li>
 * </ul>
 * 
 * @author hx
 * 
 */
public class MathUtil {
	/**
	 * 计算加权和。
	 * 
	 * @param value
	 *            值
	 * @param weight
	 *            权重
	 * @return 给定值和权重的加权和
	 */
	public static double weightedSum(double[] value, double[] weight) {
		double res = 0;
		for (int i = 0; i < value.length; ++i) {
			res += value[i] * weight[i];
		}
		return res;
	}

	/**
	 * 求和。
	 * 
	 * @param value
	 *            待求和的值数组
	 * @return 和
	 */
	public static double sum(double[] value) {
		double result = 0;
		for (double v : value) {
			result += v;
		}
		return result;
	}

	/**
	 * 得到数据的分贝数。
	 * 
	 * @param number
	 *            数
	 * @return 分贝数
	 */
	public static double getDB(double number) {
		return 20 * Math.log10(number);
	}

	/**
	 * 得到一维数组中的最大值。
	 * 
	 * @param doubleArray
	 *            数组
	 * @return 数组的最大值
	 */
	public static double max(double[] doubleArray) {
		double max = Double.MIN_VALUE;
		for (double d : doubleArray) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	/**
	 * 得到一维数组中的最小值。
	 * 
	 * @param doubleArray
	 *            数组
	 * @return 数组的最小值
	 */
	public static double min(double[] doubleArray) {
		double min = Double.MAX_VALUE;
		for (double d : doubleArray) {
			if (d < min) {
				min = d;
			}
		}
		return min;
	}

	public static int firstGreaterEqualIdx(double[] data, double d) {
		for (int i = 0; i < data.length; ++i) {
			if (data[i] < d) {
				continue;
			} else {
				return i;
			}
		}
		return -1;
	}

	public static int lastSmallerEqualIdx(double[] data, double d) {
		for (int j = data.length - 1; j >= 0; --j) {
			if (data[j] > d) {
				continue;
			} else {
				return j;
			}
		}
		return -1;
	}

	public static double[] getInterpolation(double[] x, double a, double b) {
		double[] y = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			y[i] = a * x[i] + b;
		}
		return y;
	}

	public static double[] toPercent(double[] data, double min, double max) {
		RealVector v = new ArrayRealVector(data);
		RealVector v1 = v.mapSubtract(min);
		if (max != min) {
			v1.mapDivideToSelf(max - min);
		}
		return v1.toArray();
	}
}

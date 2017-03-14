package hust.hx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 从txt文件中读取或写数据。
 * 
 * @author hx
 * 
 */
public class TxtEditor {
	private static final String DELIMITER = "\\s+";// 一个或者多个空格的正则表达式

	/**
	 * 读取txt中以空格分开的一行数。
	 * 
	 * @param filePath
	 * @return double类型的数组
	 */
	public static double[] readArray(String filePath) {
		return read(filePath, new ScannerOperator<double[]>() {
			@Override
			public double[] read(Scanner scanner) {
				List<Double> doubleList = new ArrayList<Double>();
				while (scanner.hasNextDouble()) {
					doubleList.add(scanner.nextDouble());
				}
				return LangUtil.toPrimitiveDoubleArray(doubleList);
			}
		});
	}

	/**
	 * 从文件中读取以空格分开，以回车换行的数据。
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 二维数组
	 */
	public static double[][] readArray2D(String filePath) {
		return read(filePath, new ScannerOperator<double[][]>() {
			@Override
			public double[][] read(Scanner scanner) {
				List<Double[]> list = new ArrayList<Double[]>();
				while (scanner.hasNextLine()) {
					String aLine = scanner.nextLine();
					double[] array = parseString(aLine, DELIMITER);
					list.add(LangUtil.toWrappedArray(array));
				}
				double[][] result = new double[list.size()][list.get(0).length];
				for (int i = 0; i < result.length; ++i) {
					result[i] = LangUtil.toPrimitiveDoubleArray(list.get(i));
				}
				return result;
			}
		});
	}

	public static void writeArray(String filePath, double[] data) {
		write(filePath, data, new WriterOperator<double[]>() {
			@Override
			public void write(PrintWriter writer, double[] data) {
				for (double d : data) {
					writer.println(d);
				}
			}
		});
	}

	/**
	 * 模板方法：以WriteOperator定义的格式向文件中写数据。
	 * 
	 * @param filePath
	 *            文件路径
	 * @param data
	 *            待写数据
	 * @param operator
	 *            对数据的格式化操作
	 */
	private static <T> void write(String filePath, T data,
			WriterOperator<T> operator) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(filePath));
			operator.write(writer, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 从文件中解析一定格式数据的函数模板。<br/>
	 * 注意：这个模板对异常不做处理，所以使用的前置条件是数据文件确实存在。
	 * 
	 * @param filePath
	 *            文件路径
	 * @param opertator
	 *            操作函数
	 * @return T类型的数据
	 */
	private static <T> T read(String filePath, ScannerOperator<T> opertator) {
		FileInputStream inStream;
		Scanner scanner = null;
		T t = null;
		try {
			inStream = new FileInputStream(new File(filePath));
			scanner = new Scanner(inStream);
			t = opertator.read(scanner);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return t;
	}

	/**
	 * 解析一行以空格分开的数字组成的字符串为一维数组。
	 * 
	 * @param aLine
	 *            一行以空格分开的数字组成的字符串
	 * @param delimiter
	 *            分隔符
	 * @return 一维数组
	 */
	private static double[] parseString(String aLine, String delimiter) {
		double[] result = {};
		String[] array = aLine.split(delimiter);
		if (array != null) {
			result = new double[array.length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = Double.parseDouble(array[i]);
			}
		}
		return result;
	}

	private static interface ScannerOperator<T> {
		T read(Scanner scanner);
	}

	private static interface WriterOperator<T> {
		void write(PrintWriter writer, T data);
	}
}

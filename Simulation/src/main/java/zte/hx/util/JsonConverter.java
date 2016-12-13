package zte.hx.util;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 单例类，完成从java对象和java数组向json格式数据的转换。
 * 
 * @author hx
 *
 */
public class JsonConverter {
	private static JsonConverter converter;
	private StringWriter sw;
	private JsonGenerator gen;

	@SuppressWarnings("deprecation")
	private JsonConverter() {
		sw = new StringWriter();
		try {
			gen = new ObjectMapper().getJsonFactory().createJsonGenerator(sw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonConverter getInstance() {
		if (converter == null) {
			converter = new JsonConverter();
		}
		return converter;
	}

	/**
	 * @param num
	 *            传入任意类型的数字
	 * @return json格式的数字
	 */
	public String convert(Number num) {
		String str = null;
		if (num instanceof Integer) {
			try {
				gen.writeNumber((Integer) num);
				str = sw.toString().trim();
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		clearStringWriter();
		return str;
	}

	/**
	 * @param array
	 *            字符串数组
	 * @return 代表json格式数组的字符串
	 */
	public String convert(String[] array) {
		String res = null;
		writeObject(array);
		res = sw.toString().trim();
		clearStringWriter();
		return res;
	}

	/**
	 * @param obj
	 *            java对象
	 * @return 代表json格式对象的字符串
	 */
	public String convert(Object obj) {
		writeObject(obj);
		String res = sw.toString().trim();
		clearStringWriter();
		return res;
	}

	/**
	 * highcharts传入的数据格式为[[x1,y1],[x2,y2],...,[xn,yn]]。
	 * 
	 * @param xSeries
	 *            x坐标
	 * @param ySeries
	 *            与x坐标一一对应的y坐标
	 * @return
	 */
	public String getHighchartsData(Number[] xSeries, Number[] ySeries) {
		Number[][] data = new Number[xSeries.length][2];
		for (int i = 0; i < xSeries.length; ++i) {
			data[i][0] = xSeries[i];
			data[i][1] = ySeries[i];
		}
		String res = convert(data);
		return res;
	}

	/**
	 * 刷新缓冲区。
	 */
	private void clearStringWriter() {
		sw.getBuffer().delete(0, sw.getBuffer().length()).trimToSize();
	}

	private void writeObject(Object obj) {
		try {
			gen.writeObject(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

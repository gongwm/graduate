package zte.hx.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 解析json字符串成为java对象。
 * 
 * @author hx
 *
 */
public class JsonParser {
	private static JsonParser parser = null;
	private ObjectMapper mapper;

	private JsonParser() {
		mapper = new ObjectMapper();
	}

	public static JsonParser getInstance() {
		if (parser == null) {
			parser = new JsonParser();
		}
		return parser;
	}

	public <T> T parse(String parsedString, Class<T> valueType) {
		T res = null;
		try {
			res = mapper.readValue(parsedString, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public <T> T[] parseArray(String parsedString, Class<T[]> valueType) {
		return parse(parsedString, valueType);
	}

	public <T> T[][] parseArray2D(String parsedString, Class<T[][]> valueType) {
		return parse(parsedString, valueType);
	}

}

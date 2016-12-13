package zte.hx.util.dao;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import zte.hx.util.LangUtil;

public class BeanSqlMapper {
	private static final Map<Class<?>, String> DERBY_TYPE_MAPPING = new HashMap<>();
	private static final Map<Class<?>, String> SQLSERVER_TYPE_MAPPING = new HashMap<>();

	static {
		DERBY_TYPE_MAPPING.put(String.class, "long varchar");
		DERBY_TYPE_MAPPING.put(int.class, "int");
		DERBY_TYPE_MAPPING.put(double.class, "double");
	}

	public static <T> String generateDDL(Class<T> clazz, String dbType) {
		Map<Class<?>, String> mapping = DERBY_TYPE_MAPPING;
		switch (dbType) {
		case "derby":
			mapping = DERBY_TYPE_MAPPING;
			break;
		case "sqlserver":
			mapping = SQLSERVER_TYPE_MAPPING;
			break;
		default:
			mapping = DERBY_TYPE_MAPPING;
		}
		String result = "create table {0}(\n{1})";
		String col = "{0} {1},\n";
		try {
			BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(
					clazz.newInstance());
			StringBuilder cols = new StringBuilder();
			for (String prop : bpsps.getReadablePropertyNames()) {
				if (prop.equals("class")) {
					continue;
				}
				cols.append(MessageFormat.format(col,
						LangUtil.toSnakeCase(prop),
						mapping.get(clazz.getDeclaredField(prop).getType())));
			}
			return MessageFormat.format(result, clazz.getSimpleName(), cols);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return "generation failed!";
	}
}

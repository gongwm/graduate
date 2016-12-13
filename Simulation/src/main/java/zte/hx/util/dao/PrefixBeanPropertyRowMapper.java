package zte.hx.util.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class PrefixBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
	private String prefix;

	private PrefixBeanPropertyRowMapper(String prefix) {
		super();
		this.prefix = prefix;
	}

	public static <K> BeanPropertyRowMapper<K> of(Class<K> mappedClass,
			String prefix) {
		BeanPropertyRowMapper<K> result = new PrefixBeanPropertyRowMapper<K>(
				prefix);
		result.setMappedClass(mappedClass);
		return result;
	}

	@Override
	protected String underscoreName(String name) {
		return prefix + "_" + super.underscoreName(name);
	}
}

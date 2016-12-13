package zte.hx.util.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import zte.hx.util.LangUtil;

/**
 * 数据库配置类。<br/>
 * 采用spring
 * jdbc模板时dao都继承这个类，并调用其内所继承的.getJdbcTemplate()获得模板，然后使用JdbcTemplate提供的模板方法，
 * 可以大大简化dao类的编写工作。<br/>
 * 另外，使用模板方法可以大大改善类的可读性。jvm是一种在类组织和编写良好（遵循面向对象编程规范）的前提下才会高效运转的机器。
 * 
 * @author hx
 * 
 */
public class JdbcDaoUtil extends JdbcDaoSupport {
	public JdbcDaoUtil() {
	}

	protected <T> List<T> queryBeans(String sql, Class<T> clazz,
			Object... objects) {
		return getJdbcTemplate().query(sql, objects,
				BeanPropertyRowMapper.newInstance(clazz));
	}

	protected <T> List<T> queryBeansWithPrefix(String sql, Class<T> clazz,
			String prefix, Object... objects) {
		return getJdbcTemplate().query(sql, objects,
				PrefixBeanPropertyRowMapper.of(clazz, prefix));
	}

	/**
	 * 查询符合bean规范的数据库记录。bean要满足：<br/>
	 * <ul>
	 * <li>具有无参构造方法</li>
	 * <li>每个属性具有getter和setter方法</li>
	 * <li>名字使用驼峰命名法，除了没有下划线，命名要和数据库字段名完全一致</li>
	 * </ul>
	 * 
	 * @param sql
	 *            查询所需sql语句
	 * @param clazz
	 *            bean的类对象，使用ClassName.class可以得到
	 * @param objects
	 *            输入参数
	 * @return 查询结果。
	 */
	protected <T> T queryBean(String sql, Class<T> clazz, Object... objects) {
		return getJdbcTemplate().queryForObject(sql,
				BeanPropertyRowMapper.newInstance(clazz), objects);
	}

	protected <T> T queryBeanWithPrefix(String sql, Class<T> clazz,
			String prefix, Object... objects) {
		return getJdbcTemplate().queryForObject(sql,
				PrefixBeanPropertyRowMapper.of(clazz, prefix), objects);
	}

	protected <T> T query(String sql, ResultSetExtractor<T> resultSetExtractor,
			Object... objects) {
		return getJdbcTemplate().query(sql, objects, resultSetExtractor);
	}

	/**
	 * 对bean的要求和queryForBean查询方法一模一样。
	 * 
	 * @param sql
	 *            插入的sql语句
	 * @param bean
	 *            bean对象
	 * @return 受影响的记录的个数。
	 */
	protected <T> int insertBean(String sql, T bean) {
		sql = sql.trim();
		int start = sql.indexOf("(") + 1;
		int end = sql.indexOf(")");
		final String[] cols = sql.substring(start, end).replaceAll(" ", "")
				.split(",");
		final int count = cols.length;
		final BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(
				bean);
		final String[] beanNames = new String[count];
		for (int i = 0; i < count; ++i) {
			beanNames[i] = LangUtil.toCamelCase(cols[i]);
		}
		return getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < count; ++i) {
					String beanName = beanNames[i];
					if (bpsps.hasValue(beanName)) {
						int type = bpsps.getSqlType(beanNames[i]);
						Object value = bpsps.getValue(beanName);
						if (value instanceof byte[]) {
							ps.setBytes(i + 1, (byte[]) value);
							continue;
						}
						ps.setObject(i + 1, value, type);
					} else {
						throw new IllegalStateException("sql cols " + cols[i]
								+ " doesn\'t match with bean property: "
								+ beanName);
					}
				}
			}
		});
	}

	protected <T> int[] insertBeans(String sql, final List<T> beans) {
		int start = sql.indexOf("(") + 1;
		int end = sql.indexOf(")");
		String[] cols = sql.substring(start, end).split(",");
		final int count = cols.length;
		final BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(
				beans.get(0));
		final String[] beanNames = new String[count];
		for (int i = 0; i < count; ++i) {
			beanNames[i] = LangUtil.toCamelCase(cols[i]);
		}
		return getJdbcTemplate().batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						for (int i = 0; i < count; ++i) {
							String beanName = beanNames[i];
							if (bpsps.hasValue(beanName)) {
								int type = bpsps.getSqlType(beanNames[i]);
								Object value = bpsps.getValue(beanName);
								if (value instanceof byte[]) {
									ps.setBytes(i + 1, (byte[]) value);
									continue;
								}
								ps.setObject(i + 1, value, type);
							} else {
								throw new IllegalStateException(
										"sql cols doesn\'t match with bean property!");
							}
						}
					}

					@Override
					public int getBatchSize() {
						return beans.size();
					}
				});
	}

	protected <T> int updateBean(String sql, T bean) {
		List<String> properties = new ArrayList<>();
		String[] set = sql
				.substring(sql.toLowerCase().indexOf("set") + 4,
						sql.toLowerCase().indexOf("where") - 1)
				.replaceAll(" ", "").replaceAll("=\\?", "").split(",");
		String[] setted = sql
				.substring(sql.toLowerCase().indexOf("where") + 6, sql.length())
				.replaceAll(" ", "").replaceAll("=\\?", "").split(",");
		for (String str : set) {
			properties.add(str);
		}
		for (String str : setted) {
			properties.add(str);
		}
		BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(
				bean);
		List<Object> values = new ArrayList<>();
		for (String property : properties) {
			values.add(bpsps.getValue(LangUtil.toCamelCase(property)));
		}
		return getJdbcTemplate().update(sql,
				values.toArray(new Object[values.size()]));
	}
}

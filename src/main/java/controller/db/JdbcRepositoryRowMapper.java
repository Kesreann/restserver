package controller.db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;


public class JdbcRepositoryRowMapper<T> implements RowMapper<T> {

	private Class<T> repositoryClass;

	public JdbcRepositoryRowMapper(Class<T> repositoryClass) {
		this.repositoryClass = repositoryClass;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		try {
			T t = BeanUtils.instantiate(repositoryClass);
			Class<?> clazz = repositoryClass;
			while (!clazz.equals(Object.class)) {
				for (Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(Transient.class)) {
						continue;
					}
					field.setAccessible(true);
					Column column = field.getAnnotation(Column.class);
					boolean mandatory = column != null;
					String name = (column != null && !StringUtils.isEmpty(column.name()) ? column.name() : field.getName()).toLowerCase();
					if (mandatory || hasColumn(rs, name)) {
						field.set(t, getValue(field, rs.getObject(name)));
					}
				}
				clazz = clazz.getSuperclass();
			}
			return t;
		} catch (Exception e) {
			throw new JdbcRepositoryException("Problem converting result set to entity.", e);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Object getValue(Field field, Object value) {
		if (value != null && field.getType().isEnum()) {
			Enumerated er = field.getAnnotation(Enumerated.class);
			if (er != null && er.value() == EnumType.ORDINAL) {
				int v = 0;
				if (value instanceof Integer) {
					v = ((Integer)value).intValue();
				}
				if (value instanceof Long) {
					v = ((Long)value).intValue();
				}
				value = field.getType().getEnumConstants()[v];

			} else {
				value = Enum.valueOf((Class) field.getType(), value.toString());
			}
		} else if (field.getType().equals(Long.class) && value instanceof Integer) {
			value = new Long((Integer)value);
		}
		return value;
	}

	private boolean hasColumn(ResultSet rs, String name) throws SQLException {
		ResultSetMetaData rsMetaData = rs.getMetaData();
		for (int i = 1; i < rsMetaData.getColumnCount() + 1; i++) {
			String columnName = rsMetaData.getColumnName(i);
			if (name.equals(columnName)) {
				return true;
			}
		}
		return false;
	}
}
package controller.db;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;


public abstract class JdbcRepository<T extends Persistable<?>, K> {
	protected SqlGenerator sqlGenerator = new SqlGenerator();
	protected abstract JdbcTemplate getJdbcTemplate();
	protected abstract Class<T> getRepositoryClass();

	protected String getTableName() {
		Table table = getRepositoryClass().getAnnotation(Table.class);
		if (table != null) {
			return table.name();
		}
		Entity entity = getRepositoryClass().getAnnotation(Entity.class);
		if (entity != null) {
			return entity.name();
		}
		throw new JdbcRepositoryException("Table name for entity " + getRepositoryClass().getSimpleName() + " not found.");
	}

	protected String getIdColumnName() {
		Field[] fields = getRepositoryClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Id id = field.getAnnotation(Id.class);
			Column column = field.getAnnotation(Column.class);
			if (id != null && column != null) {
				return StringUtils.isEmpty(column.name()) ? field.getName() : column.name();
			}
		}
		throw new JdbcRepositoryException("ID column for entity " + getRepositoryClass().getSimpleName() + " not found.");
	}

	protected Map<String, Object> convertEntityToMap(Object o) throws JdbcRepositoryException {
		try {
			Map<String, Object> map = new HashMap<>();
			Class<?> clazz = o.getClass();
			while (!clazz.equals(Object.class)) {
				for (Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(Transient.class) || "serialVersionUID".equals(field.getName()) || "$jacocoData".equals(field.getName()) ) {
						continue;
					}
					field.setAccessible(true);
					Column column = field.getAnnotation(Column.class);
					String name = column != null && !StringUtils.isEmpty(column.name()) ? column.name() : field.getName().toLowerCase();
					Object value = field.get(o);
					if (value != null && field.getType().isEnum()) {
						Enumerated er = field.getAnnotation(Enumerated.class);
						if (er != null && er.value() == EnumType.ORDINAL) {
							value = ((Enum<?>)value).ordinal();
						}
					}
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(((java.util.Date)value).getTime());
					}
					map.put(name, value);
				}
				clazz = clazz.getSuperclass();
			}
			return map;
		} catch (Exception e) {
			throw new JdbcRepositoryException("Problem converting entity to map.", e);
		}
	}

	protected Map<String, String> getColumnDefinition() {
		try {
			Map<String, String> map = new HashMap<>();
			Field[] fields = getRepositoryClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				if (column != null && !StringUtils.isEmpty(column.columnDefinition())) {
					String name = StringUtils.isEmpty(column.name()) ? field.getName() : column.name();
					map.put(name, column.columnDefinition());
				}
			}
			return map;
		} catch (Exception e) {
			throw new JdbcRepositoryException("Problem converting entity to map.", e);
		}
	}

	protected void applyId(Object o, Number n) {
		try {
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					field.set(o, n);
					return;
				}
			}
		} catch (Exception e) {
			throw new JdbcRepositoryException("Cannot set ID column for entity " + o.getClass().getSimpleName() + ".", e);
		}
	}

	public T save(T e) {
		if (e.isNew()) {
			return create(e);
		} else {
			return update(e);
		}
	}

	protected T create(T e) {
		String tableName = getTableName();
		Map<String, Object> data = convertEntityToMap(e);

		GeneratedKeyHolder key = new GeneratedKeyHolder();
		String idColumnName = getIdColumnName();
		data.remove(idColumnName);
		Object[] queryParams = data.values().toArray();
		String query = sqlGenerator.create(tableName, data.keySet(), getColumnDefinition());

		getJdbcTemplate().update((PreparedStatementCreator) con -> {
			final PreparedStatement ps = con.prepareStatement(query, new String[] { idColumnName });
			for (int i = 0; i < queryParams.length; ++i) {
				ps.setObject(i + 1, queryParams[i]);
			}
			return ps;
		}, key);
		applyId(e, key.getKey());
		return e;
	}

	protected T update(T e) {
		throw new UnsupportedOperationException();
	}

	public void updateColumnWhere(Map<String, String> columnValueMap, String where){
		getJdbcTemplate().update(sqlGenerator.update(getTableName(), columnValueMap.keySet(), columnValueMap, where));
	}

	public void deleteWhere(String where){
		getJdbcTemplate().update(sqlGenerator.delete(getTableName(), where));
	}

	public List<T> findAll() {
		return getJdbcTemplate().query(sqlGenerator.selectAll(getTableName()), new JdbcRepositoryRowMapper<>(getRepositoryClass()));
	}

	public List<T> findAllWhere(String where){
		return getJdbcTemplate().query(sqlGenerator.selectAllWhere(getTableName(), where), new JdbcRepositoryRowMapper<>(getRepositoryClass()));
	}

	public long count(String where) {
		return getJdbcTemplate().queryForObject(sqlGenerator.count(getTableName(), where), Long.class);
	}
}

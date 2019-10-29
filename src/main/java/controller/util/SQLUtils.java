package controller.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import lombok.AllArgsConstructor;

public class SQLUtils {

	@AllArgsConstructor
	public static class SQLPair {
		protected String name;
		protected Object value;
	}

	public static String escape(Object o) {
		if (o == null) {
			return "null";
		}
		if (o instanceof Number) {
			return o.toString();
		}
		if (o instanceof Date) {
			return "'" + Constants.DATE_TIME_FORMAT.format((Date) o) + "'";
		}
		if (o instanceof Boolean) {
			return o.toString();
		}
		if (o instanceof String) {
			return "'" + ((String) o).replace("'", "''") + "'";
		}
		if (o instanceof Enum<?>) {
			return "'" + o.toString() + "'";
		}
		throw new IllegalArgumentException("Unsupported type.");
	}

	public static String joinList(List<?> list) {
		if (list.isEmpty()) {
			return "";
		}
		return list.stream().map(p -> escape(p)).collect(Collectors.joining(","));
	}

	public static String insert(String table, SQLPair... pairs) {
		String columns = Arrays.asList(pairs).stream().map(p -> p.name).collect(Collectors.joining(","));
		String values = Arrays.asList(pairs).stream().map(p -> escape(p.value)).collect(Collectors.joining(","));
		return "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");";
	}

	public static String update(String table, SQLPair... pairs) {
		String values = Arrays.asList(pairs).stream().map(p -> p.name + "=" + escape(p.value)).collect(Collectors.joining(","));
		return "UPDATE " + table + " SET " + values + ";";
	}

	public static String update(String table, String where, SQLPair... pairs) {
		String values = Arrays.asList(pairs).stream().map(p -> p.name + "=" + escape(p.value)).collect(Collectors.joining(","));
		return "UPDATE " + table + " SET " + values + " WHERE " + where + ";";
	}

	public static String delete(String table, SQLPair... pairs) {
		String[] wheres = Arrays.asList(pairs).stream().map(p -> is(p.name, p.value)).collect(Collectors.toList()).toArray(new String[pairs.length]);
		return "DELETE FROM " + table + " WHERE " + and(wheres) + ";";
	}

	public static String in(Object key, Object value) {
		return key + " in (" + value +")";
	}

	public static String is(Object key, Object value) {
		return key + " = " + escape(value);
	}

	public static String or(String... ors) {
		return operation("OR", ors);
	}

	public static String and(String... ors) {
		return operation("AND", ors);
	}

	private static String operation(String separator, String... ors) {
		boolean addSeparator = false;
		StringBuilder sb = new StringBuilder();
		for (String s : ors) {
			if (StringUtils.isEmpty(s)) {
				continue;
			}
			if (addSeparator) {
				sb.append(separator).append(" ");
			}
			sb.append(s).append(" ");
			addSeparator = true;
		}
		String str = sb.toString().trim();
		return StringUtils.isEmpty(str) ? null : str;
	}

	public static String pageable(Pageable pageable) {
		if (pageable == null) {
			return null;
		}
		StringBuffer query = new StringBuffer();
		if (pageable.getSort() != null) {
			query.append(" order by");
			for (Iterator<Order> it = pageable.getSort().iterator(); it.hasNext();) {
				Order o = it.next();
				query.append(" " + o.getProperty() + " " + o.getDirection().name());
			}
		}
		query.append(" limit " + pageable.getPageSize());
		query.append(" offset " + pageable.getOffset());
		return query.toString();
	}
}

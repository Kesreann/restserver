package controller.db;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import controller.util.SQLUtils;


public class SqlGenerator {

	public static final String allColumnsClause = "*";
	public static final String SPACE = " ";
	public static final String SELECT = "SELECT";
	public static final String FROM = "FROM ";
	public static final String COMMA = ", ";
	public static final String WHERE = "WHERE";

	public String count(String tableName, String where) {
		return SELECT + SPACE + "COUNT(*) " + FROM + SPACE + tableName + " " + WHERE + " " + where ;
	}

	public String create(String tableName, Set<String> columns, Map<String, String> columnDefinition) {
		StringBuilder createQuery = new StringBuilder("INSERT INTO " + tableName + " (");
		createQuery.append(columns.stream().collect(Collectors.joining(COMMA)));
		createQuery.append(")").append(" VALUES (");
		for (Iterator<String> it = columns.iterator(); it.hasNext();) {
			String name = it.next();
			createQuery.append("?");
			if (columnDefinition.containsKey(name)) {
				createQuery.append("::"+columnDefinition.get(name));
			}
			if (it.hasNext()){
				createQuery.append(",");
			}
		}
		return createQuery.append(")").toString();
	}

	public String update(String tableName, Set<String> columns, Map<String, String> columnValueMap, String where) {
		StringBuilder createQuery = new StringBuilder("UPDATE " + tableName + " SET ");

		for (Iterator<String> it = columns.iterator(); it.hasNext();) {
			String name = it.next();
			createQuery.append(name + "= "+SQLUtils.escape(columnValueMap.get(name)));
			if (it.hasNext()){
				createQuery.append(",");
			}
		}
		if(StringUtils.isNotEmpty(where)){
			createQuery.append(SPACE + WHERE + SPACE + where);
		}
		return createQuery.toString();
	}

	public String delete(String tableName, String where) {
		StringBuilder createQuery = new StringBuilder("UPDATE " + tableName + " SET ");
		if(StringUtils.isNotEmpty(where)){
			createQuery.append(SPACE + WHERE + SPACE + where);
		}
		return createQuery.toString();
	}

	public String selectAll(String tableName) {
		return SELECT + SPACE + allColumnsClause + SPACE + FROM + tableName;
	}

	public String selectAllWhere(String tableName, String where) {
		String sql = SELECT + SPACE + allColumnsClause + SPACE + FROM + tableName;
		sql += SPACE + WHERE + SPACE + where;
		return sql;
	}
}

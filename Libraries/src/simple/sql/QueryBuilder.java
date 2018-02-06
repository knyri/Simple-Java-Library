package simple.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
	private final String baseTable;
	private final WhereBuilder where= new WhereBuilder();
	private int limit= -1, offset= -1;
	private String orderBy= "";
	private final List<String> fields= new LinkedList<String>();
	private final List<String> groupBy= new LinkedList<String>();
	private final List<TableJoin> joins= new LinkedList<TableJoin>();

	/**
	 * Run parameters for the final query
	 */
	private Map<String, Object> runArgs;
	/**
	 * The final query
	 */
	private String query;

	public QueryBuilder(String baseTable){
		this.baseTable= baseTable;
	}

	public QueryBuilder setLimit(int limit){
		this.limit= limit;
		return this;
	}
	public QueryBuilder setOffset(int offset){
		this.offset= offset;
		return this;
	}
	public QueryBuilder orderBy(String field, boolean ascending){
		orderBy += "," + field + " " + (ascending ? "ASC" : "DESC");
		return this;
	}
	public QueryBuilder groupBy(String... fields){
		for(String field: fields){
			groupBy.add(field);
		}
		return this;
	}
	public QueryBuilder addField(String field){
		fields.add(field);
		return this;
	}
	public QueryBuilder leftJoin(String table, String on){
		joins.add(new TableJoin("left", table, on));
		return this;
	}
	public QueryBuilder leftJoin(String table, WhereBuilder on){
		joins.add(new TableJoin("left", table, on));
		return this;
	}
	public QueryBuilder rightJoin(String table, String on){
		joins.add(new TableJoin("right", table, on));
		return this;
	}
	public QueryBuilder rightJoin(String table, WhereBuilder on){
		joins.add(new TableJoin("right", table, on));
		return this;
	}
	public QueryBuilder join(String table, String on){
		joins.add(new TableJoin("", table, on));
		return this;
	}
	public QueryBuilder join(String table, WhereBuilder on){
		joins.add(new TableJoin("", table, on));
		return this;
	}
	public QueryBuilder innerJoin(String table, String on){
		joins.add(new TableJoin("inner", table, on));
		return this;
	}
	public QueryBuilder innerJoin(String table, WhereBuilder on){
		joins.add(new TableJoin("inner", table, on));
		return this;
	}
	public QueryBuilder outerJoin(String table, String on){
		joins.add(new TableJoin("outer", table, on));
		return this;
	}
	public QueryBuilder outerJoin(String table, WhereBuilder on){
		joins.add(new TableJoin("outer", table, on));
		return this;
	}
	public WhereBuilder getWhereBuilder(){
		return where;
	}
	public QueryBuilder build(){
		runArgs= new HashMap<String, Object>();
		runArgs.putAll(where.getValues());
		StringBuilder query= new StringBuilder("SELECT ");
		for(String field: fields){
			query.append(field).append(',');
		}
		query.setLength(query.length() - 1);

		query
			.append(" FROM ")
			.append(baseTable)
		;

		for(TableJoin join: joins){
			query
				.append(' ')
				.append(join.getType())
				.append(" JOIN ")
				.append(join.getTable())
			;
			if(!join.getOn().isEmpty()){
				query
					.append(" ON (")
					.append(join.getOn())
					.append(')')
				;
				runArgs.putAll(join.getOnValues());
			}
		}

		if(!where.getWhere().isEmpty()){
			query
				.append(" WHERE ")
				.append(where.getWhere())
			;
		}

		if(!groupBy.isEmpty()){
			query.append(" GROUP BY ");
			for(String field: groupBy){
				query
					.append(field)
					.append(',')
				;
			}
			query.setLength(query.length() - 1);
		}

		if(!orderBy.isEmpty()){
			query
				.append(" ORDER BY ")
				.append(orderBy)
			;
		}

		if(limit > 0){
			query
				.append(" LIMIT ")
				.append(Integer.toString(limit, 10))
			;
		}

		if(offset > 0){
			query
				.append(" OFFSET ")
				.append(Integer.toString(offset, 10))
			;
		}

		this.query= query.toString();
		return this;
	}
	public String getQuery(){
		return query;
	}
	public Map<String, Object> getParams(){
		return runArgs;
	}
	public NamedParamStatement prepare(Connection con) throws SQLException{
		NamedParamStatement stm= new NamedParamStatement(con, query);
		stm.setAll(runArgs);
		return stm;
	}
	public NamedParamStatement prepare(Connection con, int resultSetType, int resultSetConcurrency) throws SQLException{
		NamedParamStatement stm= new NamedParamStatement(con, query, resultSetType, resultSetConcurrency);
		stm.setAll(runArgs);
		return stm;
	}
	private final static class TableJoin{
		private final String
			type,
			table,
			on;
		private final WhereBuilder onWb;
		public TableJoin(String type, String table, String on){
			this.type= type;
			this.table= table;
			this.on= (on == null ? "" : on);
			onWb= null;
		}
		public TableJoin(String type, String table, WhereBuilder on){
			this.type= type;
			this.table= table;
			this.on= null;
			onWb= on;
		}
		public String getType(){
			return type;
		}
		public String getTable(){
			return table;
		}
		public String getOn(){
			return on == null ? onWb.getWhere() : on;
		}
		@SuppressWarnings("unchecked")
		public Map<String, Object> getOnValues(){
			return onWb == null ? Collections.EMPTY_MAP : onWb.getValues();
		}
	}
}

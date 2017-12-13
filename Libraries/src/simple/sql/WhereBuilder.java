package simple.sql;

import java.util.HashMap;
import java.util.Map;

public class WhereBuilder {
	private static volatile long instanceCnt= 0;
	private static Object instanceLock= new Object();
	private final String prefix;
	private int paramIdx= 0;
	private final Map<String, Object> values= new HashMap<String, Object>();
	private final StringBuilder where= new StringBuilder();
	public WhereBuilder(){
		synchronized(instanceLock){
			// Attempt to be thread safe
			prefix= ":WhereBuilder" + Long.toString(instanceCnt, 10);
			if(++instanceCnt == Long.MAX_VALUE){
				instanceCnt= 0;
			}
		}
	}
	public final String getWhere(){
		return where.toString();
	}
	public final Map<String, Object> getValues(){
		return values;
	}
	/**
	 * @param v
	 * @return A placeholder for the added value
	 */
	private final String addValue(Object v){
		String ref= prefix + values.size();
		values.put(ref, v);
		return ref;
	}
	public final WhereBuilder and(){
		if(paramIdx > 0){
			where.append(" AND ");
		}
		return this;
	}
	public final WhereBuilder or(){
		if(paramIdx > 0){
			where.append(" OR ");
		}
		return this;
	}
	public final WhereBuilder and(WhereBuilder wb){
		and();
		where.append('(').append(wb).append(')');
		values.putAll(wb.values);
		return this;
	}
	public final WhereBuilder or(WhereBuilder wb){
		or();
		where.append('(').append(wb).append(')');
		values.putAll(wb.values);
		return this;
	}
	public WhereBuilder isNull(String expr){
		where
			.append(expr)
			.append(" IS NULL");
		return this;
	}
	public WhereBuilder isNotNull(String expr){
		where
			.append(expr)
			.append(" IS NOT NULL");
		return this;
	}
	public WhereBuilder like(String expr, String like){
		where
			.append(expr)
			.append(" LIKE ")
			.append(addValue(like));
		return this;
	}
	public WhereBuilder between(String expr, Object start, Object end){
		where
			.append(expr)
			.append("BETWEEN ")
			.append(addValue(start))
			.append(" AND ")
			.append(addValue(end));
		return this;
	}
	public WhereBuilder where(String expr, String op, Object value){
		where
			.append(expr)
			.append(' ')
			.append(op)
			.append(' ')
			.append(addValue(value));
		return this;
	}
	public WhereBuilder in(String expr, Object... values){
		where
			.append(expr)
			.append(" IN {");
		for(Object value: values){
			where.append(addValue(value)).append(',');
		}
		where.setCharAt(where.length() - 1, ')');
		return this;
	}
}

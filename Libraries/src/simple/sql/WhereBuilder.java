package simple.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * For use with {@link NamedParamStatement}
 */
public class WhereBuilder {
	private static final AtomicLong instanceCnt= new AtomicLong();
	private final String prefix;
	private final Map<String, Object> values= new HashMap<String, Object>();
	private final StringBuilder where= new StringBuilder();
	private WhereBuilder nested= null;
	public WhereBuilder(){
		prefix= ":WhereBuilder" + instanceCnt.getAndIncrement() + "v";
	}
	/**
	 * The where clause without the WHERE keyword
	 * @return
	 */
	public final String getWhere(){
		String wh= where.toString();
		if(wh.endsWith(" AND ")) {
			wh= wh.substring(0, wh.length() - 5);
		}
		if(wh.endsWith(" OR ")) {
			wh= wh.substring(0, wh.length() - 4);
		}
		return wh;
	}
	/**
	 * All the values supplied
	 * @return A map of the placeholders and values
	 */
	public final Map<String, Object> getValues(){
		return values;
	}
	/**
	 * Creates a named placeholder for the value
	 * @param v
	 * @return A placeholder for the added value
	 */
	private final String addValue(Object v){
		String ref= prefix + values.size();
		values.put(ref, v);
		return ref;
	}
	/**
	 * Appends ' AND '
	 * @return
	 */
	public final WhereBuilder and(){
		if(nested != null) {
			nested.and();
		}else {
			if(!values.isEmpty()){
				where.append(" AND ");
			}
		}
		return this;
	}
	/**
	 * Appends ' OR '
	 * @return
	 */
	public final WhereBuilder or(){
		if(nested != null) {
			nested.or();
		}else {
			if(!values.isEmpty()){
				where.append(" OR ");
			}
		}
		return this;
	}
	/**
	 * AND (wb)
	 * Further changes to wb will not be applied.
	 * @param wb
	 * @return
	 */
	public final WhereBuilder and(WhereBuilder wb){
		if(nested != null) {
			nested.and(wb);
		}else if(!wb.getWhere().trim().isEmpty()) {
			and();
			where.append('(').append(wb.getWhere()).append(')');
			values.putAll(wb.values);
		}
		return this;
	}
	/**
	 * OR (wb)
	 * Further changes to wb will not by applied.
	 * @param wb
	 * @return
	 */
	public final WhereBuilder or(WhereBuilder wb){
		if(nested != null) {
			nested.or(wb);
		}else if(!wb.getWhere().trim().isEmpty()) {
			or();
			where.append('(').append(wb.getWhere()).append(')');
			values.putAll(wb.values);
		}
		return this;
	}
	/**
	 * expr IS NULL
	 * @param expr
	 * @return
	 */
	public WhereBuilder isNull(String expr){
		if(nested != null) {
			nested.isNull(expr);
		}else {
			where
				.append(expr)
				.append(" IS NULL");
		}
		return this;
	}
	/**
	 * expr IS NOT NULL
	 * @param expr
	 * @return
	 */
	public WhereBuilder isNotNull(String expr){
		if(nested != null) {
			nested.isNotNull(expr);
		}else {
			where
				.append(expr)
				.append(" IS NOT NULL");
		}
		return this;
	}
	/**
	 * expr LIKE pattern
	 * @param expr
	 * @param pattern
	 * @return
	 */
	public WhereBuilder like(String expr, String pattern){
		if(nested != null) {
			nested.like(expr, pattern);
		}else {
			where
				.append(expr)
				.append(" LIKE ")
				.append(addValue(pattern));
		}
		return this;
	}
	/**
	 * expr NOT LIKE pattern
	 * @param expr
	 * @param pattern
	 * @return
	 */
	public WhereBuilder notLike(String expr, String pattern){
		if(nested != null) {
			nested.notLike(expr, pattern);
		}else {
			where
				.append(expr)
				.append(" NOT LIKE ")
				.append(addValue(pattern));
		}
		return this;
	}
	/**
	 * expr BETWEEN start AND end
	 * @param expr
	 * @param start
	 * @param end
	 * @return
	 */
	public WhereBuilder between(String expr, Object start, Object end){
		if(nested != null) {
			nested.between(expr, start, end);
		}else {
			where
				.append(expr)
				.append(" BETWEEN ")
				.append(addValue(start))
				.append(" AND ")
				.append(addValue(end));
		}
		return this;
	}
	/**
	 * expr NOT BETWEEN start AND end
	 * @param expr
	 * @param start
	 * @param end
	 * @return
	 */
	public WhereBuilder notBetween(String expr, Object start, Object end){
		if(nested != null) {
			nested.notBetween(expr, start, end);
		}else {
			where
				.append(expr)
				.append(" NOT BETWEEN ")
				.append(addValue(start))
				.append(" AND ")
				.append(addValue(end));
		}
		return this;
	}
	/**
	 * expr op value
	 * @param expr
	 * @param op
	 * @param value
	 * @return
	 */
	public WhereBuilder where(String expr, String op, Object value){
		if(nested != null) {
			nested.where(expr, op, value);
		}else {
			where
				.append(expr)
				.append(' ')
				.append(op)
				.append(' ')
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr < value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder lessThan(String expr, Object value){
		if(nested != null) {
			nested.lessThan(expr, value);
		}else {
			where
				.append(expr)
				.append(" < ")
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr = value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder equal(String expr, Object value){
		if(nested != null) {
			nested.equal(expr, value);
		}else {
			where
				.append(expr)
				.append(" = ")
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr > value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder greaterThan(String expr, Object value){
		if(nested != null) {
			nested.greaterThan(expr, value);
		}else {
			where
				.append(expr)
				.append(" > ")
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr != value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder notEqual(String expr, Object value){
		if(nested != null) {
			nested.notEqual(expr, value);
		}else {
			where
				.append(expr)
				.append(" != ")
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr <= value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder lessThanOrEqual(String expr, Object value){
		if(nested != null) {
			nested.lessThanOrEqual(expr, value);
		}else {
			where
				.append(expr)
				.append(" <= ")
				.append(addValue(value));
		}
		return this;
	}
	/**
	 * expr >= value
	 * @param expr
	 * @param value
	 * @return
	 */
	public WhereBuilder greaterThanOrEqual(String expr, Object value){
		if(nested != null) {
			nested.greaterThanOrEqual(expr, value);
		}else {
			where
				.append(expr)
				.append(" >= ")
				.append(addValue(value));
		}
		return this;
	}
	/** Adds a '('
	 * @return
	 */
	public WhereBuilder startGroup(){
		if(nested != null) {
			nested.startGroup();
		}else {
			where.append('(');
			if(nested == null) {
				nested= new WhereBuilder();
			}
		}
		return this;
	}
	/** Adds a ')'
	 * @return
	 */
	public WhereBuilder endGroup(){
		if(nested != null) {
			if(nested.nested == null) {
				String wh= nested.getWhere();
				if(wh.trim().isEmpty()) {
					where.setLength(where.length() - 1);
				}else {
					where.append(nested.getWhere());
					values.putAll(nested.values);
					where.append(')');
				}
				nested= null;
			}else {
				nested.endGroup();
			}
		}
		return this;
	}
	/**
	 * expr IN (values[0], values[1], ...)
	 * @param expr
	 * @param values
	 * @return
	 */
	public WhereBuilder in(String expr, Object... values){
		if(nested != null) {
			nested.in(expr, values);
		}else {
			where
				.append(expr)
				.append(" IN (");
			for(Object value: values){
				where.append(addValue(value)).append(',');
			}
			where.setCharAt(where.length() - 1, ')');
		}
		return this;
	}
	/**
	 * expr IN (values[0], values[1], ...)
	 * @param expr
	 * @param values
	 * @return
	 */
	public WhereBuilder in(String expr, Collection<?> values){
		if(nested != null) {
			nested.in(expr, values);
		}else {
			where
				.append(expr)
				.append(" IN (");
			for(Object value: values){
				where.append(addValue(value)).append(',');
			}
			where.setCharAt(where.length() - 1, ')');
		}
		return this;
	}
	/**
	 * expr NOT IN (...)
	 * @param expr
	 * @param values
	 * @return
	 */
	public WhereBuilder notIn(String expr, Object... values){
		if(nested != null) {
			nested.notIn(expr, values);
		}else {
			where
				.append(expr)
				.append(" NOT IN (");
			for(Object value: values){
				where.append(addValue(value)).append(',');
			}
			where.setCharAt(where.length() - 1, ')');
		}
		return this;
	}
	/**
	 * expr NOT IN (...)
	 * @param expr
	 * @param values
	 * @return
	 */
	public WhereBuilder notIn(String expr, Collection<?> values){
		if(nested != null) {
			nested.notIn(expr, values);
		}else {
			where
				.append(expr)
				.append(" NOT IN (");
			for(Object value: values){
				where.append(addValue(value)).append(',');
			}
			where.setCharAt(where.length() - 1, ')');
		}
		return this;
	}
	/**
	 * Adds the literal to the where clause
	 * @param literal
	 * @return
	 */
	public WhereBuilder literal(String literal){
		if(nested != null) {
			nested.literal(literal);
		}else {
			where.append(literal);
		}
		return this;
	}
}

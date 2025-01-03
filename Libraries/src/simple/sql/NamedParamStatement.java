package simple.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows for :paramName style parameter place holders. This version
 * does not support duplicate place holders.
 * @author piercekc
 */
public class NamedParamStatement implements PreparedStatement{
	private static final Pattern fieldFinder= Pattern.compile(":[a-zA-Z0-9]+");
	private final Map<String, List<Integer>> fields= new HashMap<String, List<Integer>>();
	private final PreparedStatement stm;
	private String processStm(String stm){
		Matcher finder= fieldFinder.matcher(stm);
		int fieldIdx= 1;
		while(finder.find()){
			if(!fields.containsKey(finder.group())) {
				fields.put(finder.group(), new ArrayList<>());
			}
			fields.get(finder.group()).add(fieldIdx++);
//			fields.put(finder.group(), fieldIdx++);
		}
		return finder.replaceAll("?");
	}
	public NamedParamStatement(Connection con, String stm) throws SQLException{
		this.stm= con.prepareStatement(processStm(stm));
	}
	public NamedParamStatement(Connection con, String stm, int resultSetType, int resultSetConcurrency) throws SQLException{
		this.stm= con.prepareStatement(processStm(stm), resultSetType, resultSetConcurrency);
	}
	public NamedParamStatement(Connection con, String stm, Map<String, Object> values) throws SQLException{
		this(con, stm);
		setAll(values);
	}
	public NamedParamStatement(Connection con, String stm, int resultSetType, int resultSetConcurrency, Map<String, Object> values) throws SQLException{
		this(con, stm, resultSetType, resultSetConcurrency);
		setAll(values);
	}

	/**
	 * Attempts to set all the values.
	 * InputStreams are added using setBlob(...)
	 * Readers are added using setClob(...)
	 * @param values
	 * @throws SQLException
	 */
	public void setAll(Map<String, Object> values) throws SQLException{
		for(Entry<String, Object> value: values.entrySet()){
			if(value.getValue() instanceof String){
				set(value.getKey(), (String)value.getValue());
			}else
			if(value.getValue() instanceof CharSequence){
				set(value.getKey(), value.getValue().toString());
			}else
			if(value.getValue() instanceof Integer){
				set(value.getKey(), ((Integer)value.getValue()).intValue());
			}else
			if(value.getValue() instanceof Double){
				set(value.getKey(), ((Double)value.getValue()).doubleValue());
			}else
			if(value.getValue() instanceof Float){
				set(value.getKey(), ((Float)value.getValue()).floatValue());
			}else
			if(value.getValue() instanceof Short){
				set(value.getKey(), ((Short)value.getValue()).shortValue());
			}else
			if(value.getValue() instanceof Byte){
				set(value.getKey(), ((Byte)value.getValue()).byteValue());
			}else
			if(value.getValue() instanceof Date){
				set(value.getKey(), (Date)value.getValue());
			}else
			if(value.getValue() instanceof Time){
				set(value.getKey(), (Time)value.getValue());
			}else
			if(value.getValue() instanceof Timestamp){
				set(value.getKey(), (Timestamp)value.getValue());
			}else
			if(value.getValue() instanceof Clob){
				set(value.getKey(), (Clob)value.getValue());
			}else
			if(value.getValue() instanceof Blob){
				set(value.getKey(), (Blob)value.getValue());
			}else
			if(value.getValue() instanceof NClob){
				set(value.getKey(), (NClob)value.getValue());
			}else
			if(value.getValue() instanceof BigDecimal){
				set(value.getKey(), (BigDecimal)value.getValue());
			}else
			if(value.getValue() instanceof URL){
				set(value.getKey(), (URL)value.getValue());
			}else
			if(value.getValue() instanceof Array){
				set(value.getKey(), (Array)value.getValue());
			}else
			if(value.getValue() instanceof Ref){
				set(value.getKey(), (Ref)value.getValue());
			}else
			if(value.getValue() instanceof RowId){
				set(value.getKey(), (RowId)value.getValue());
			}else
			if(value.getValue() instanceof SQLXML){
				set(value.getKey(), (SQLXML)value.getValue());
			}else
			if(value.getValue() instanceof InputStream){
				setBlob(value.getKey(), (InputStream)value.getValue());
			}else
			if(value.getValue() instanceof Reader){
				setClob(value.getKey(), (Reader)value.getValue());
			}else{
				set(value.getKey(), value.getValue());
			}
		}
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return stm.isWrapperFor(iface);
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return stm.unwrap(iface);
	}
	@Override
	public void addBatch(String sql) throws SQLException {
		stm.addBatch(sql);
	}
	@Override
	public void cancel() throws SQLException {
		stm.cancel();
	}
	@Override
	public void clearBatch() throws SQLException {
		stm.clearBatch();
	}
	@Override
	public void clearWarnings() throws SQLException {
		stm.clearWarnings();
	}
	@Override
	public void close() throws SQLException {
		stm.close();
	}
	@Override
	public void closeOnCompletion() throws SQLException {
		stm.closeOnCompletion();
	}
	@Override
	public boolean execute(String sql) throws SQLException {
		return stm.execute(sql);
	}
	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return stm.execute(sql, autoGeneratedKeys);
	}
	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return stm.execute(sql, columnIndexes);
	}
	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return stm.execute(sql, columnNames);
	}
	@Override
	public int[] executeBatch() throws SQLException {
		return stm.executeBatch();
	}
	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		return stm.executeQuery();
	}
	@Override
	public int executeUpdate(String sql) throws SQLException {
		return stm.executeUpdate();
	}
	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return stm.executeUpdate(sql, autoGeneratedKeys);
	}
	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return stm.executeUpdate(sql, columnIndexes);
	}
	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return stm.executeUpdate(sql, columnNames);
	}
	@Override
	public Connection getConnection() throws SQLException {
		return stm.getConnection();
	}
	@Override
	public int getFetchDirection() throws SQLException {
		return stm.getFetchDirection();
	}
	@Override
	public int getFetchSize() throws SQLException {
		return stm.getFetchSize();
	}
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return stm.getGeneratedKeys();
	}
	@Override
	public int getMaxFieldSize() throws SQLException {
		return stm.getMaxFieldSize();
	}
	@Override
	public int getMaxRows() throws SQLException {
		return stm.getMaxRows();
	}
	@Override
	public boolean getMoreResults() throws SQLException {
		return stm.getMoreResults();
	}
	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return stm.getMoreResults(current);
	}
	@Override
	public int getQueryTimeout() throws SQLException {
		return stm.getQueryTimeout();
	}
	@Override
	public ResultSet getResultSet() throws SQLException {
		return stm.getResultSet();
	}
	@Override
	public int getResultSetConcurrency() throws SQLException {
		return stm.getResultSetConcurrency();
	}
	@Override
	public int getResultSetHoldability() throws SQLException {
		return stm.getResultSetHoldability();
	}
	@Override
	public int getResultSetType() throws SQLException {
		return stm.getResultSetType();
	}
	@Override
	public int getUpdateCount() throws SQLException {
		return stm.getUpdateCount();
	}
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return stm.getWarnings();
	}
	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return stm.isCloseOnCompletion();
	}
	@Override
	public boolean isClosed() throws SQLException {
		return stm.isClosed();
	}
	@Override
	public boolean isPoolable() throws SQLException {
		return stm.isPoolable();
	}
	@Override
	public void setCursorName(String name) throws SQLException {
		stm.setCursorName(name);
	}
	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		stm.setEscapeProcessing(enable);
	}
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		stm.setFetchDirection(direction);
	}
	@Override
	public void setFetchSize(int rows) throws SQLException {
		stm.setFetchSize(rows);
	}
	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		stm.setMaxFieldSize(max);
	}
	@Override
	public void setMaxRows(int max) throws SQLException {
		stm.setMaxRows(max);
	}
	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		stm.setPoolable(poolable);
	}
	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		stm.setQueryTimeout(seconds);
	}
	@Override
	public void addBatch() throws SQLException {
		stm.addBatch();
	}
	@Override
	public void clearParameters() throws SQLException {
		stm.clearParameters();
	}
	@Override
	public boolean execute() throws SQLException {
		return stm.execute();
	}
	@Override
	public ResultSet executeQuery() throws SQLException {
		return stm.executeQuery();
	}
	@Override
	public int executeUpdate() throws SQLException {
		return stm.executeUpdate();
	}
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return stm.getMetaData();
	}
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return stm.getParameterMetaData();
	}
	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		stm.setArray(parameterIndex, x);
	}
	public void set(String param, Array value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setArray(i, value);
		}
//		stm.setArray(fields.get(param), value);
	}
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		stm.setAsciiStream(parameterIndex, x);
	}
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setAsciiStream(parameterIndex, x, length);
	}
	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		stm.setAsciiStream(parameterIndex, x, length);
	}
	public void setAsciiStream(String param, InputStream value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setAsciiStream(i, value);
		}
//		stm.setAsciiStream(fields.get(param), value);
	}
	public void setAsciiStream(String param, InputStream value, int length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setAsciiStream(i, value,length);
		}
//		stm.setAsciiStream(fields.get(param), value, length);
	}
	public void setAsciiStream(String param, InputStream value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setAsciiStream(i, value,length);
		}
//		stm.setAsciiStream(fields.get(param), value, length);
	}
	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		stm.setBigDecimal(parameterIndex, x);
	}
	public void set(String param, BigDecimal value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBigDecimal(i, value);
		}
//		stm.setBigDecimal(fields.get(param), value);
	}
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		stm.setBinaryStream(parameterIndex, x);
	}
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setBinaryStream(parameterIndex, x, length);
	}
	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		stm.setBinaryStream(parameterIndex, x, length);
	}
	public void setBinaryStream(String param, InputStream value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBinaryStream(i, value);
		}
//		stm.setBinaryStream(fields.get(param), value);
	}
	public void setBinaryStream(String param, InputStream value, int length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBinaryStream(i, value,length);
		}
//		stm.setBinaryStream(fields.get(param), value, length);
	}
	public void setBinaryStream(String param, InputStream value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBinaryStream(i, value,length);
		}
//		stm.setBinaryStream(fields.get(param), value, length);
	}
	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		stm.setBlob(parameterIndex, x);
	}
	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		stm.setBlob(parameterIndex, inputStream);
	}
	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		stm.setBlob(parameterIndex, inputStream, length);
	}
	public void set(String param, Blob value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBlob(i, value);
		}
//		stm.setBlob(fields.get(param), value);
	}
	public void setBlob(String param, InputStream value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBlob(i, value);
		}
//		stm.setBlob(fields.get(param), value);
	}
	public void setBlob(String param, InputStream value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBlob(i, value);
		}
//		stm.setBlob(fields.get(param), value, length);
	}
	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		stm.setBoolean(parameterIndex, x);
	}
	public void set(String param, boolean value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBoolean(i, value);
		}
//		stm.setBoolean(fields.get(param), value);
	}
	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		stm.setByte(parameterIndex, x);
	}
	public void set(String param, byte value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setByte(i, value);
		}
//		stm.setByte(fields.get(param), value);
	}
	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		stm.setBytes(parameterIndex, x);
	}
	public void set(String param, byte... value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setBytes(i, value);
		}
//		stm.setBytes(fields.get(param), value);
	}
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		stm.setCharacterStream(parameterIndex, reader);
	}
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		stm.setCharacterStream(parameterIndex, reader, length);
	}
	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		stm.setCharacterStream(parameterIndex, reader, length);
	}
	public void setCharacterStream(String param, Reader value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setCharacterStream(i, value);
		}
//		stm.setCharacterStream(fields.get(param), value);
	}
	public void setCharacterStream(String param, Reader value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setCharacterStream(i, value, length);
		}
//		stm.setCharacterStream(fields.get(param), value, length);
	}
	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		stm.setClob(parameterIndex, x);
	}
	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		stm.setClob(parameterIndex, reader);
	}
	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		stm.setClob(parameterIndex, reader, length);
	}
	public void set(String param, Clob value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setClob(i, value);
		}
//		stm.setClob(fields.get(param), value);
	}
	public void setClob(String param, Reader value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setClob(i, value);
		}
//		stm.setClob(fields.get(param), value);
	}
	public void setClob(String param, Reader value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setClob(i, value, length);
		}
//		stm.setClob(fields.get(param), value, length);
	}
	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		stm.setDate(parameterIndex, x);
	}
	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		stm.setDate(parameterIndex, x, cal);
	}
	public void set(String param, Date value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setDate(i, value);
		}
//		stm.setDate(fields.get(param), value);
	}
	public void set(String param, Date value, Calendar c) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setDate(i, value, c);
		}
//		stm.setDate(fields.get(param), value, c);
	}
	public void setDate(String param, Instant time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setDate(i, new Date(time.toEpochMilli()));
		}
//		stm.setDate(fields.get(param), new Date(time.toEpochMilli()));
	}
	public void setDate(String param, long time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setDate(i, new Date(time));
		}
//		stm.setDate(fields.get(param), new Date(time));
	}
	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		stm.setDouble(parameterIndex, x);
	}
	public void set(String param, double value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setDouble(i, value);
		}
//		stm.setDouble(fields.get(param), value);
	}
	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		stm.setFloat(parameterIndex, x);
	}
	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		stm.setInt(parameterIndex, x);
	}
	public void set(String param, int value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setInt(i, value);
		}
//		stm.setInt(fields.get(param), value);
	}
	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		stm.setLong(parameterIndex, x);
	}
	public void set(String param, long value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setLong(i, value);
		}
//		stm.setLong(fields.get(param), value);
	}
	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		stm.setNCharacterStream(parameterIndex, value);
	}
	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		stm.setNCharacterStream(parameterIndex, value);
	}
	public void setNCharacterStream(String param, Reader value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNCharacterStream(i, value);
		}
//		stm.setNCharacterStream(fields.get(param), value);
	}
	public void setNCharacterStream(String param, Reader value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNCharacterStream(i, value, length);
		}
//		stm.setNCharacterStream(fields.get(param), value, length);
	}
	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		stm.setNClob(parameterIndex, value);
	}
	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		stm.setNClob(parameterIndex, reader);
	}
	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		stm.setNClob(parameterIndex, reader, length);
	}
	public void set(String param, NClob value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNClob(i, value);
		}
//		stm.setNClob(fields.get(param), value);
	}
	public void setNClob(String param, Reader value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNClob(i, value);
		}
//		stm.setNClob(fields.get(param), value);
	}
	public void setNClob(String param, Reader value, long length) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNClob(i, value, length);
		}
//		stm.setNClob(fields.get(param), value, length);
	}
	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		stm.setNString(parameterIndex, value);
	}
	public void setNString(String param, String value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNString(i, value);
		}
//		stm.setNString(fields.get(param), value);
	}
	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		stm.setNull(parameterIndex, sqlType);
	}
	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		stm.setNull(parameterIndex, sqlType, typeName);
	}
	public void setNull(String param, int sqlType) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNull(i, sqlType);
		}
//		stm.setNull(fields.get(param), sqlType);
	}
	public void setNull(String param, int sqlType, String typeName) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setNull(i, sqlType, typeName);
		}
//		stm.setNull(fields.get(param), sqlType, typeName);
	}
	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		stm.setObject(parameterIndex, x);
	}
	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		stm.setObject(parameterIndex, x, targetSqlType);
	}
	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		stm.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}
	public void set(String param, Object value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setObject(i, value);
		}
//		stm.setObject(fields.get(param), value);
	}
	public void set(String param, Object value, int targetSqlType) throws SQLException {
		for(Integer i: fields.get(param)) {
			stm.setObject(i, value, targetSqlType);
		}
//		stm.setObject(fields.get(param), value, targetSqlType);
	}
	public void set(String param, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		for(Integer i: fields.get(param)) {
			stm.setObject(i, x, targetSqlType, scaleOrLength);
		}
//		stm.setObject(fields.get(param), x, targetSqlType, scaleOrLength);
	}
	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		stm.setRef(parameterIndex, x);
	}
	public void set(String param, Ref value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setRef(i, value);
		}
//		stm.setRef(fields.get(param), value);
	}
	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		stm.setRowId(parameterIndex, x);
	}
	public void set(String param, RowId value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setRowId(i, value);
		}
//		stm.setRowId(fields.get(param), value);
	}
	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		stm.setSQLXML(parameterIndex, xmlObject);
	}
	public void set(String param, SQLXML value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setSQLXML(i, value);
		}
//		stm.setSQLXML(fields.get(param), value);
	}
	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		stm.setShort(parameterIndex, x);
	}
	public void set(String param, short value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setShort(i, value);
		}
//		stm.setShort(fields.get(param), value);
	}
	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		stm.setString(parameterIndex, x);
	}
	public void set(String param, String value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setString(i, value);
		}
//		stm.setString(fields.get(param), value);
	}
	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		stm.setTime(parameterIndex, x);
	}
	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		stm.setTime(parameterIndex, x, cal);
	}
	public void set(String param, Time value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTime(i, value);
		}
//		stm.setTime(fields.get(param), value);
	}
	public void set(String param, Time value, Calendar c) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTime(i, value, c);
		}
//		stm.setTime(fields.get(param), value, c);
	}
	public void setTime(String param, Instant time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTime(i, new Time(time.toEpochMilli()));
		}
//		stm.setTime(fields.get(param), new Time(time.toEpochMilli()));
	}
	public void setTime(String param, long time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTime(i, new Time(time));
		}
//		stm.setTime(fields.get(param), new Time(time));
	}
	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		stm.setTimestamp(parameterIndex, x);
	}
	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		stm.setTimestamp(parameterIndex, x, cal);
	}
	public void set(String param, Timestamp value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTimestamp(i, value);
		}
//		stm.setTimestamp(fields.get(param), value);
	}
	public void set(String param, Timestamp value, Calendar c) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTimestamp(i, value, c);
		}
//		stm.setTimestamp(fields.get(param), value, c);
	}
	public void setTimestamp(String param, Instant time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTimestamp(i, new Timestamp(time.toEpochMilli()));
		}
//		stm.setTimestamp(fields.get(param), new Timestamp(time.toEpochMilli()));
	}
	public void setTimestamp(String param, long time) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setTimestamp(i, new Timestamp(time));
		}
//		stm.setTimestamp(fields.get(param), new Timestamp(time));
	}
	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		stm.setURL(parameterIndex, x);
	}
	public void set(String param, URL value) throws SQLException{
		for(Integer i: fields.get(param)) {
			stm.setURL(i, value);
		}
//		stm.setURL(fields.get(param), value);
	}
	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setUnicodeStream(parameterIndex, x, length);
	}
}

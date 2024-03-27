package simple.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

/**
 * A java.sql.PreparedStatement wrapper that lets you chain set* calls. Calls setNull() if the value is NULL
 *
 */
public class ChainablePreparedStatement extends ChainableStatement{
	protected final PreparedStatement pobj;
	public ChainablePreparedStatement(PreparedStatement wrap){
		super(wrap);
		pobj= wrap;
	}

	public ChainablePreparedStatement addBatch() throws SQLException{
		pobj.addBatch();
		return this;
	}

	public ChainablePreparedStatement clearParameters() throws SQLException{
		pobj.clearParameters();
		return this;
	}

	public boolean execute() throws SQLException{
		return pobj.execute();
	}

	public ResultSet executeQuery() throws SQLException{
		return pobj.executeQuery();
	}

	public int executeUpdate() throws SQLException{
		return pobj.executeUpdate();
	}

	public ResultSetMetaData getMetaData() throws SQLException{
		return pobj.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException{
		return pobj.getParameterMetaData();
	}

	public ChainablePreparedStatement setArray(int idx, Array value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.ARRAY);
		}else{
			pobj.setArray(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setAsciiStream(int idx, InputStream value) throws SQLException{
		pobj.setAsciiStream(idx, value);
		return this;
	}

	public ChainablePreparedStatement setAsciiStream(int idx, InputStream value, int length) throws SQLException{
		pobj.setAsciiStream(idx, value, length);
		return this;
	}

	public ChainablePreparedStatement setAsciiStream(int idx, InputStream value, long length) throws SQLException{
		pobj.setAsciiStream(idx, value, length);
		return this;
	}

	public ChainablePreparedStatement setBigDecimal(int idx, BigDecimal value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.DECIMAL);
		}else{
			pobj.setBigDecimal(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setBinaryStream(int idx, InputStream value) throws SQLException{
		pobj.setBinaryStream(idx, value);
		return this;
	}

	public ChainablePreparedStatement setBinaryStream(int idx, InputStream value, int length) throws SQLException{
		pobj.setBinaryStream(idx, value, length);
		return this;
	}

	public ChainablePreparedStatement setBinaryStream(int idx, InputStream value, long length) throws SQLException{
		pobj.setBinaryStream(idx, value, length);
		return this;
	}

	public ChainablePreparedStatement setBlob(int idx, Blob value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.BLOB);
		}else{
			pobj.setBlob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setBlob(int idx, InputStream value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.BLOB);
		}else{
			pobj.setBlob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setBlob(int idx, InputStream value, long length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.BLOB);
		}else{
			pobj.setBlob(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setBoolean(int idx, Boolean value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.BOOLEAN);
		}else{
			pobj.setBoolean(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setByte(int idx, Byte value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.TINYINT);
		}else{
			pobj.setByte(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setBytes(int idx, byte[] value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.VARBINARY);
		}else{
			pobj.setBytes(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setCharacterStream(int idx, Reader value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.LONGVARCHAR);
		}else{
			pobj.setCharacterStream(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setCharacterStream(int idx, Reader value, int length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.LONGVARCHAR);
		}else{
			pobj.setCharacterStream(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setCharacterStream(int idx, Reader value, long length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.LONGVARCHAR);
		}else{
			pobj.setCharacterStream(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setClob(int idx, Clob value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.CLOB);
		}else{
			pobj.setClob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setClob(int idx, Reader value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.CLOB);
		}else{
			pobj.setClob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setClob(int idx, Reader value, long length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.CLOB);
		}else{
			pobj.setClob(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setDate(int idx, Date value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.DATE);
		}else{
			pobj.setDate(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setDate(int idx, Date value, Calendar cal) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.DATE);
		}else{
			pobj.setDate(idx, value, cal);
		}
		return this;
	}

	public ChainablePreparedStatement setDouble(int idx, Double value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.DOUBLE);
		}else{
			pobj.setDouble(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setFloat(int idx, Float value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.REAL);
		}else{
			pobj.setFloat(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setInt(int idx, Integer value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.INTEGER);
		}else{
			pobj.setInt(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setLong(int idx, Long value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.BIGINT);
		}else{
			pobj.setLong(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setNCharacterStream(int idx, Reader value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NCLOB);
		}else{
			pobj.setNCharacterStream(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setNCharacterStream(int idx, Reader value, long length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NCLOB);
		}else{
			pobj.setNCharacterStream(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setNClob(int idx, NClob value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NCLOB);
		}else{
			pobj.setNClob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setNClob(int idx, Reader value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NCLOB);
		}else{
			pobj.setNClob(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setNClob(int idx, Reader value, long length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NCLOB);
		}else{
			pobj.setNClob(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setNString(int idx, String value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.NVARCHAR);
		}else{
			pobj.setNString(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setNull(int idx, int value) throws SQLException{
		pobj.setNull(idx, value);
		return this;
	}

	public ChainablePreparedStatement setNull(int idx, int value, String typeName) throws SQLException{
		pobj.setNull(idx, value, typeName);
		return this;
	}

	public ChainablePreparedStatement setObject(int idx, Object value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.JAVA_OBJECT);
		}else{
			pobj.setObject(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setObject(int idx, Object value, int length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.JAVA_OBJECT);
		}else{
			pobj.setObject(idx, value, length);
		}
		return this;
	}

	public ChainablePreparedStatement setObject(int idx, Object value, int targetSqlType, int scaleOrLength) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, targetSqlType);
		}else{
			pobj.setObject(idx, value, targetSqlType, scaleOrLength);
		}
		return this;
	}

	public ChainablePreparedStatement setRef(int idx, Ref value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.REF);
		}else{
			pobj.setRef(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setRowId(int idx, RowId value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.ROWID);
		}else{
			pobj.setRowId(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setSQLXML(int idx, SQLXML value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.SQLXML);
		}else{
			pobj.setSQLXML(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setShort(int idx, Short value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.SMALLINT);
		}else{
			pobj.setShort(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setString(int idx, String value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.VARCHAR);
		}else{
			pobj.setString(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setTime(int idx, Time value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.TIME);
		}else{
			pobj.setTime(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setTime(int idx, Time value, Calendar cal) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.TIME);
		}else{
			pobj.setTime(idx, value, cal);
		}
		return this;
	}

	public ChainablePreparedStatement setTimestamp(int idx, Timestamp value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.TIMESTAMP);
		}else{
			pobj.setTimestamp(idx, value);
		}
		return this;
	}

	public ChainablePreparedStatement setTimestamp(int idx, Timestamp value, Calendar arg2) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.TIMESTAMP);
		}else{
			pobj.setTimestamp(idx, value, arg2);
		}
		return this;
	}

	public ChainablePreparedStatement setURL(int idx, URL value) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.DATALINK);
		}else{
			pobj.setURL(idx, value);
		}
		return this;
	}

	@Deprecated
	public ChainablePreparedStatement setUnicodeStream(int idx, InputStream value, int length) throws SQLException{
		if(value == null) {
			pobj.setNull(idx, Types.LONGVARCHAR);
		}else{
			pobj.setUnicodeStream(idx, value, length);
		}
		return this;
	}

}

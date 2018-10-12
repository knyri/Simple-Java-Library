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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import simple.collections.ImmutableSet;

public class TableModel implements AutoCloseable{
	protected final String table;
	protected final Set<String> pkey;
	protected final Map<String, Integer> columns;
	protected final Map<String, String> distinctTypeMap= new HashMap<String, String>();
	protected Connection db= null;
	protected final Map<String, Object> values= new HashMap<String, Object>();
	protected ResultSet result= null;
	protected PreparedStatement stm= null;
	public TableModel(String tableName, Map<String, Integer> columns, String[] primaryKey){
		table= tableName;
		this.columns= columns;

		pkey= new ImmutableSet<String>(primaryKey);
	}
	public TableModel(String tableName, Map<String, Integer> columns, String[] primaryKey, Connection con){
		this(tableName, columns, primaryKey);
		setConnection(con);
	}
	/**
	 * Adds an explicit cast
	 * @param col
	 * @param typeName
	 */
	protected void addDistinctType(String col, String typeName){
		distinctTypeMap.put(col, typeName);
	}
	public void setConnection(Connection con){
		db= con;
	}
	public TableModel set(String col, Object val){
		values.put(col, val);
		return this;
	}
	public Object get(String col) throws SQLException{
		return values.get(col);
	}
	public Object get(String col, Object def){
		return values.getOrDefault(col, def);
	}
	public boolean loadNext() throws Exception{
		if(result.next()){
			for(String col: columns.keySet()){
				set(col, result.getObject(col));
			}
			afterLoadNext();
			return true;
		}
		return false;
	}
	public void reset() throws SQLException{
		close();
		values.clear();
	}
	@Override
	public void close() throws SQLException{
		if(result != null){
			result.close();
			result= null;
		}
		if(stm != null){
			stm.close();
			stm= null;
		}
	}
	public void setValues(ResultSet src) throws SQLException{
		values.clear();
		ResultSetMetaData md= src.getMetaData();
		String colName;
		for(int i= 1, end= md.getColumnCount() + 1; i < end; i++){
			// prevent extra columns from being added
			colName= md.getColumnName(i);
			if(columns.containsKey(colName)){
				values.put(colName, src.getObject(i));
			}
		}
	}
	protected void beforeInsert() throws Exception{}
	protected void afterInsert() throws Exception{}
	protected void beforeUpdate() throws Exception{}
	protected void afterUpdate() throws Exception{}
	protected void beforeDelete() throws Exception{}
	protected void afterDelete() throws Exception{}
	protected void beforeLoad() throws Exception{}
	protected void afterLoad() throws Exception{}
	protected void afterLoadNext() throws Exception{}
	public void insert() throws Exception{
		close();
		beforeInsert();
		StringBuilder insert= new StringBuilder();
		StringBuilder valuesStm= new StringBuilder();
		Set<String> columns= values.keySet();
		String type;
		insert.append("INSERT INTO ").append(table).append(" (");
		for(String col: columns){
			insert.append(col).append(',');
			type= distinctTypeMap.get(col);
			if(type != null){
				valuesStm.append("CAST(? AS ").append(type).append("),");
			}else{
				valuesStm.append("?,");
			}
		}
		insert.setCharAt(insert.length() - 1, ')');
		insert.append(" VALUES (");
//		insert.append(do_str.repeat("?,", columns.size()));
		insert.append(valuesStm);
		insert.setCharAt(insert.length() - 1, ')');

		stm= db.prepareStatement(insert.toString());
		int index= 1;
		for(String col: columns){
			set(col, index++, values.get(col));
		}

		stm.execute();
		afterInsert();
		close();
	}
	protected boolean isPrimaryKeySet(){
		for(String key: pkey){
			if(!values.containsKey(key)){
				return false;
			}
		}
		return true;
	}

	public int update() throws Exception{
		if(result != null){
			values.clear();
			// grab the current primary key
			for(String col: pkey){
				set(col, result.getObject(col));
			}
		}
		close();
		beforeUpdate();
		StringBuilder update= new StringBuilder();
		update.append("UPDATE ").append(table).append(" SET ");
		Set<String> columns= values.keySet();
		String type;
		for(String col: columns){
			type= distinctTypeMap.get(col);
			update.append(col);
			if(type != null){
				update.append("=CAST(? AS ").append(type).append("),");
			}else{
				update.append("=?,");
			}
		}
		update.setLength(update.length() - 1);

		boolean addPKey= isPrimaryKeySet();

		if(addPKey){
			update.append(" WHERE ");
			for(String col: pkey){
				type= distinctTypeMap.get(col);
				update.append(col);
				if(type != null){
					update.append("=CAST(? AS ").append(type).append(')');
				}else{
					update.append("=?");
				}
				update.append(" AND ");
			}
			update.setLength(update.length() - 5);
		}


		stm= db.prepareStatement(update.toString());
		int index= 1;
		for(String col: columns){
			set(col, index++, values.get(col));
		}
		if(addPKey){
			for(String key: pkey){
				set(key, index++, values.get(key));
			}
		}
		int result= stm.executeUpdate();
		afterUpdate();
		return result;
	}

	public void delete() throws Exception{
		close();
		beforeDelete();
		StringBuilder delete= new StringBuilder();
		delete.append("DELETE FROM ").append(table).append(" WHERE ");

		Set<String> columns= isPrimaryKeySet() ? pkey : values.keySet();
		String type;
		for(String col: columns){
			type= distinctTypeMap.get(col);
			delete.append(col);
			if(type != null){
				delete.append("=CAST(? AS ").append(type).append(')');
			}else{
				delete.append("=?");
			}
			delete.append(" AND ");
		}
		delete.setLength(delete.length() - 5);

		stm= db.prepareStatement(delete.toString());
		int index= 1;
		for(String col: columns){
			set(col, index++, values.get(col));
		}

		stm.execute();

		afterDelete();
	}
	public void find(String... getColumns) throws Exception {
		close();
		beforeLoad();
		StringBuffer load= new StringBuffer();
		load.append("SELECT ");

		Set<String> columns= isPrimaryKeySet() ? pkey : values.keySet();
		if(getColumns == null || getColumns.length == 0){
			for(String col: this.columns.keySet()){
				load.append(col).append(',');
			}
		}else{
			for(String col: getColumns){
				load.append(col).append(',');
			}
		}
		load.setLength(load.length() - 1);

		load.append(" FROM ").append(table);

		if(!columns.isEmpty()){
			load.append(" WHERE ");
			String type;
			for(String col: columns){
				type= distinctTypeMap.get(col);
				load.append(col);
				if(type != null){
					load.append("=CAST(? AS ").append(type).append(')');
				}else{
					load.append("=?");
				}
				load.append(" AND ");
			}
			load.setLength(load.length() - 5);
		}

		stm= db.prepareStatement(load.toString());
		int index= 1;
		for(String col: columns){
			set(col, index++, values.get(col));
		}

		result= stm.executeQuery();

		afterLoad();
	}
	public void find() throws Exception{
		find((String[])null);
	}

	public boolean exists() throws SQLException{
		StringBuilder query= new StringBuilder("SELECT DISTINCT 1 FROM ");
		query.append(table).append(" WHERE ");
		Set<String> columns= isPrimaryKeySet() ? pkey : values.keySet();
		String type;
		for(String col: columns){
			type= distinctTypeMap.get(col);
			query.append(col);
			if(type != null){
				query.append("=CAST(? AS ").append(type).append(')');
			}else{
				query.append("=?");
			}
			query.append(" AND ");
		}
		query.setLength(query.length() - 5);
		try(PreparedStatement stm= db.prepareStatement(query.toString())){
			int index= 1;
			for(String col: columns){
				set(col, index++, values.get(col));
			}
			try(ResultSet rs= stm.executeQuery()){
				return rs.next();
			}
		}
	}

	/**
	 * Grunt work of determining the param type and calling the appropriate set functions
	 * @param col
	 * @param param
	 * @param value
	 * @throws SQLException
	 */
	private void set(String col, int param, Object value) throws SQLException{
		Integer type= columns.get(col);
		if(type != null){
			if(value == null){
				stm.setNull(param, type);
			}else{
				switch(type){

				case Types.ARRAY:
					stm.setArray(param, (Array)value);
				break;

				case Types.BIGINT:
					if(value instanceof Number){
						stm.setLong(param, ((Number)value).longValue());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.CHAR:
				case Types.LONGVARCHAR:
				case Types.VARCHAR:
					if(value instanceof CharSequence){
						stm.setString(param, value.toString());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.DECIMAL:
				case Types.DOUBLE:
					if(value instanceof Number){
						stm.setDouble(param, ((Number)value).doubleValue());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.FLOAT:
				case Types.INTEGER:
					if(value instanceof Number){
						stm.setLong(param, ((Number)value).intValue());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.NCHAR:
				case Types.NVARCHAR:
				case Types.LONGNVARCHAR:
					if(value instanceof CharSequence){
						stm.setNString(param, value.toString());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.NUMERIC:
				case Types.REAL:
					if(value instanceof Number){
						stm.setDouble(param, ((Number)value).doubleValue());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.SMALLINT:
					if(value instanceof Number){
						stm.setShort(param, ((Number)value).shortValue());
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.TIME:
				case Types.TIME_WITH_TIMEZONE:
					if(value instanceof Time){
						stm.setTime(param, (Time)value);
					}else
					if(value instanceof Instant){
						stm.setTime(param, new Time(((Instant)value).toEpochMilli()));
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.TIMESTAMP:
				case Types.TIMESTAMP_WITH_TIMEZONE:
					if(value instanceof Timestamp){
						stm.setTimestamp(param, (Timestamp)value);
					}else
					if(value instanceof Instant){
						stm.setTimestamp(param, new Timestamp(((Instant)value).toEpochMilli()));
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.DATE:
					if(value instanceof Date){
						stm.setDate(param, (Date)value);
					}else
					if(value instanceof Instant){
						stm.setDate(param, new Date(((Instant)value).toEpochMilli()));
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.BLOB:
					if(value instanceof Blob){
						stm.setBlob(param, (Blob)value);
					}else
					if(value instanceof InputStream){
						stm.setBlob(param, (InputStream)value);
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.CLOB:
					if(value instanceof Clob){
						stm.setClob(param, (Clob)value);
					}else
					if(value instanceof Reader){
						stm.setClob(param, (Reader)value);
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.NCLOB:
					if(value instanceof NClob){
						stm.setNClob(param, (NClob)value);
					}else
					if(value instanceof Reader){
						stm.setNClob(param, (Reader)value);
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.BOOLEAN:
					if(value instanceof Boolean){
						stm.setBoolean(param, (Boolean)value);
					}else{
						stm.setObject(param, value, type);
					}
				break;

				case Types.DATALINK:
				case Types.LONGVARBINARY:
				case Types.NULL:
				case Types.BINARY:
				case Types.BIT:
				default:
					stm.setObject(param, value, type);
				}
			}
		}else{
			if(value == null){
				stm.setNull(param, Types.NULL);
			}else{
				if(value instanceof String){
					stm.setString(param, (String)value);
				}else
				if(value instanceof CharSequence){
					stm.setString(param, value.toString());
				}else
				if(value instanceof Integer){
					stm.setInt(param, ((Integer)value).intValue());
				}else
				if(value instanceof Double){
					stm.setDouble(param, ((Double)value).doubleValue());
				}else
				if(value instanceof Float){
					stm.setFloat(param, ((Float)value).floatValue());
				}else
				if(value instanceof Short){
					stm.setShort(param, ((Short)value).shortValue());
				}else
				if(value instanceof Byte){
					stm.setByte(param, ((Byte)value).byteValue());
				}else
				if(value instanceof Date){
					stm.setDate(param, (Date)value);
				}else
				if(value instanceof Time){
					stm.setTime(param, (Time)value);
				}else
				if(value instanceof Timestamp){
					stm.setTimestamp(param, (Timestamp)value);
				}else
				if(value instanceof Clob){
					stm.setClob(param, (Clob)value);
				}else
				if(value instanceof Blob){
					stm.setBlob(param, (Blob)value);
				}else
				if(value instanceof NClob){
					stm.setNClob(param, (NClob)value);
				}else
				if(value instanceof BigDecimal){
					stm.setBigDecimal(param, (BigDecimal)value);
				}else
				if(value instanceof URL){
					stm.setURL(param, (URL)value);
				}else
				if(value instanceof Array){
					stm.setArray(param, (Array)value);
				}else
				if(value instanceof Ref){
					stm.setRef(param, (Ref)value);
				}else
				if(value instanceof RowId){
					stm.setRowId(param, (RowId)value);
				}else
				if(value instanceof SQLXML){
					stm.setSQLXML(param, (SQLXML)value);
				}else
				if(value instanceof InputStream){
					stm.setBlob(param, (InputStream)value);
				}else
				if(value instanceof Reader){
					stm.setClob(param, (Reader)value);
				}else{
					stm.setObject(param, value);
				}
			}
		}
	}
	public PreparedStatement getLastStatement(){
		return stm;
	}
}

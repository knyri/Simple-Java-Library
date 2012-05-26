package simple.sql;

import java.util.Vector;
import javax.swing.table.*;

/** Table model for working with SQL.
 * <br>Created: 2004
 * @author Kenneth Pierce
 */
public class SQLTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<Vector<Object>> values;
	private Vector<String> colNames;
	private boolean[][] changed;
	/**
	 * @param values
	 * @param colNames
	 */
	public SQLTableModel(Vector<Vector<Object>> values, Vector<String> colNames) {
		this.values = values;
		this.colNames = colNames;
	}
	public String getColumnName(int col)	{	return colNames.elementAt(col);	}
	public Class<?> getColumnClass(int col)	{
		Vector<Object> vTmp = values.elementAt(0);
		if (vTmp.elementAt(col)==null) {	return Object.class;	}
		return vTmp.elementAt(col).getClass();
	}
	public int getColumnCount()				{
		if (getRowCount()==0)
			return 0;
		else
			return values.elementAt(0).size();
	}
	public int getRowCount()					{	return values.size();	}
	/**
	 * @return Always returns true.
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col)	{	return true;	}
	public Object getValueAt(int row, int col)		{	return values.elementAt(row).elementAt(col);	}
	public void setValueAt(Object value, int row, int col)	{
		values.elementAt(row).setElementAt((String)value,col);
		changed[row][col] = true;
	}
}
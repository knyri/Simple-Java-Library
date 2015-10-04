package simple.sql;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/** Table model for working with SQL.
 * <br>Created: 2004
 * @author Kenneth Pierce
 */
public class SQLTableModel extends AbstractTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<List<Object>> values;
	private List<String> colNames;
	private boolean[][] changed;
	/**
	 * @param values values
	 * @param colNames column names
	 */
	public SQLTableModel(List<List<Object>> values, List<String> colNames) {
		this.values = values;
		this.colNames = colNames;
	}
	@Override
	public String getColumnName(int col)	{	return colNames.get(col);	}
	@Override
	public Class<?> getColumnClass(int col)	{
		List<Object> vTmp = values.get(0);
		if (vTmp.get(col)==null) {	return Object.class;	}
		return vTmp.get(col).getClass();
	}
	@Override
	public int getColumnCount()				{
		if (getRowCount()==0)
			return 0;
		else
			return values.get(0).size();
	}
	@Override
	public int getRowCount()					{	return values.size();	}
	/**
	 * @return Always returns true.
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int col)	{	return true;	}
	@Override
	public Object getValueAt(int row, int col)		{	return values.get(row).get(col);	}
	@Override
	public void setValueAt(Object value, int row, int col)	{
		values.get(row).set(col,value);
		changed[row][col] = true;
	}
}
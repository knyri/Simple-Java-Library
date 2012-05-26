package simple.gui;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Simplifies the use of GridBagLayout for everyday needs.
 * <br>Created: 2006
 * @author Kenneth Pierce
 * @see java.awt.GridBagLayout
 * @see java.awt.GridBagConstraints
 */
public class GridBagPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Convenience reference to the GridBagConstraints this
	 * object uses.
	 */
	public final GridBagConstraints con = new GridBagConstraints();
	public static final int FILL_BOTH = GridBagConstraints.BOTH,
	FILL_VERTICAL = GridBagConstraints.VERTICAL,
	FILL_HORIZONTAL = GridBagConstraints.HORIZONTAL,
	FILL_NONE = GridBagConstraints.NONE,
	ALIGN_CENTER = GridBagConstraints.CENTER,
	ALIGN_EAST = GridBagConstraints.EAST,
	ALIGN_WEST = GridBagConstraints.WEST,
	ALIGN_NORTH = GridBagConstraints.NORTH,
	ALIGN_SOUTH = GridBagConstraints.SOUTH,
	ALIGN_NORTHEAST = GridBagConstraints.NORTHEAST,
	ALIGN_NORTHWEST = GridBagConstraints.NORTHWEST,
	ALIGN_SOUTHEAST = GridBagConstraints.SOUTHEAST,
	ALIGN_SOUTHWEST = GridBagConstraints.SOUTHWEST,
	WIDTH_REMAINDER = GridBagConstraints.REMAINDER,
	WIDTH_RELATIVE = GridBagConstraints.RELATIVE;
	
	public GridBagPanel() {
		super(new GridBagLayout());
	}
	/**
	 * Sets the alignment of the component in its cell.<br>
	 * Valid values are:<br>
	 * CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH,
	 * SOUTHWEST, WEST, and NORTHWEST.<br>
	 * PAGE_START, PAGE_END, LINE_START, LINE_END, FIRST_LINE_START,
	 * FIRST_LINE_END, LAST_LINE_START and LAST_LINE_END.<br>
	 * The default value is CENTER.
	 * @param position
	 */
	public void setAnchorPosition(int position) {
		con.anchor = position;
	}
	/**
	 * Sets the method used to resize the component to its cell.<br>
	 * Valid values are:<br>
	 * NONE: Do not resize the component.<br>
	 * HORIZONTAL: Make the component wide enough to fill its display
	 * area horizontally, but do not change its height.<br>
	 * VERTICAL: Make the component tall enough to fill its display
	 * area vertically, but do not change its width.<br>
	 * BOTH: Make the component fill its display area entirely.<br>
	 * The default value is NONE 
	 * @param method
	 */
	public void setFillMethod(int method) {
		con.fill = method;
	}
	/**
	 * Adds c to the specified cell at col, row.<br>
	 * Sets the current column and row to col and row repsectively.
	 * @param c
	 * @param col
	 * @param row
	 */
	public void add(Component c, int col, int row) {
		con.gridx = col;
		con.gridy = row;
		add(c, con);
	}
	/**
	 * Adds c to the current row, one column from the current.<br>
	 * Increments column position by one.
	 * @param c
	 */
	public void addToRow(Component c) {
		con.gridx++;
		add(c, con);
	}
	/**
	 * Adds c to the current column, one row down.<br>
	 * Increments row position by one.
	 * @param c
	 */
	public void addToColumn(Component c) {
		con.gridy++;
		add(c, con);
	}
	/**
	 * Returns the current row.
	 * @return The current row.
	 */
	public int getRow() {
		return con.gridy;
	}
	/**
	 * Returns the current column.
	 * @return The current column.
	 */
	public int getColumn() {
		return con.gridx;
	}
}

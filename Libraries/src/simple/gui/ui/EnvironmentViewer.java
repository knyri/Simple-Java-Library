package simple.gui.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.util.*;
import simple.util.do_str;

/** Simple JPanel that shows the running environment.
 * <hr>
 * Requires {@link simple.util.do_str}
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class EnvironmentViewer extends JPanel {
	private static final long serialVersionUID = 1264701375898358367L;
	public EnvironmentViewer(Map<String, String> env, Properties props) {
		setLayout(new BorderLayout());
		JTabbedPane main = new JTabbedPane();
		JPanel pTmp = new JPanel(new BorderLayout());
		pTmp.add(new JScrollPane(new JTable(new DataModel(env))));
		main.addTab("Environment", pTmp);
		pTmp = new JPanel(new BorderLayout());
		pTmp.add(new JScrollPane(new JTable(new DataModel(props))));
		main.addTab("Properties", pTmp);
		add(main);
	}
	//============= The data model for out table ===========
	private static final class DataModel extends AbstractTableModel {
		private static final long serialVersionUID = 4182721113153773335L;
		Vector<String> attr = new Vector<String>();
		Vector<String> valu = new Vector<String>();
		public DataModel(Map<String, String> vals) {
			for(Map.Entry<String, String> cur : vals.entrySet()) {
				addElement(cur.getKey(), cur.getValue());
			}
			sort();
		}
		public DataModel(Properties vals) {
			for(Map.Entry<Object, Object> cur : vals.entrySet()) {
				addElement((String)cur.getKey(), (String)cur.getValue());
			}
			sort();
		}
		public int getRowCount() {
			return attr.size();
		}
		public int getColumnCount() {
			return 2;
		}
		public String getValueAt(int row, int column) {
			String val = null;
			switch (column){
			case 0:
				val = attr.elementAt(row);
				break;
			case 1:
				val = valu.elementAt(row);
			}
			return val;
		}
		public Class<?> getColumnClass(int col) {
			return String.class;
		}
		public String getColumnName(int col) {
			String name = null;
			switch (col) {
			case 0:
				name = "Attribute";
				break;
			case 1:
				name = "Value";
			}
			return name;
		}
		//==== Sorts Table From A to Z by the Attributes =====
		public synchronized void sort() {
			String tmp = null;
			boolean change = true;
			while (change) {
				change = false;
				for(int i = 0; i<attr.size()-1; i++) {
					if (do_str.compareIgnoreCase(attr.elementAt(i), attr.elementAt(i+1))==-1) {
						tmp = attr.elementAt(i);
						attr.set(i, attr.get(i+1));
						attr.set(i+1, tmp);
						
						tmp = valu.elementAt(i);
						valu.set(i, valu.get(i+1));
						valu.set(i+1, tmp);
						change = true;
					}
				}
			}
			fireTableRowsUpdated(0, attr.size()-1);
		}
		public void addElement(String attribute, String value) {
			attr.addElement(attribute);
			valu.addElement(value);
		}
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Java Environment Viewer");
		frame.setSize(300, 500);
		frame.getContentPane().add(new EnvironmentViewer(System.getenv(), System.getProperties()));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

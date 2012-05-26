package simple.sql;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import simple.gui.container.ButtonPanel;
import simple.gui.container.ScrollableJPanel;
import simple.gui.event.ButtonPanelEvent;
import simple.gui.event.ButtonPanelListener;
import simple.gui.factory.SJPanel;
import simple.gui.factory.SwingFactory;
import simple.util.do_str;

/** Shows information about the database.<br>
 * Other dependents:
 * <br>simple.gui.layout.ButtonPanel
 * <br>simple.gui.event.ButtonPanelEvent
 * <br>simple.gui.event.ButtonPanelListener
 * <br>simple.javax.swing.ScrollableJPanel
 * <br>simple.gui.SJPanel
 * <br>simple.gui.SwingFactory
 * <br>simple.util.do_str
 * <br>Created: 2004
 * @author Kenneth Pierce
 */
public class SqlExplorer extends ScrollableJPanel implements ButtonPanelListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, Properties> drivers = new HashMap<String, Properties>();
	public SqlExplorer() {
		super(new BorderLayout());
		final JTabbedPane tabs = new JTabbedPane();
		JPanel pane;
		final Enumeration<Driver> en = DriverManager.getDrivers();
		Driver cur;
		Properties driver;
		while (en.hasMoreElements()) {
			pane = SJPanel.makeBoxLayoutPanelY(new ScrollableJPanel(true, false));
			cur = en.nextElement();
			driver = new Properties();
			System.out.println(cur.getClass().getCanonicalName());
			drivers.put(cur.getClass().getCanonicalName(), driver);
			try {
				for (final DriverPropertyInfo info : cur.getPropertyInfo("141.165.208.216", driver)) {
					pane.add(createJPanel(info, driver));
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			tabs.addTab(cur.getClass().getCanonicalName(), new JScrollPane(pane));
		}
		final ButtonPanel buttons = new ButtonPanel(ButtonPanel.RIGHT);
		buttons.add(SwingFactory.makeJButton("Save", "save"));
		buttons.addButtonListener(this);
		add(buttons, BorderLayout.SOUTH);
		add(tabs);
	}
	public static void main(final String[] args) throws SQLException, ClassNotFoundException {
		if (args!=null && args.length>0) {
			for (final String cur : args) {
				Class.forName(cur);
			}
		}
		//* AutoComment 1
		final JFrame frame = SwingFactory.makeDefaultJFrame("SQL Explorer");

		final SqlExplorer pane = new SqlExplorer();
		pane.setOpaque(true);
		frame.setContentPane(pane);

		final Dimension maxSize = frame.getToolkit().getScreenSize();
		Dimension curSize;
		frame.pack();
		curSize = frame.getSize();
		if (curSize.height > maxSize.height) {
			curSize.height = maxSize.height;
		}
		if (curSize.width > maxSize.width) {
			curSize.width = maxSize.width;
		}
		frame.setSize(curSize);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		/* AutoComment 1 */
		/* AutoComment 2
		Enumeration<Driver> en = DriverManager.getDrivers();
		Driver cur;
		while (en.hasMoreElements()) {
			cur = en.nextElement();
			System.out.println("-----"+cur.getClass().getCanonicalName()+"-----");
			for (DriverPropertyInfo info : cur.getPropertyInfo(null, null)) {
				System.out.println(info.name);
				System.out.println(info.description);
				System.out.println(info.value);
				System.out.println(info.required);
				if (info.choices != null) {
					for (String choice : info.choices) {
						System.out.println("\t"+choice);
					}
				}
				System.out.println("-----------");
			}
		}
		/* AutoComment 2 */
	}
	private static JPanel createJPanel(final DriverPropertyInfo info, final Properties props) {
		final JPanel main = SJPanel.makeBoxLayoutPanelY();
		final JTextArea desc = new JTextArea(info.description, 1, 80);
		desc.setWrapStyleWord(true);
		desc.setLineWrap(true);
		desc.setBackground(main.getBackground());
		desc.setEditable(false);
		main.setBorder(BorderFactory.createTitledBorder(info.name));
		//System.out.println(desc.getText());
		main.add(SJPanel.makeLabeledPanel(desc, "Description: "));
		main.add(SJPanel.makeLabeledPanel2(new JLabel(info.required?"Yes":"No"), "Required: "));
		if (info.choices == null) {
			main.add(new JTextField(info.value));
		} else {
			final JComboBox<String> choices = new JComboBox<String>(info.choices);
			choices.setSelectedItem(info.value);
			main.add(choices);
		}
		if (props.getProperty("names")==null) {
			props.setProperty("names", info.name);
		} else {
			props.setProperty("names", props.getProperty("names")+", "+info.name);
		}
		props.setProperty(info.name+".desc", info.description.replace('\n', ' ').replace('\r',' '));
		props.setProperty(info.name+".required", info.required?"yes":"no");
		props.setProperty(info.name+".value", info.value==null?"" : info.value);
		props.setProperty(info.name+".choices", info.choices==null?"":do_str.toString(info.choices, ","));
		return main;
	}
	public void actionPerformed(final ButtonPanelEvent e) {
		final String cmd = e.getSource().getActionCommand();
		if ("save".equals(cmd)) {
			synchronized (drivers) {
				for (final Map.Entry<String, Properties> cur : drivers.entrySet()) {
					try {
						cur.getValue().store(new FileOutputStream(new File(cur.getKey()+".conf")), null);
					} catch (final FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}

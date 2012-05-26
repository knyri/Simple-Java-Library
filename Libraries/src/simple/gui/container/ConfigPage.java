package simple.gui.container;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import simple.gui.factory.SJOptionPane;
import simple.gui.factory.SwingFactory;


/**This abstract class gives general implementation to create
 * "plug-in" configuration panels.
 * <hr>
 * depends on {@link simple.gui.factory.SJOptionPane}, {@link simple.gui.factory.SwingFactory}
 * <br>Created: 2008
 * @author Kenneth Pierce
 */
public abstract class ConfigPage extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * This HashMap is used to store the settings for easy
	 * retrieval.
	 */
	protected Properties props = new Properties();
	/**
	 * Added as a convenient reference to the 'main' JPanel. 
	 */
	protected final ConfigLayoutPane main = new ConfigLayoutPane(ConfigLayoutPane.LEFT);
	/**
	 * Added as a convenient reference to the 'bottom' JPanel. 
	 */
	protected final JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	protected final JButton save = SwingFactory.makeJButton("Save", "sv", new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			saveChanges();
		}
	});
	protected final JButton cancel = SwingFactory.makeJButton("Cancel", "cl", new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			undoChanges();
		}
	});
	/**
	 * Creates a JPanel using BorderLayout laid out like so:<br>
	 * <pre>
	 * .___________________________.
	 * ||       JPanel main       ||
	 * ||                         ||
	 * ||_________________________||
	 * ||       JPanel bottom     ||
	 * ||___________[save][cancel]||
	 * Save and Cancel are setup to call saveChanges() and
	 * undoChanges() repsectively.
	 * </pre>
	 * All other constructors defined in this class call this constructor.<br>
	 * Calls init() which should be overridden to set-up the components used
	 * for settings.
	 */
	public ConfigPage() {
		this((Properties)null);
	}
	/**
	 * Calls ConfigPanel() and setProperties(settings).
	 * @param settings
	 */
	public ConfigPage(Properties settings) {
		setupGUI(settings);
	}
	/**
	 * Calls ConfigPage() and sets this JPanel as double buffered.<br>
	 * Overrides {@link javax.swing.JPanel#JPanel(boolean)}.
	 * @param isDoubleBuffered
	 */
	protected ConfigPage(boolean isDoubleBuffered) {
		this();
		setDoubleBuffered(isDoubleBuffered);
	}
	/**
	 * Sets this JPanel as double buffered and sets the 'main' JPanel's
	 * LayoutManager to layout.<br>
	 * Overrides {@link javax.swing.JPanel#JPanel(java.awt.LayoutManager, boolean)}.
	 * @param layout
	 * @param isDoubleBuffered
	 */
	protected ConfigPage(LayoutManager layout, boolean isDoubleBuffered) {
		this(isDoubleBuffered);
		main.setLayout(layout);
	}
	/**
	 * Calls ConfigPage() and sets the 'main' JPanel's LayoutManager to
	 * layout.<br>
	 * Overrides {@link javax.swing.JPanel#JPanel(java.awt.LayoutManager)}.
	 * @param layout
	 */
	protected ConfigPage(LayoutManager layout) {
		this();
		main.setLayout(layout);
	}
	/**
	 * This should not be overridden!
	 * @param props
	 */
	protected void setupGUI(Properties props) {
		super.setLayout(new BorderLayout());
		super.add(main);
		super.add(bottom, BorderLayout.SOUTH);
		bottom.add(save);
		bottom.add(cancel);
		init(props);
	}
	public String getProperty(String name) {
		return props.getProperty(name);
	}
	public String getProperty(String name, String def) {
		return props.getProperty(name, def);
	}
	protected void setProperty(String name, String prop) {
		props.put(name, prop);
	}
	/**
	 * Using the names gotten from Names.getNames(), gets the values
	 * from props and puts them using setProperty.
	 * @param props HashMap containing the values.
	 */
	protected void setProperties(Properties props) {
		if (props == null) return;
		for (String name : getNames()) {
			setProperty(name, props.getProperty(name));
		}
		undoChanges();
	}
	/**
	 * Redirects component to 'main' JPanel instead of adding
	 * it to the JPanel's container.
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	public Component add(Component c) {
		main.add(c);
		return c;
	}
	public void setPanelLayout(LayoutManager m) {
		main.setLayout(m);
	}
	public Enumeration<?> getPropertyNames() {
		return props.propertyNames();
	}
	/**
	 * This method is called by the constructor and
	 * should be overriden by the subclass to setup the panel.
	 */
	protected abstract void init(Properties properties);
	/**
	 * This method is called when the save button is pressed.<br>
	 * It should save the current values using setProperty().<br>
	 * Can be used for validation.
	 */
	protected abstract void saveChanges();
	/**
	 * This method is called when the cancel button is pressed.<br>
	 * It should undo any changes using getProperty().
	 */
	protected abstract void undoChanges();
	
	/**
	 * Saves the current settings to an XML file.
	 * @param out OutputStream to save the settings to.
	 * @param comment
	 * @throws IOException
	 */
	public void saveProperties(OutputStream out, String comment) throws IOException {
		props.store(out, comment);
	}
	/**
	 * Loads settings from a XML file.
	 * @param in InputStream to read the settings from.
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public void loadProperties(InputStream in) throws InvalidPropertiesFormatException, IOException {
		props.load(in);
	}
	
	/**
	 * This should return a String array of the property names for
	 * this class.
	 * @return An array of Strings containing the names of the properties.
	 */
	protected abstract String[] getNames();
	
	/**
	 * Shows a dialog box with the set message. Focus is given to the
	 * target when the dialog box is closed.
	 * @param target Component to give focus to.
	 * @param message Message to display.
	 */
	protected void showError(JComponent target, String message) {
		SJOptionPane.showErrorMessage(target, message);
		if (target != null)
			target.requestFocusInWindow();
	}
}

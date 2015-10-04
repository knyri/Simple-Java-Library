package simple.gui.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import simple.util.App;

/** Simplification of JMenus.<br>
 * Predefines:
 * <ul>
 * <li>File
 * 	<ul>
 * 		<li>Open...</li>
 * 		<li>Save...</li>
 * 		<li>Quit</li>
 * 	</ul>
 * </li>
 * <li>Options</li>
 * <li>Tools</li>
 * <li>Help
 * 	<ul>
 * 		<li>Help</li>
 * 		<li>About</li>
 * 	</ul>
 * </li>
 * </ul>
 * <br>
 * Options can be combined by simply adding them together.
 * <br>Requires simple.util.App(simple.util.logging.*)
 * <br>Created: May 14, 2006
 * @author Kenneth Pierce
 */
public final class SMenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JMenu File = new JMenu("File");
	private final JMenuItem File_Open = new JMenuItem("Open...");
	private final JMenuItem File_Save = new JMenuItem("Save...");
	private final JMenuItem File_Quit = new JMenuItem("Quit");
	private final JMenu Options = new JMenu("Options");
	private final JMenu Tools = new JMenu("Tools");
	private final JMenu Help = new JMenu("Help");
	private final JMenuItem Help_Help = new JMenuItem("Help");
	private final JMenuItem Help_About = new JMenuItem("About");
	private Component AboutDialog = null;
	private Component HelpDialog = null;
	public static final int FILE = 1,
		FILE_OPEN = 2,
		FILE_SAVE = 4,
		FILE_QUIT = 8,
		HELP = 16,
		HELP_HELP = 32,
		HELP_ABOUT = 64,
		OPTION = 128,
		TOOLS = 256;
	private int options = 0;
	{
		File.setActionCommand("file");
		File.setMnemonic('f');
		File_Open.setActionCommand("open");
		File_Open.setMnemonic('o');
		File_Save.setActionCommand("save");
		File_Save.setMnemonic('s');
		File_Quit.setActionCommand("quit");
		File_Quit.setMnemonic('q');
		Options.setActionCommand("option");
		Options.setMnemonic('o');
		Tools.setActionCommand("tool");
		Tools.setMnemonic('t');
		Help_Help.setActionCommand("help");
		Help_Help.setMnemonic('h');
		Help_About.setActionCommand("about");
		Help_About.setMnemonic('a');
	}
	public SMenuBar() {
		super();
	}
	public SMenuBar(final int options) {
		this();
		this.options = options;
		if (App.isSet(options,FILE)||App.isSet(options,FILE_OPEN)||App.isSet(options,FILE_SAVE)||App.isSet(options,FILE_QUIT)) {
			if (App.isSet(options,FILE_OPEN)) {
				File.add(File_Open);
			}
			if (App.isSet(options,FILE_SAVE)) {
				File.add(File_Save);
			}
			if (App.isSet(options, FILE_QUIT)) {
				File.addSeparator();
				File.add(File_Quit);
			}
			this.add(File);
		}
		if (App.isSet(options,OPTION)) {
			this.add(Options);
		}
		if (App.isSet(options,TOOLS)) {
			this.add(Tools);
		}
		if (App.isSet(options,HELP)||App.isSet(options, HELP_HELP)||App.isSet(options,HELP_ABOUT)) {
			if (App.isSet(options, HELP_HELP))
				Help.add(Help_Help);
			if (App.isSet(options,HELP_ABOUT)) {
				Help.add(Help_About);
			}
			this.add(Help);
		}
	}
	public SMenuBar(final int options, final ActionListener ml) {
		this(options);
		addActionListener(options, ml);
	}
	public void addToMenu(final int menu, final JMenuItem me) {
		if (App.isSet(menu,FILE)||App.isSet(menu,FILE_OPEN)||App.isSet(menu,FILE_SAVE)) {
			addToFileMenu(me);
		} else if (App.isSet(menu,OPTION)) {
			Options.add(me);
		} else if (App.isSet(menu,TOOLS)) {
			Tools.add(me);
		} else if (App.isSet(menu,HELP)||App.isSet(menu,HELP_ABOUT)) {
			Help.add(me);
		}
	}
	public void addToFileMenu(final JMenuItem me) {
		if (App.isSet(options,FILE_QUIT)) {
			File.insert(me, File.getMenuComponentCount()-2);
		} else {
			File.add(me);
		}
	}
	public void addToOptionMenu(final JMenuItem me) {
		Options.add(me);
	}
	public void addToToolMenu(final JMenuItem me) {
		Tools.add(me);
	}
	public void addToHelpMenu(final JMenuItem me) {
		Help.add(me);
	}
	/**Adds a separator line to the menu(s).
	 * @param menu menu to add
	 */
	public void addSeparator(final int menu) {
		if (App.isSet(menu, FILE)) {
			File.addSeparator();
		}
		if (App.isSet(menu, HELP)) {
			Help.addSeparator();
		}
		if (App.isSet(menu, TOOLS)) {
			Tools.addSeparator();
		}
		if (App.isSet(menu, OPTION)) {
			Options.addSeparator();
		}
	}


	/** Returns the predefined menu.
	 * @param menu menu to get
	 * @return the predefined menu
	 */
	public final JMenu getSMenu(final int menu) {
		if (App.isSet(menu, FILE)) {
			return File;
		}
		if (App.isSet(menu,OPTION)) {
			return Options;
		}
		if (App.isSet(menu,TOOLS)) {
			return Tools;
		}
		if (App.isSet(menu, HELP)) {
			return Help;
		}
		return null;
	}

	public final JMenuItem getMenuItem(final int menu) {
		if (App.isSet(menu, FILE_OPEN)) {
			return File_Open;
		}
		if (App.isSet(menu, FILE_SAVE)) {
			return File_Save;
		}
		if (App.isSet(menu, FILE_QUIT)) {
			return File_Quit;
		}
		if (App.isSet(menu, HELP_ABOUT)) {
			return Help_About;
		}
		return super.getMenu(menu);
	}

	/**Returns the action command for the menu or menu item.
	 * @param menu command to get
	 * @return the action command for the menu
	 */
	public static final String getMenuCommand(final int menu) {
		if (App.isSet(menu, FILE)) {
			return "file";
		}
		if (App.isSet(menu, FILE_OPEN)) {
			return "open";
		}
		if (App.isSet(menu, FILE_SAVE)) {
			return "save";
		}
		if (App.isSet(menu, FILE_QUIT)) {
			return "quit";
		}
		if (App.isSet(menu,OPTION)) {
			return "option";
		}
		if (App.isSet(menu,TOOLS)) {
			return "tool";
		}
		if (App.isSet(menu, HELP_HELP)||App.isSet(menu, HELP)) {
			return "help";
		}
		if (App.isSet(menu, HELP_ABOUT)) {
			return "about";
		}
		return null;
	}
	/** Adds the action listener to the menu(s) and menu item(s).
	 * @param menu menu to add to
	 * @param ml listener to add
	 */
	public void addActionListener(final int menu, final ActionListener ml) {
		if (App.isSet(menu, FILE)) {
			File.addActionListener(ml);
		}
		if (App.isSet(menu, FILE_OPEN)) {
			File_Open.addActionListener(ml);
		}
		if (App.isSet(menu, FILE_SAVE)) {
			File_Save.addActionListener(ml);
		}
		if (App.isSet(menu, FILE_QUIT)) {
			File_Quit.addActionListener(ml);
		}
		if (App.isSet(menu,OPTION)) {
			Options.addActionListener(ml);
		}
		if (App.isSet(menu,TOOLS)) {
			Tools.addActionListener(ml);
		}
		if (App.isSet(menu, HELP_HELP)) {
			Help_Help.addActionListener(ml);
		}
		if (App.isSet(menu, HELP_ABOUT)) {
			Help_About.addActionListener(ml);
		}
	}
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("help"))
			showHelpDialog();
		else if (e.getActionCommand().equals("about"))
			showAboutDialog();
	}
	public void setAboutDialog(final Component dialog) {
		if (AboutDialog==null&&dialog!=null)
			Help_About.addActionListener(this);
		AboutDialog = dialog;
	}
	public void setHelpDialog(final Component dialog) {
		if (HelpDialog==null&&dialog!=null)
			Help_Help.addActionListener(this);
		HelpDialog = dialog;
	}
	public Component getAboutDialog() {
		return AboutDialog;
	}
	public Component getHelpDialog() {
		return HelpDialog;
	}
	public void showAboutDialog() {
		if (AboutDialog!=null)
			AboutDialog.setVisible(true);
	}
	public void showHelpDialog() {
		if (HelpDialog!=null)
			HelpDialog.setVisible(true);
	}
}

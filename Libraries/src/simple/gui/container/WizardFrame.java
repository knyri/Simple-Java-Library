package simple.gui.container;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simple.gui.factory.SJOptionPane;
import simple.gui.factory.SwingFactory;



/**
 * This is a JDialog frame that is used in combination with
 * {@link simple.gui.container.WizardPanel} to create a step-by-step
 * wizard.<br>
 * 
 * What the frame looks like:<br>
 * <pre>
 * ___________________________
 * ||                       ||
 * ||                       ||   &lt;&lt; main panel
 * ||                       ||
 * ||_______________________||
 * ||_____[&lt;&lt; Back][Next &gt;&gt;]||   &lt;&lt; bottom panel
 * </pre>
 * The WizardPanels decide if Back or Next is enabled.
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class WizardFrame extends JDialog implements ActionListener {
	private static final long serialVersionUID = 8397218610517217628L;
	public static final String PANEL_VALIDATED_CMD = "valid";
	public static final String PANEL_SHOWN_CMD = "show";
	public static final String WIZARD_FINISHED_CMD = "fin";
	private final JButton next = SwingFactory.makeJButton("Next >>", "nt", this);
	private final JButton back = SwingFactory.makeJButton("<< Back", "bk", this);
	private final JPanel content = new JPanel(new BorderLayout());
	private final CardLayout cl = new CardLayout();
	private final JPanel panels = new JPanel(cl);
	private int index = 0;
	private final Vector<WizardPanel> panelMap = new Vector<WizardPanel>();
	private final Vector<ActionListener> listeners = new Vector<ActionListener>();
	/**
	 * Creates a blank wizard with the next and back buttons displayed
	 * and a default size of 600x500.
	 * @param frame
	 * @param title
	 * @param modal If true, blocks until window is closed and disallows
	 * 		the parent frame from taking focus.
	 */
	public WizardFrame(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		content.setOpaque(true);
		setContentPane(content);
		JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bot.add(next);
		bot.add(back);
		back.setEnabled(false);
		next.setEnabled(true);
		content.add(panels);
		content.add(bot, BorderLayout.SOUTH);
		setSize(600, 500);
		setLocationRelativeTo(frame);
	}
	public void add(WizardPanel panel) {
		if (panel==null) return;
		panelMap.add(panel);
		panels.add(panel);
		panel.setWizardFrame(this);
	}
	public void addActionListener(ActionListener al) {
		listeners.add(al);
	}
	public void removeActionListener(ActionListener al) {
		listeners.remove(al);
	}
	private synchronized void fireActionEvent(ActionEvent ae) {
		for (ActionListener al : listeners) {
			al.actionPerformed(ae);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (!isReady()) return;
		String cmd = SwingFactory.getActionCommand(e);
		WizardPanel panel = panelMap.get(index);
		if ("nt".equals(cmd)) {
			if (panel.isPanelValid()) {
				cl.next(panels);
				fireActionEvent(new ActionEvent(panel,0,PANEL_VALIDATED_CMD));
				index++;
				if (index==panelMap.size()) {
					next.setText("Finish");
					next.setActionCommand("fh");
				}
				back.setEnabled(panelMap.get(index).canGoBack());
			} else {
				showError(panel);
			}
		} else if ("bk".equals(cmd)) {
			cl.previous(panels);
			index--;
			back.setEnabled(panelMap.get(index).canGoBack());
			if (next.getActionCommand().equals("fh")) {
				next.setActionCommand("nt");
				next.setText("Next >>");
			}
		} else if ("fh".equals(cmd)) {
			if (panel.isPanelValid()) {
				fireActionEvent(new ActionEvent(panelMap.get(index),0,PANEL_VALIDATED_CMD));
				fireActionEvent(new ActionEvent(panelMap.get(index),0,WIZARD_FINISHED_CMD));
			} else {
				showError(panel);
			}
		}
	}
	private void showError(WizardPanel wp) {
		SJOptionPane.showErrorMessage(wp, wp.getErrorCode());
	}
	public void showPanel(int index) {
		if (index<0 || index>=panelMap.size()) return;
		cl.show(panels, panelMap.get(index).getName());
		this.index = index;
	}
	public synchronized void reset() {
		for (WizardPanel cur : panelMap) {
			cur.reset();
		}
		cl.first(panels);
		index = 0;
		next.setText("Next >>");
		next.setActionCommand("nt");
		next.setEnabled(true);
		back.setEnabled(false);
	}
	public void reset(int index) {
		panelMap.get(index).reset();
	}
	public void setNextEnabled(boolean b) {
		next.setEnabled(b);
	}
	public void setBackEnabled(boolean b) {
		back.setEnabled(b);
	}
	public boolean isReady() {
		return (panelMap.size()>0);
	}
	public void setVisible(boolean b) {
		if (isReady())
			next.setEnabled(true);
		super.setVisible(b);
	}
}

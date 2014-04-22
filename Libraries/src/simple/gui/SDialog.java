package simple.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
/**An extension of JDialog that simplifies setup and layout by predefining it.<br>
 * As shown below, there is a top, left, center, right, and bottom panel.
 * The top and bottom panels flow components left to right.
 * The left, center, and right panel flow components top to bottom.
 * <pre>
 * ________________________________
 * || top panel                  ||
 * ||____________________________||
 * ||left | center panel | right ||
 * ||panel|              | panel ||
 * ||_____|______________|_______||
 * || bottom panel               ||
 * ||____________________________||
 * |______________________________|
 * </pre>
 * <br>Created: 2007
 * @author Kenneth Pierce
 * @version 1.0
 */
public class SDialog extends JDialog {

	private static final long serialVersionUID = 6842397187711866097L;
	protected final JPanel top;// = SJPanel.makeBoxLayoutPanelX();
	protected final JPanel left;// = SJPanel.makeBoxLayoutPanelY();
	protected final JPanel right;// = SJPanel.makeBoxLayoutPanelY();
	protected final JPanel bottom;// = SJPanel.makeBoxLayoutPanelX();
	protected final JPanel center;// = SJPanel.makeBoxLayoutPanelY();
	private volatile int response = 0;
	private final Object sync=new Object();
	public static final int YES_OPTION = 1,
		NO_OPTION = 2,
		CANCEL_OPTION = 4,
		OK_OPTION = 8,
		WINDOW_CLOSED_OPTION = -1;
	public static final int YES_BUTTON = YES_OPTION,
		YES_NO_BUTTON = YES_OPTION+NO_OPTION,
		YES_CANCEL_BUTTON = YES_OPTION+CANCEL_OPTION,
		OK_BUTTON = OK_OPTION,
		OK_CANCEL_BUTTON = OK_OPTION+CANCEL_OPTION,
		YES_NO_CANCEL_BUTTON = YES_OPTION+NO_OPTION+CANCEL_OPTION;
	/**
	 *
	 * @param frame Frame to center on.
	 * @param title
	 * @param modal Prevent loss of focus until closed.
	 */
	public SDialog (Frame frame, String title, boolean modal) {
		this(frame, title, modal, null, null, null, null, null);
	}
	/**
	 * @param frame Frame to center on.
	 * @param title
	 * @param modal Prevent loss of focus until closed.
	 * @param topm Layout manager for the top frame or null.
	 * @param leftm Layout manager for the left frame or null.
	 * @param rightm Layout manager for the right frame or null.
	 * @param bottomm Layout manager for the bottom frame or null.
	 * @param centerm Layout manager for the center frame or null.
	 */
	public SDialog (Frame frame, String title, boolean modal, LayoutManager topm, LayoutManager leftm, LayoutManager rightm, LayoutManager bottomm, LayoutManager centerm) {
		super(frame, title, modal);
		if (topm==null){
			top=new JPanel();
			top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		}else
			top=new JPanel(topm);
		if (leftm==null){
			left=new JPanel();
			left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		}else
			left = new JPanel(leftm);
		if (rightm == null){
			right=new JPanel();
			right.setLayout(new BoxLayout(right,BoxLayout.Y_AXIS));
		}else
			right = new JPanel(rightm);
		if (bottomm == null){
			bottom=new JPanel();
			bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		}else
			bottom = new JPanel(bottomm);
		if (centerm == null){
			center=new JPanel();
			center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		}else
			center = new JPanel(centerm);
		JPanel content = new JPanel(new BorderLayout());
		content.add(top, BorderLayout.NORTH);
		content.add(left, BorderLayout.WEST);
		content.add(center);
		content.add(right, BorderLayout.EAST);
		content.add(bottom, BorderLayout.SOUTH);
		this.setContentPane(content);
	}
	/**
	 * Add a component to the center Dialog.
	 * @param item Component to add.
	 */
	public void addCenter(JComponent item) {
		center.add(item);
	}
	public void addCenter(JComponent item, Object constraints) {
		center.add(item, constraints);
	}
	public void addLeft(JComponent item) {
		left.add(item);
	}
	public void addLeft(JComponent item, Object constraints) {
		left.add(item, constraints);
	}
	public void addRight(JComponent item) {
		right.add(item);
	}
	public void addRight(JComponent item, Object constraints) {
		right.add(item, constraints);
	}
	public void addTop(JComponent item) {
		top.add(item);
	}
	public void addTop(JComponent item, Object constraints) {
		top.add(item, constraints);
	}
	public void addBottom(JComponent item) {
		bottom.add(item);
	}
	public void addBottom(JComponent item, Object constraints) {
		bottom.add(item, constraints);
	}
	public void setButtons(int buttons) {
		if (isSet(YES_OPTION, buttons))
			addOption(YES_OPTION);
		if (isSet(OK_OPTION, buttons))
			addOption(OK_OPTION);
		if (isSet(NO_OPTION, buttons))
			addOption(NO_OPTION);
		if (isSet(CANCEL_OPTION, buttons))
			addOption(CANCEL_OPTION);
	}
	protected static boolean isSet(int option, int in) {
		return (option&in)==option;
	}
	protected void addOption(int option) {
		JButton tmp;
		switch(option) {
		case YES_OPTION:
			tmp = new JButton("Yes");
			tmp.setActionCommand("y");
			tmp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					synchronized(sync){
						response = YES_OPTION;
					}
					setVisible(false);
				}
			});
			break;
		case OK_OPTION:
			tmp = new JButton("Okay");
			tmp.setActionCommand("o");
			tmp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					synchronized(sync){
						response = OK_OPTION;
					}
					setVisible(false);
				}
			});
			break;
		case NO_OPTION:
			tmp = new JButton("No");
			tmp.setActionCommand("n");
			tmp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					synchronized(sync){
						response = NO_OPTION;
					}
					setVisible(false);
				}
			});
			break;
		case CANCEL_OPTION:
			tmp = new JButton("Cancel");
			tmp.setActionCommand("c");
			tmp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					synchronized(sync){
						response = CANCEL_OPTION;
					}
					setVisible(false);
				}
			});
			break;
			default:
				return;
		}
		bottom.add(tmp);
	}
	/**
	 * Centers DialogBox on owner or on the screen if owner == null.
	 */
	public void center() {
		super.setLocationRelativeTo(super.getOwner());
	}
	/**
	 * Temporarily sets this dialog as modal and displays it.
	 * @return The response
	 */
	public synchronized  int getResponse() {
		int resp;
		boolean modal;
		synchronized(sync){
			response=-1;
			center();
			modal=isModal();
		}
		setModal(true);
		setVisible(true);
		synchronized(sync){
			resp= response;
		}
		setModal(modal);
		return resp;
	}
	@Override
	public void setVisible(boolean b){
		if(!b){
			synchronized(sync){
				sync.notify();
			}
		}
		super.setVisible(b);
	}
}
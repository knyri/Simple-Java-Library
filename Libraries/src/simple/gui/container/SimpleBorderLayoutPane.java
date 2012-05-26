/**
 * 
 */
package simple.gui.container;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import simple.gui.factory.SJPanel;

/** A more natural border layout.<br>
 * The top and bottom lays out components left to right.<br>
 * The right, left and center lays out components top to bottom.
 * <br>Created: Aug 29, 2008
 * <br>depends on {@link simple.gui.factory.SJPanel}
 * @author Kenneth Pierce
 */
public class SimpleBorderLayoutPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JPanel left = SJPanel.makeBoxLayoutPanelY();
	private final JPanel right = SJPanel.makeBoxLayoutPanelY();
	private final JPanel center = SJPanel.makeBoxLayoutPanelY();
	private final JPanel bottom = SJPanel.makeBoxLayoutPanelX();
	private final JPanel top = SJPanel.makeBoxLayoutPanelX();
	public SimpleBorderLayoutPane() {
		super(new BorderLayout());
		add(top, BorderLayout.NORTH);
		add(left, BorderLayout.WEST);
		add(center);
		add(right, BorderLayout.EAST);
		add(bottom, BorderLayout.SOUTH);
	}
	public void addCenter(JComponent item) {
		center.add(item);
	}
	public void addLeft(JComponent item) {
		left.add(item);
	}
	public void addRight(JComponent item) {
		right.add(item);
	}
	public void addTop(JComponent item) {
		top.add(item);
	}
	public void addBottom(JComponent item) {
		bottom.add(item);
	}
}

package simple.gui.container;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * Allows a JPanel to be added to a JScrollPane.<br>
 * Found out soon after making this class that I had
 * goofed in my layout. While this class is no longer
 * necessary, it still does a slightly better job than
 * the default.
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public class ScrollableJPanel extends JPanel implements Scrollable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean autoSizeWidth = false;
	private boolean autoSizeHeight = false;
	public ScrollableJPanel() {
		super();
	}

	public ScrollableJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public ScrollableJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ScrollableJPanel(LayoutManager layout) {
		super(layout);
	}
	public ScrollableJPanel(boolean sizeWidthToView, boolean sizeHeightToView) {
		super();
		autoSizeWidth = sizeWidthToView;
		autoSizeHeight = sizeHeightToView;
	}
	public ScrollableJPanel(LayoutManager layout, boolean sizeWidthToView, boolean sizeHeightToView) {
		super(layout);
		autoSizeWidth = sizeWidthToView;
		autoSizeHeight = sizeHeightToView;
	}
	/**
	 * Returns the preferred size of this JPanel
	 * by calling this.getPreferredSize().
	 * 
	 * @return The preferred size of the JPanel.
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		int blockSize = 0;
		if (orientation == SwingConstants.HORIZONTAL) {
			blockSize = visibleRect.width;
		} else {
			blockSize = visibleRect.height;
		}
		direction = (int)(blockSize * .05);
		return direction + 1;//direction may be 0 if the float is < .5
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width;
        } else {
            return visibleRect.height;
        }
	}

	/**
	 * Returns false.<br>
	 * The ScrollPane will not resize the Panel's width to fit
	 * the visible Viewport.
	 * @return See superclass.
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return autoSizeWidth;
	}

	/**
	 * Returns false.<br>
	 * The ScrollPane will not resize the Panel's height to fit
	 * the visible Viewport.
	 * @return Returns false.
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return autoSizeHeight;
	}
	public void setScrollableTracksViewportWidth(boolean b) {
		autoSizeWidth = b;
	}
	public void setScrollableTracksViewportHeight(boolean b) {
		autoSizeHeight = b;
	}
	/**
	 * Enables or disables both auto sizing at once.
	 * @param b
	 */
	public void setScrollableTracksViewportSize(boolean b) {
		autoSizeWidth = b;
		autoSizeHeight = b;
	}

}

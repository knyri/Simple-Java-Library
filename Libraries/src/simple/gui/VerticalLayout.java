package simple.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Lays out components in a vertical column.
 *
 * @author Ken
 *
 */
public class VerticalLayout implements LayoutManager{
	private int vgap;
	private int minWidth=0,minHeight=0;
	private int preferredWidth=0,preferredHeight=0;
	private boolean sizeUnknown=true;
	public static final int LEFT=0,CENTER=1,RIGHT=2;
	private int alignment=0;

	/**
	 * Left alignment, vertical gap of 5 pixels.
	 */
	public VerticalLayout(){
		this(5,0);
	}

	/**
	 * Left alignment
	 * @param gap vertical gap in pixels
	 */
	public VerticalLayout(int gap){
		this(gap,0);
	}

	/**
	 * @param gap vertical gap in pixels
	 * @param alignment LEFT, CENTER, or RIGHT
	 */
	public VerticalLayout(int gap,int alignment){
		vgap=gap;
		this.alignment=alignment;
	}

	public void setAlignment(int align){
		if(align>-1 && align<3)
			this.alignment=align;
	}

	/* Required by LayoutManager. */
	@Override
	public void addLayoutComponent(String name,Component comp){
	}

	/* Required by LayoutManager. */
	@Override
	public void removeLayoutComponent(Component comp){
	}

	private void setSizes(Container parent){
		int nComps=parent.getComponentCount();
		Dimension d=null;

		// Reset preferred/minimum width and height.
		preferredWidth=0;
		preferredHeight=0;
		minWidth=0;
		minHeight=0;

		for(int i=0;i<nComps;i++){
			Component c=parent.getComponent(i);
			if(c.isVisible()){
				d=c.getPreferredSize();

				if(i>0)
					preferredHeight+=vgap;
				if(d.width>preferredWidth)
					preferredWidth=d.width;
				preferredHeight+=d.height;

				minWidth=Math.max(c.getMinimumSize().width,minWidth);
				minHeight=preferredHeight;
			}
		}
	}

	/* Required by LayoutManager. */
	@Override
	public Dimension preferredLayoutSize(Container parent){
		Dimension dim=new Dimension(0,0);

		setSizes(parent);

		// Always add the container's insets!
		Insets insets=parent.getInsets();
		dim.width=preferredWidth+insets.left+insets.right;
		dim.height=preferredHeight+insets.top+insets.bottom;

		sizeUnknown=false;

		return dim;
	}

	/* Required by LayoutManager. */
	@Override
	public Dimension minimumLayoutSize(Container parent){
		Dimension dim=new Dimension(0,0);

		// Always add the container's insets!
		Insets insets=parent.getInsets();
		dim.width=minWidth+insets.left+insets.right;
		dim.height=minHeight+insets.top+insets.bottom;

		sizeUnknown=false;

		return dim;
	}

	/* Required by LayoutManager. */
	/*
	 * This is called when the panel is first displayed, and every time its size
	 * changes. Note: You CAN'T assume preferredLayoutSize or minimumLayoutSize
	 * will be called -- in the case of applets, at least, they probably won't
	 * be.
	 */
	@Override
	public void layoutContainer(Container parent){
		Insets insets=parent.getInsets();
		int nComps=parent.getComponentCount(),
			y=insets.top,
			left=insets.left,
			right=parent.getWidth()-insets.right,
			center=parent.getWidth()/2+insets.left;

		// Go through the components' sizes, if neither
		// preferredLayoutSize nor minimumLayoutSize has
		// been called.
		if(sizeUnknown)
			setSizes(parent);

		for(int i=0;i<nComps;i++){
			Component c=parent.getComponent(i);
			if(c.isVisible()){
				Dimension d=c.getPreferredSize();

				/* If y is too large,
				if((y+d.height)>(parent.getHeight()-insets.bottom)){
					// do nothing.
					// Another choice would be to do what we do to x.
				}
				//*/

				// Set the component's size and position.
				switch(alignment){
				case LEFT:
					c.setBounds(left,y,d.width,d.height);
					break;
				case CENTER:
					c.setBounds(center-d.width/2,y,d.width,d.height);
					break;
				case RIGHT:
					c.setBounds(right-d.width,y,d.width,d.height);
					break;
				}

				y+=d.height+vgap;
			}
		}
	}

	@Override
	public String toString(){
		return getClass().getName()+"[vgap="+vgap+";alignment="+alignment+"]";
	}

}

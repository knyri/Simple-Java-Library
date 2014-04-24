package simple.gui;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Lays out components in a vertical column.
 * The max of the minimum and preferred height and width is used.
 * Alignments:
 * LEFT: left aligned. Will respect minimum and preferred sizes.
 * CENTER: centered. Will respect minimum and preferred sizes.
 * RIGHT: right aligned. Will respect minimum and preferred sizes.
 * FULLWIDTH: Takes up the full width. Respects minimum sizes.
 * 		More specifically, the largest minimum size.
 *
 * @author Ken
 *
 */
public class VerticalLayout implements LayoutManager{
	private int vgap;
	public static final int LEFT=0,CENTER=1,RIGHT=2,FULLWIDTH=3;
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
		this.alignment=align;
	}
	public void setVerticalGap(int gap){
		if(gap<0)
			vgap=0;
		else
			gap=vgap;
	}

	/* Required by LayoutManager. */
	@Override
	public void addLayoutComponent(String name,Component comp){}

	/* Required by LayoutManager. */
	@Override
	public void removeLayoutComponent(Component comp){}

	/* Required by LayoutManager. */
	@Override
	public Dimension preferredLayoutSize(Container parent){
		synchronized(parent.getTreeLock()){
			int nComps=parent.getComponentCount();
			Insets insets=parent.getInsets();
			Dimension dim=new Dimension(insets.left+insets.right,insets.top+insets.bottom),
					d;

			int nVisible=-1;

			for(int i=0;i<nComps;i++){
				Component c=parent.getComponent(i);
				if(c.isVisible()){
					nVisible++;
					d=c.getPreferredSize();

					if(d.width>dim.width)
						dim.width=d.width;
					dim.height+=d.height;
				}
			}
			if(nVisible>0)
				dim.height+=vgap*nVisible;
			return dim;
		}
	}

	/* Required by LayoutManager. */
	@Override
	public Dimension minimumLayoutSize(Container parent){
		synchronized(parent.getTreeLock()){
			synchronized(parent.getTreeLock()){
				int nComps=parent.getComponentCount();
				Insets insets=parent.getInsets();
				Dimension dim=new Dimension(insets.left+insets.right,insets.top+insets.bottom),
						d;

				int nVisible=-1;

				for(int i=0;i<nComps;i++){
					Component c=parent.getComponent(i);
					if(c.isVisible()){
						nVisible++;
						d=c.getMinimumSize();

						if(d.width>dim.width)
							dim.width=d.width;
						dim.height+=d.height;
					}
				}
				if(nVisible>0)
					dim.height+=vgap*nVisible;
				return dim;
			}
		}
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
		synchronized(parent.getTreeLock()){
			Insets insets=parent.getInsets();
			int nComps=parent.getComponentCount(),
				y=insets.top,
				left=insets.left,
				right=parent.getWidth(),
				center=parent.getWidth()/2+insets.left;
			Dimension pref,min,d=new Dimension(0,0);

			for(int i=0;i<nComps;i++){
				Component c=parent.getComponent(i);
				if(c.isVisible()){
					pref=c.getPreferredSize();
					min=c.getMinimumSize();
					d.width=Math.max(pref.width,min.width);
					d.height=Math.max(pref.height,min.height);

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
					case FULLWIDTH:
						c.setBounds(left,y,right,d.height);
						break;
						default:
							throw new AWTError("Unknown Alignment");
					}

					y+=d.height+vgap;
				}
			}
		}
	}

	@Override
	public String toString(){
		return getClass().getName()+"[vgap="+vgap+" alignment="+alignment+"]";
	}
}

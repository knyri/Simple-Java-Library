/**
 * 
 */
package simple.gui;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * <hr>
 * <br>Created: Dec 18, 2011
 * @author Kenneth Pierce
 */
public class AnonymousJComponent extends JComponent{
	
	private static final long serialVersionUID=1L;
	public AnonymousJComponent(){super();}
	public AnonymousJComponent(LayoutManager lm){super();setLayout(lm);}
	public AnonymousJComponent aadd(Component c){
		add(c);
		return this;
	}
	public AnonymousJComponent aadd(Component c, int idx){
		add(c,idx);
		return this;
	}
	public AnonymousJComponent aadd(Component c,Object con){
		add(c,con);
		return this;
	}
	public AnonymousJComponent aadd(Component c,Object con,int idx){
		add(c,con,idx);
		return this;
	}
	public AnonymousJComponent aadd(String name, Component c){
		add(name,c);
		return this;
	}
	public AnonymousJComponent asetBorder(Border b){
		setBorder(b);
		return this;
	}
}
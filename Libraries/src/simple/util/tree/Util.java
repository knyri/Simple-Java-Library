/**
 * 
 */
package simple.util.tree;

import java.io.PrintStream;
import java.util.Map.Entry;

import simple.util.do_str;

/**
 * <hr>
 * <br>Created: Dec 18, 2011
 * @author Kenneth Pierce
 */
public final class Util{
	private Util(){}
	public static void print(FullNode<?,?,?> tree,PrintStream out){
		out.println("+"+tree.getName());
		if(!tree.properties.isEmpty())
			for(Entry<?,?> entry:tree.properties.entrySet()){
				out.println("| ["+entry.getKey()+":"+entry.getValue());
			}
		if(tree.hasChild()){
			for(FullNode<?,?,?> child:tree.children){
				print(child,1,out);
			}
		}
	}
	private static void print(FullNode<?,?,?> tree,int indent,PrintStream out){
		String ind=do_str.repeat('|',indent);
		out.println(ind+"+"+tree.getName());
		if(!tree.properties.isEmpty())
			for(Entry<?,?> entry:tree.properties.entrySet()){
				out.println(ind+" ["+entry.getKey()+":"+entry.getValue());
			}
		if(tree.hasChild()){
			for(FullNode<?,?,?> child:tree.children){
				print(child,indent+1,out);
			}
		}
	}
}

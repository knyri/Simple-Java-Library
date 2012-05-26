package simple.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Stack;

import simple.io.FileUtil;
import simple.io.StringWriterExt;

/**
 * Slightly simplifies the exploration of a class.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @see java.lang.Class
 */
public class ClassExplorer {
	private final Class<?> _o;
	public ClassExplorer(final Object o) {
		_o = o.getClass();
	}
	public ClassExplorer(final Class<?> c) {
		_o = c;
	}
	/**
	 * Returns the simplified package.classname of this class's superclass.
	 * @return The simplified name of this class's superclass.
	 * @see java.lang.Class#getSuperclass()
	 * @see java.lang.Class#getSimpleName()
	 */
	public String getSuperSimpleName() {
		return _o.getSuperclass().getSimpleName();
	}
	/**
	 * Returns the Canonical name of this class's superclass.
	 * @return The canonical name of this class's superclass.
	 * @see java.lang.Class#getSuperclass()
	 * @see java.lang.Class#getName()
	 */
	public String getSuperName() {
		return _o.getSuperclass().getName();
	}
	/**
	 * Creates an array of the class heirarchy.<br>
	 * Element 0 is the topmost class.
	 * @return An array containing the class tree for this class.
	 */
	public Class<?>[] getClassTree() {
		final Stack<Class<?>> cs = new Stack<Class<?>>();
		Class<?> tmp = _o;
		cs.push(tmp);
		while ((tmp=tmp.getSuperclass())!=null)
			cs.push(tmp);
		final Class<?>[] tree = new Class[cs.size()];
		for (int i = 0;i<tree.length;i++)
			tree[i] = cs.pop();
		return tree;
	}
	public Class<?> getSuperclass() {
		return _o.getSuperclass();
	}
	public boolean isArray() {
		return _o.isArray();
	}
	public boolean isAnnotation() {
		return _o.isAnnotation();
	}
	public boolean isAnonymousClass() {
		return _o.isAnonymousClass();
	}
	public boolean isEnum() {
		return _o.isEnum();
	}
	public boolean isInterface() {
		return _o.isInterface();
	}
	public boolean isLocalClass() {
		return _o.isLocalClass();
	}
	public boolean isMemberClass() {
		return _o.isMemberClass();
	}
	public boolean isPrimitive() {
		return _o.isPrimitive();
	}
	public boolean isSynthetic() {
		return _o.isSynthetic();
	}
	public Type[] getInterfaces() {
		return _o.getInterfaces();
	}
	public TypeVariable<?>[] getTypeParameters() {
		return _o.getTypeParameters();
	}
	public Class<?>[] getSubclasses() {
		return _o.getClasses();
	}
	@Override
	public String toString() {
		final Class<?> c = _o;
		final StringWriterExt buf = new StringWriterExt();
		buf.writeln(c.getName());
		if (c.getSuperclass() != null)
			buf.writeln("Superclass:\t"+c.getSuperclass().getSimpleName());
		buf.writeln("Type:\t\t"+c.getSimpleName());
		buf.writeln("isArray:\t"+c.isArray());
		buf.writeln("isAnnotation:\t"+c.isAnnotation());
		buf.writeln("isAnonymous:\t"+c.isAnonymousClass());
		buf.writeln("isEnum:\t\t"+c.isEnum());
		buf.writeln("isInterface:\t"+c.isInterface());
		buf.writeln("isLocalClass:\t"+c.isLocalClass());
		buf.writeln("isMemberClass:\t"+c.isMemberClass());
		buf.writeln("isPrimitive:\t"+c.isPrimitive());
		buf.writeln("isSynthetic:\t"+c.isSynthetic());
		final Type[] inter = c.getInterfaces();
		if (inter!=null) {
			buf.writeln("Implemented Interfaces:");
			for (int i = 0;i<inter.length;i++) {
				buf.writeln("\t"+inter[i].toString());
			}
		}
		final TypeVariable<?>[] typed = c.getTypeParameters();
		if (typed!=null) {
			buf.writeln("Type Parameters:");
			for (int i = 0; i < typed.length; i++) {
				buf.writeln("\t"
						+ typed[i].getGenericDeclaration().getClass()
								.getCanonicalName());
			}
		}
		final Class<?>[] subs = c.getClasses();
		if (subs!=null) {
			buf.writeln("Known Subclasses:");
			for (int i = 0; i < subs.length; i++) {
				buf.writeln("\t"+subs[i].getCanonicalName());
			}
		}
		if (c.isEnum()) {
			final Object[] enums = c.getEnumConstants();
			buf.writeln("Declared enum values:");
			for (int i = 0; i < enums.length; i++) {
				buf.writeln("\t" + enums[i].toString());
			}
		}
		final Constructor<?>[] con = c.getDeclaredConstructors();
		if (con!=null) {
			buf.writeln("Declared Constructors:");
			for (int i = 0;i<con.length;i++) {
				buf.writeln("\t"+con[i].toString());
			}
		}
		final Annotation[] anno = c.getDeclaredAnnotations();
		if (anno!=null) {
			buf.writeln("Declared Annotations:");
			for (int i = 0;i<anno.length;i++) {
				buf.writeln("\t"+anno[i].toString());
			}
		}
		final Class<?>[] cla = c.getDeclaredClasses();
		if (cla!=null) {
			buf.writeln("Declared Classes:");
			for (int i = 0;i<cla.length;i++) {
				buf.writeln("\t"+cla[i].toString());
			}
		}
		final Method[] meth = c.getDeclaredMethods();
		if (meth!=null) {
			buf.writeln("Declared Methods:");
			for (int i = 0;i<meth.length;i++) {
				buf.writeln("\t"+meth[i].toString());
			}
		}
		final Field[] feild = c.getDeclaredFields();
		if (feild!=null) {
			buf.writeln("Declared Feilds:");
			for (int i = 0;i<feild.length;i++) {
				buf.writeln("\t"+feild[i].toString());
			}
		}
		return buf.toString();
	}
	public static void main(final String[] args) {
		if (args==null || args.length == 0) {
			System.out.println("Syntax:");
			System.out.println("\tjava [-cp <path>] simple.util.ClassExplorer <class>[, <class>...]");
			System.out.println("Where <path> is a comma dilimited list of paths or jars in");
			System.out.println("which <class> can be found.");
			System.exit(0);
		}
		File f;
		PrintStream out;
		for (final String cur : args) {
			try {
			f = new File(cur+".txt");
				f.createNewFile();
			out = new PrintStream(f);
			try {
				out.print(new ClassExplorer(Class.forName(cur)).toString());
			} catch (final ClassNotFoundException e) {
				e.printStackTrace(out);
			}
			out.flush();
			FileUtil.close(out);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

/**
 * 
 */
package com.yhd.arch.photon.util;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author Archer
 * 
 */
public class ActorNameUtil {

	public static String mangleName(Method method) {
		return mangleName(method, true);
	}

	/**
	 * Creates a unique mangled method name based on the method name and the
	 * method parameters.
	 * 
	 * @param method
	 *            the method to mangle
	 * @param isFull
	 *            if true, mangle the full classname
	 * 
	 * @return a mangled string.
	 */
	public static String mangleName(Method method, boolean isFull) {
		StringBuffer sb = new StringBuffer();

		sb.append(method.getName());

		Class[] params = method.getParameterTypes();
		for (int i = 0; i < params.length; i++) {
			sb.append('_');
			sb.append(mangleClass(params[i], isFull));
		}
		return sb.toString();
	}

	/**
	 * Mangles a classname.
	 */
	public static String mangleClass(Class cl, boolean isFull) {
		String name = cl.getName();

		if (name.equals("boolean") || name.equals("java.lang.Boolean"))
			return "boolean";
		else if (name.equals("int") || name.equals("java.lang.Integer") || name.equals("short") || name.equals("java.lang.Short")
				|| name.equals("byte") || name.equals("java.lang.Byte"))
			return "int";
		else if (name.equals("long") || name.equals("java.lang.Long"))
			return "long";
		else if (name.equals("float") || name.equals("java.lang.Float") || name.equals("double") || name.equals("java.lang.Double"))
			return "double";
		else if (name.equals("java.lang.String") || name.equals("com.caucho.util.CharBuffer") || name.equals("char")
				|| name.equals("java.lang.Character") || name.equals("java.io.Reader"))
			return "string";
		else if (name.equals("java.util.Date") || name.equals("com.caucho.util.QDate"))
			return "date";
		else if (InputStream.class.isAssignableFrom(cl) || name.equals("[B"))
			return "binary";
		else if (cl.isArray()) {
			return "[" + mangleClass(cl.getComponentType(), isFull);
		} else if (name.equals("org.w3c.dom.Node") || name.equals("org.w3c.dom.Element") || name.equals("org.w3c.dom.Document"))
			return "xml";
		else if (isFull)
			return name;
		else {
			int p = name.lastIndexOf('.');
			if (p > 0)
				return name.substring(p + 1);
			else
				return name;
		}
	}

}

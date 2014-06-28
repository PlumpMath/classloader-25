package com.amundi.main;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * an URLClassloader that delegate to its parent only as a last resort
 * 
 * @author zerog
 *
 */
public class InvertedClassLoader extends URLClassLoader {

	private ClassLoader parent;

	public InvertedClassLoader(URL[] urls, ClassLoader parent) {
		super(urls);
		this.parent = parent;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		Class<?> c = findLoadedClass(name);
		if (c == null) {

			try {
				c = findClass(name);
			} catch (ClassNotFoundException e) {
				// do nothing
			}
			if (c == null)
				return parent.loadClass(name);
		}

		return c;
	}
}

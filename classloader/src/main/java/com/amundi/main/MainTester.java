package com.amundi.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import com.amundi.test.api.IBaseInterface;

public class MainTester {

	private ClassLoader cl2;
	private ClassLoader cl1;

	public MainTester(ClassLoader cl1, ClassLoader cl2) {
		this.cl1 = cl1;
		this.cl2 = cl2;
	}

	public void runTest() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		IBaseInterface v1 = buildBaseInterface(cl1);
		IBaseInterface v2 = buildBaseInterface(cl2);
		
		try {
			testMethods(IBaseInterface.class, v1, v2);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private static void testMethods(Class clazz, Object o1, Object o2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Method m : clazz.getMethods()) {
			if (m.getName().startsWith("get")) {
				System.out.println("Calling method " + m.getName() +"() on both instances");
				System.out.println("Result for 1st obj : " + m.invoke(o1, null));
				System.out.println("Result for 2nd obj : " + m.invoke(o2, null));
			}
		}
	}

	private static IBaseInterface buildBaseInterface(ClassLoader cl)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Class<IBaseInterface> clazz = (Class<IBaseInterface>) cl
				.loadClass("com.amundi.test.TestedClass");
		return clazz.newInstance();

	}

	public static void main(String[] args) throws MalformedURLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, URISyntaxException {
		String currentPath = new File("").getAbsolutePath();

		URL url1 = new URL("file://" + currentPath
				+ "/libs/tested-bean-1.0.jar");
		URLClassLoader cl1 = new InvertedClassLoader(new URL[] { url1 }, ClassLoader.getSystemClassLoader());

		URL url2 = new URL("file://" + currentPath
				+ "/libs/tested-bean-2.0.jar");
		URLClassLoader cl2 = new InvertedClassLoader(new URL[] { url2 }, ClassLoader.getSystemClassLoader());

		new MainTester(cl1, cl2).runTest();

	}
}

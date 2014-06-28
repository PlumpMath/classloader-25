package com.amundi.main;

import java.io.File;
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
	
	public void runTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		IBaseInterface v1 = buildBaseInterface(cl1);
		IBaseInterface v2 = buildBaseInterface(cl2);
		
		System.out.println(v1.getVersion());
		System.out.println(v2.getVersion());
		
	}
	
	private static IBaseInterface buildBaseInterface(ClassLoader cl) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<IBaseInterface> clazz = (Class<IBaseInterface>) cl.loadClass("com.amundi.test.TestedClass");
		return clazz.newInstance();
		
	}
	

	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException {
		String currentPath = new File("").getAbsolutePath();
		
		URL url1 = new URL("file://" + currentPath + "/libs/tested-bean-1.0.jar");
		URLClassLoader cl1 = new URLClassLoader(new URL[] {url1});
		
		URL url2 = new URL("file://" + currentPath + "/libs/tested-bean-2.0.jar");
		URLClassLoader cl2 = new URLClassLoader(new URL[] {url2});

		new MainTester(cl1, cl2).runTest();
		
	}
}

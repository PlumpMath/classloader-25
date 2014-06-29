package com.amundi.net.client;
public class ByteArrayClassLoader extends ClassLoader {

    public Class findClass(String name, byte[] b) {
    	return defineClass(name,b,0,b.length);
    }

}
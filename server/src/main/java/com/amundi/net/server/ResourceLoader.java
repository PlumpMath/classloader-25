package com.amundi.net.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceLoader {
	
	private static final byte[] EMPTY = new byte[0];
	
	public byte[] loadClass(String className) {

		try {
			String path = className.replace('.', '/').concat(".class");
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					path);
			return toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EMPTY;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}

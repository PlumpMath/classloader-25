package com.amundi.net.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClassLoaderWorker implements Runnable {
	private BlockingQueue<ServerDataEvent> queue = new LinkedBlockingQueue<ServerDataEvent>();

	private static final byte[] EMPTY = new byte[0];

	public void processData(NioServer server, SocketChannel socket,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];

		System.arraycopy(data, 0, dataCopy, 0, count);
		try {
			queue.put(new ServerDataEvent(server, socket, dataCopy));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		ServerDataEvent dataEvent;

		while (true) {
			try {
				dataEvent = (ServerDataEvent) queue.take();

				byte[] ret = loadClass(new String(dataEvent.data));
				// Return to sender
				dataEvent.server.send(dataEvent.socket, ret);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private byte[] loadClass(String className) {

		try {
			String path = className.replace('.', '/').concat(".class");
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					path);
			toByteArray(is);
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

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static long copyLarge(InputStream input, OutputStream output)
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

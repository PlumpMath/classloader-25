package com.amundi.net.server;

import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClassLoaderWorker implements Runnable {
	private BlockingQueue<ServerDataEvent> queue = new LinkedBlockingQueue<ServerDataEvent>();
	private ResourceLoader loader;

	public ClassLoaderWorker() {
		this.loader = new ResourceLoader();
	}

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

				byte[] ret = loader.loadClass(new String(dataEvent.data));
				// Return to sender
				dataEvent.server.send(dataEvent.socket, ret);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

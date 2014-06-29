package com.amundi.net.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static final int port = 8080;

	private static final class ClassLoaderRunnable implements Runnable {

		private Socket socket;

		ClassLoaderRunnable(Socket socket) {
			this.socket = socket;
		}

		public void run() {

			// Un BufferedReader permet de lire par ligne.
			BufferedReader plec = null;
			PrintWriter pred = null;
			try {
				plec = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				// Un PrintWriter possède toutes les opérations print
				// classiques.
				// En mode auto-flush, le tampon est vidé (flush) à l'appel de
				// println.
				// pred = new PrintWriter(new BufferedWriter(
				// new OutputStreamWriter(socket.getOutputStream())), true);

				ResourceLoader loader = new ResourceLoader();

				OutputStream out = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(out);
				while (true) {
					String line = plec.readLine(); // lecture du message
					if ("END".equals(line)) {
						break;
					} else if ("".equals(line)) {
						continue;
					}

					System.out.println("Client asking for " + line); // trace

					byte[] b = loader.loadClass(line);
					oos.writeObject(b);
					out.flush();

				}
			} catch (IOException e) {
				if (plec != null)
					try {
						plec.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				if (pred != null)
					pred.close();

				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(port);
		Socket soc;
		while ((soc = s.accept()) != null) {
			new Thread(new ClassLoaderRunnable(soc)).start();
		}
		s.close();
	}
}

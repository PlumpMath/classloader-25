package com.amundi.net.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		System.setSecurityManager(null);
		String hostName = "localhost";
		int portNumber = 8080;
		Socket socket = null;
		try {
			socket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			
			InputStream in = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(in);
			String className ="com.amundi.test.TestedClass"; 
			out.println(className);
			
			try {
				byte [] result = (byte[]) ois.readObject();
				
				Class clazz = new ByteArrayClassLoader().findClass(className, result);
				Object o = clazz.newInstance();
				System.out.println("ici");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
			System.out.println("fin du stream");

//			String line = in.readLine();
//			System.out.println(line);
//			// byte[] bytes =
			// ResourceLoader.toByteArray(socket.getInputStream());
			// System.out.println(new String(bytes));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

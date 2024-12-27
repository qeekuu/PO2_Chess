package network; 

import java.net.*;
import java.io.*;

class ChessServer
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(3767);
		Socket socket = serverSocket.accept();
		
		System.out.println("Client connected");

		InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = bufferedReader.readLine();
		System.out.println("Client response: " + str);
		
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
		printWriter.println("Yes");
		printWriter.flush();

		serverSocket.close();
	}
}

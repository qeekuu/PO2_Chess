package network; 

import java.net.*;
import java.io.*;

class ChessClient
{
	public static void main(String[] args) throws IOException
	{
		Socket socket = new Socket("localhost", 3767);

		PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
		printWriter.println("working");
		printWriter.flush();

		InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = bufferedReader.readLine();
		System.out.println("Server respone: " + str);

		socket.close();
	}
}

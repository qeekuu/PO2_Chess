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

		serverSocket.close();
	}
}

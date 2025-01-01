package network; 

import java.net.*;
import java.io.*;

class ChessServer
{
	public static void main(String[] args) throws IOException
	{
		try(ServerSocket serverSocket = new ServerSocket(3000))
		{
			System.out.println("Server running...");

			Socket clientSocket = serverSocket.accept();
			System.out.println("Client connected: " + clientSocket.getInetAddress());
		

			InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader in = new BufferedReader(inputStreamReader);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			String strInput;
			while((strInput = in.readLine()) != null)
			{
				System.out.println("Client: " + strInput);
				if("quit".equalsIgnoreCase(strInput))
					break;

				//odpowiedz do klienta
				out.println("Server: Received -> " + strInput);
			}
			
			System.out.println("Client disconnected.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

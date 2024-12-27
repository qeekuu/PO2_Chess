package network; 

import java.net.*;
import java.io.*;

class ChessClient
{
	public static void main(String[] args) throws IOException
	{
		try(Socket socket = new Socket("localhost", 3000))
		{
			System.out.println("Connected to server.");

			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(inputStreamReader);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			String userLine;

			System.out.println("Type message");
			while((userLine = userInput.readLine()) != null)
			{
				out.println(userLine);
				out.flush();
				if("quit".equalsIgnoreCase(userLine))
					break;

				String response = in.readLine();
				System.out.println(response);
			}

			System.out.println("Disconnected form server.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

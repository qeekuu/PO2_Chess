package network; 

import java.net.*;
import java.io.*;

class ChessClient
{
	public static void main(String[] args) throws IOException
	{
		Socket socket = new Socket("localhost", 3767);
	}
}

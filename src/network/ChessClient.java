package network; 

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * Klient do łączenia się z ChessServer.
 *
 */

class ChessClient
{
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String host;
    private int port;
    private int playerId;
	
	public ChessClient(String host, int port){
		this.host = host;
		this.port = port;
	}

	public void startClients()
	{
		try{
			socket = new Socket(host, port);
			System.out.println("ChessClient connested to server: " + host + ":" + port);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// osbiór w osobnym watku wszystkich komunikatóœ z serwera
			Thread listenThread = new Thread(() -> {
				String serverMessage;
				try{
					while((serverMessage = in.readLine()) != null){
						handleServerMessage(serverMessage);
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			});
			listenThread.start();

			// czytanie ruchóœ z konsoli (powinno zostać przenieśione do board)
			Scanner scanner = new Scanner(System.in);
			while(true){
				String line = scanner.nextLine();
				out.println(line);
				out.flush();

				if("QUIT".equalsIgnoreCase(line))
					break;
			}
			scanner.close();
			closeConnection();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Tymaczsowa metoda obsługi komunikatów z serwera.
	 * Docelowo powinna być metoda typu board.handleInComingMove(startCol, startRow, endCol, endRow) z board
	 */
	private void handleServerMessage(String message){
		if(message.startsWith("Welcome Player")){
			// serwer nadaje id
			this.playerId = Integer.parseInt(message.split(" ")[1]);
			System.out.println("ChessClient assign player id: " + playerId);
		} else if(message.startsWith("MOVE")){
			System.out.println("ChessClient received opponent move -> " + message);	
			// w tym miejscu powinien się znajdować faktycnzy ruch na board np(parseMoveAndApply(message))
		} else {
			System.out.println("ChessClient: " + message);
		}
	}

	private void closeConnection(){
		try{
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(socket != null)
				socket.close();
			System.out.println("ChessClient: disconnected form the server.");
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		// test - uruchomienie lokalnie
		ChessClient client = new ChessClient("localhost", 3000);
		client.startClients();
	}
}

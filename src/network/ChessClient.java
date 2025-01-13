package network; 

import java.net.*;
import java.util.Scanner;

import javafx.application.Platform;

import java.io.*;

import main.Board;
/**
 * Klient do łączenia się z ChessServer.
 *
 */

public class ChessClient
{
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String host;
    private int port;
    private int playerId;

	private final Board board;

	public ChessClient(String host, int port, Board board){
		this.host = host;
		this.port = port;
		this.board = board;
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
			listenThread.setDaemon(true);
			listenThread.start();

			// czytanie ruchóœ z konsoli (powinno zostać przenieśione do board)
			// Scanner scanner = new Scanner(System.in);
			// while(true){
				// String line = scanner.nextLine();
				// out.println(line);
				// out.flush();

				// if("QUIT".equalsIgnoreCase(line))
					// break;
			// }
			// scanner.close();
			// closeConnection();

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
			String[] parts = message.split(" ");
			playerId = Integer.parseInt(parts[1]);
			System.out.println("ChessClient assign player id: " + playerId);
		} else if(message.startsWith("MOVE")){
			System.out.println("ChessClient received opponent move -> " + message);	
			// w tym miejscu powinien się znajdować faktycnzy ruch na board np(parseMoveAndApply(message))
			String[] parts = message.split(" ");
			if(parts.length == 5){
				int sc = Integer.parseInt(parts[1]);
				int sr = Integer.parseInt(parts[2]);
				int ec = Integer.parseInt(parts[3]);
				int er = Integer.parseInt(parts[4]);

				// metoda applyMove zmienia GUI, potrzba ją wywołać w wątku javaFx
				Platform.runLater(() -> board.applyMove(sc, sr, ec, er));
			}
		} else {
			System.out.println("ChessClient: " + message);
		}
	}

	// klient przesyła ruch do serwera
	public void sendMove(int startCol, int startRow, int endCol, int endRow){
		if(out != null){
			out.println("MOVE " + startCol + " " + startRow + " " + endCol + " " + endRow);
		}
	}

	private void closeConnection(){
		try{
			if(in != null)
				in.close();
			if(out != null)
				out.println("QUIT");
			if(socket != null)
				socket.close();
			System.out.println("ChessClient: disconnected form the server.");
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public int getPlayerId(){
		return playerId;
	}
}

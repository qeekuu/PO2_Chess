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
import pieces.PieceColor;

public class ChessClient
{
	private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String host;
    private int port;
    private int playerId;
	private PieceColor localColor;

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

			// closeConnection();

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Metoda odpowiadająca za obsługę komunikatów.
	 * @param message komunikat.
	 */
	private void handleServerMessage(String message){
		if(message.startsWith("Welcome Player")){
			// serwer nadaje id
			String[] parts = message.split(" ");
			playerId = Integer.parseInt(parts[2]); // część "trzecia" wiadomości z id
			System.out.println("ChessClient assign player id: " + playerId);
		} else if(message.startsWith("COLOR")){
			// white or black
			String[] parts = message.split(" ");
			String colorStr = parts[1];
			if(colorStr.equalsIgnoreCase("WHITE")) {
				localColor = PieceColor.WHITE;
			} else {
				localColor = PieceColor.BLACK;
			}

			board.setLocalColor(localColor);

			// if black player flip the board
			if(localColor == PieceColor.BLACK)
				board.flipBoard();

		} else if(message.startsWith("MOVE")){
			// np MOVE 1 2 3 4 (sc sr ec er)
			System.out.println("ChessClient received opponent move -> " + message);	
			String[] parts = message.split(" ");
			if(parts.length == 5){
				int sc = Integer.parseInt(parts[1]);
				int sr = Integer.parseInt(parts[2]);
				int ec = Integer.parseInt(parts[3]);
				int er = Integer.parseInt(parts[4]);

				// metoda applyMove zmienia GUI, potrzba ją wywołać w wątku javaFx
				// startcol, startrow, endcol, endrow
				Platform.runLater(() -> board.applyMove(sc, sr, ec, er));
			}
		} else {
			// jesli nie wysylamy ruchu a chcemy przeslac np GAME OVER
			System.out.println("ChessClient: " + message);
		}
	}

	// klient przesyła ruch do serwera
	/**
	 * Metoda odpowiadająca za przesłanie ruchu do serwera.
	 * @param startCol pole kolumny, z którego zaczyna ruch figura.
	 * @param startRow pole wiersza, z którego zaczyna ruch figura.
	 * @param endCol pole kolumny, na którym kończy ruch figura.
	 * @param endRow pole wiersza, na którym kończy ruch figura.
	 */
	public void sendMove(int startCol, int startRow, int endCol, int endRow){
		// if(out != null){
			// out.println("MOVE " + startCol + " " + startRow + " " + endCol + " " + endRow);
		// }
		System.out.println("DEBUG: ChessClient.sendMove(...) - about to write to server:");
		System.out.println("       MOVE " + startCol + " " + startRow + " " + endCol + " " + endRow);

	    if (out != null) {
		    out.println("MOVE " + startCol + " " + startRow + " " + endCol + " " + endRow);
		} else {
			System.out.println("DEBUG: out is null, can't send anything!");
		}
	}

	/**
	 * Metoda odpowiadająca za zamknięcie połączenia.
	 */
	private void closeConnection(){
		try{
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(socket != null && !socket.isClosed())
				socket.close();
			System.out.println("ChessClient: disconnected form the server.");
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Geter pobierający id gracza.
	 */
	public int getPlayerId(){
		return playerId;
	}
}

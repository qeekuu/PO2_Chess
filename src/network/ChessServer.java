package network; 

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiadająca za działanie serwera - oczekuje na dwóch hraczy, tworzy dla nich obiekty klasy PlayerHabdler
 * i przekazuje między niim ruchy. Serwer Domyślnie zaczyna pracę na procie 3000.
 *
 */

class ChessServer
{ 
	// lista przechowująca obiekty dla obu graczy
	private static List<PlayerHandler> players = new ArrayList<>(2);

	private static int connectedPlayers = 0;

	public static void main(String[] args) throws IOException
	{
		try(ServerSocket serverSocket = new ServerSocket(3000))
		{
			System.out.println("Server running using port 3000.");
			System.out.println("Waiting for two players...");

			while(connectedPlayers < 2){
			
				Socket clientSocket = serverSocket.accept();
				connectedPlayers++;
				System.out.println("Client connected: " + connectedPlayers + "from address: " + clientSocket.getInetAddress());
	
				PlayerHandler handler = new PlayerHandler(clientSocket, connectedPlayers);
				players.add(handler);

				// start wątku obsługującego danego graza
				handler.start();

				System.out.println("Both players are connected.");

				// InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				// BufferedReader in = new BufferedReader(inputStreamReader);
				// PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				// String strInput;
				// while((strInput = in.readLine()) != null)
				// {
					// System.out.println("Client: " + strInput);
					// if("quit".equalsIgnoreCase(strInput))
						// break;

					//odpowiedz do klienta
					// out.println("Server: Received -> " + strInput);
				// }
			
				// System.out.println("Client disconnected.");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	* Klasa wewnętrzna odpowiadająca za komunikację z jedym graczem w osobnym wątku.
	* Otrzymuje komunikaty od gracza i przekazuje je do drugiego z gracza.
	*
	 */

	private static class PlayerHandler extends Thread{

		private Socket socket;
		private int playerId;
		private BufferedReader in;
		private PrintWriter out;

		public PlayerHandler(Socket socket, int playerId){
			this.socket = socket;
			this.playerId = playerId;
		}

		@Override
		public void run(){
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				out.println("Welcome Player " + playerId);

				String inputLine;
				while((inputLine = in.readLine()) != null){
					System.out.println("Player # " + playerId + ":" + inputLine);

					// przeslanie ruchu klienta przez serwer do drugiego gracza
					if(inputLine.startsWith("MOVE ")){
						// rozesłanie do pozostałych graczy
						broadcastToOthers(playerId, inputLine);
					}

					if("QUIT".equalsIgnoreCase(inputLine)){
						System.out.println(" The player # " + playerId + "ended the connection.");
						break;
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				// zamkniecei zasoboww
				try{
					if(in != null)
						in.close();
					if(out != null)
						out.close();
					if(socket != null)
						socket.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}

		/**
		 * Wysyła wiadomość do konkretnego gracza.
		 */
		public void sendMessage(String message){
			out.println(message);
		}

		/**
		 * Przesyła komunikat do pozostałych graczy (innych niż senderIN).
		 */
		private static void broadcastToOthers(int senderID, String message){
			for(PlayerHandler ph : players)
				if(ph.playerId != senderID)
					ph.sendMessage(message);
		}
	}
}

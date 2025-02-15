package main;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import pieces.Type;
import pieces.PieceColor;
import pieces.Piece;
import pieces.Pawn;
import pieces.Knight;
import pieces.Bishop;
import pieces.Rook;
import pieces.Queen;
import pieces.King;

import network.*;
import database.*;

public class Board extends Pane
{
	private int columns = 8;
	private int rows = 8;
	private double tileSize = 80;
	private Piece selectedPiece = null;
	private int selectedPiecePreCol;
	private int selectedPiecePreRow;
	private ImageView selectedPieceView;
	private List<Piece> pieces = new ArrayList<>(); // zapamietanie pozycji
	private Piece piece;
	private boolean gameOver = false;
	private PieceColor currentTurn = PieceColor.WHITE;
	private MoveDataBaseSaveReading moveDB = new MoveDataBaseSaveReading(); 
	private int currentGameId = 1;
	private PieceColor localColor;

	// referencja do klienta
	private ChessClient chessClient;
	
	public Board(ChessClient client)
	{
		this.chessClient = client;
		drawBoard();	
		setupPieces();
	}

	/**
	 * Metoda odpowiadająca z "narysowanie" planszy.
	 */
	private void drawBoard()
	{
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < columns; col++)
			{
				Rectangle tile = new Rectangle(tileSize, tileSize);
				tile.setX(col * tileSize);
				tile.setY(row * tileSize);
				if((row+col) % 2 == 0)
					tile.setFill(Color.WHITESMOKE);
				else
					tile.setFill(Color.GREY);
				getChildren().add(tile);
			}
		}
		setPrefSize(columns * tileSize, rows * tileSize);
	}

	/**
	 * Metoda odpowiadająca za rozmieszczenie figur na szachownicy.
	 * Wykorzystuje metodę "addPiece".
	 */
	private void setupPieces(){
		//Black pieces
        addPiece(0, 0, Type.ROOK, PieceColor.BLACK);
		addPiece(1, 0, Type.KNIGHT, PieceColor.BLACK);
		addPiece(2, 0, Type.BISHOP, PieceColor.BLACK);
		addPiece(3, 0, Type.QUEEN, PieceColor.BLACK);
		addPiece(4, 0, Type.KING, PieceColor.BLACK);
		addPiece(5, 0, Type.BISHOP, PieceColor.BLACK);
		addPiece(6, 0, Type.KNIGHT, PieceColor.BLACK);
		addPiece(7, 0, Type.ROOK, PieceColor.BLACK);

		for(int i = 0; i < 8; i++)
			addPiece(i, 1, Type.PAWN, PieceColor.BLACK);

	
		// White pieces
		addPiece(0, 7, Type.ROOK, PieceColor.WHITE);
		addPiece(1, 7, Type.KNIGHT, PieceColor.WHITE);
		addPiece(2, 7, Type.BISHOP, PieceColor.WHITE);
		addPiece(3, 7, Type.QUEEN, PieceColor.WHITE);
		addPiece(4, 7, Type.KING, PieceColor.WHITE);
		addPiece(5, 7, Type.BISHOP, PieceColor.WHITE);
		addPiece(6, 7, Type.KNIGHT, PieceColor.WHITE);
		addPiece(7, 7, Type.ROOK, PieceColor.WHITE);

		for(int i = 0; i < 8; i++)
			addPiece(i, 6, Type.PAWN, PieceColor.WHITE);
	}
	
	/**
	 * Metoda odpowiadająca za dodanie instancji figury.
	 * @param col kolumna.
	 * @param row wiersz.
	 * @param type typ figury.
	 * @param pieceColor kolor figury.
	 * Metoda ta posiada również lambdy zajmujące się logiką ruchu myszy.
	 */
	public void addPiece(int col, int row, Type type, PieceColor pieceColor){
		
		Piece piece;

		// Tworzymy odpowiedni typ figury w zależności od podanego typu
		switch (type) {
			case PAWN:
				piece = new Pawn(pieceColor, col, row, this);
				break;
			case KNIGHT:
				piece = new Knight(pieceColor, col, row, this);
				break;
			case BISHOP:
				piece = new Bishop(pieceColor, col, row, this);
				break;
			case ROOK:
				piece = new Rook(pieceColor, col, row, this);
				break;
			case QUEEN:
				piece = new Queen(pieceColor, col, row, this);
				break;
			case KING:
			default:
				piece = new King(pieceColor, col, row, this);
				break;
		}

		ImageView pieceView = piece.getImageView();

		// Pozycjonowanie
		pieceView.setX(col * tileSize);
		pieceView.setY(row * tileSize);

		// Obsługa kliknięcia (wybór figury)
		pieceView.setOnMousePressed(event -> {
			if (gameOver || !isCurrentTurn(piece.getColor()) || piece.getColor() != localColor) {
				return;
			}
			selectedPiece = piece;
			selectedPieceView = pieceView;
			selectedPiecePreCol = piece.getColumn();
			selectedPiecePreRow = piece.getRow();
			System.out.println("Selected piece: " + selectedPiece);
		});

		// Obsługa przeciągania (ruch figury)
		pieceView.setOnMouseDragged(event -> {
			if (gameOver || selectedPiece == null) {
				return;
			}

			// Przeliczenie pozycji myszy na współrzędne ekranu z uwzględnieniem odwróconej szachownicy
			// double mouseX = event.getSceneX();
			// double mouseY = event.getSceneY();

			// Pobranie współrzędnych myszy względem szachownicy
			// double mouseX = event.getX();
			// double mouseY = event.getY();

			double mouseX = event.getSceneX() - getLayoutX();
			double mouseY = event.getSceneY() - getLayoutY();

			if (localColor == PieceColor.BLACK) {
				double boardSize = 8 * tileSize;
				mouseX = boardSize - (event.getSceneX() - getLayoutX());
				mouseY = boardSize - (event.getSceneY() - getLayoutY());
			}

			pieceView.setX(mouseX - tileSize / 2);
			pieceView.setY(mouseY - tileSize / 2);
		});

		// Obsługa zwolnienia myszy (upuszczenie figury)
		pieceView.setOnMouseReleased(event -> {
			if (gameOver) {
				return;
			}
			if (selectedPiece != null) {
				// int newCol = getCorrectedColumn(event.getX());
				// int newRow = getCorrectedRow(event.getY());

				double mouseX = event.getSceneX() - getLayoutX();
				double mouseY = event.getSceneY() - getLayoutY();

				int newCol = getCorrectedColumn(mouseX);
				int newRow = getCorrectedRow(mouseY);

				boolean validMove = selectedPiece.canMove(selectedPiecePreCol, selectedPiecePreRow, newCol, newRow);

				if (validMove) {
					selectedPiece.setColumn(newCol);
					selectedPiece.setRow(newRow);
					pieceView.setX(newCol * tileSize);
					pieceView.setY(newRow * tileSize);

					System.out.println("Moved " + selectedPiece.getType().toString().toLowerCase()
									+ " to: Column: " + newCol + ", Row: " + newRow);

					// Wysłanie ruchu do serwera
					sendMove(selectedPiecePreCol, selectedPiecePreRow, newCol, newRow);

					// Zapis do bazy danych
					moveDB.saveMove(new Move(
							currentGameId,
							selectedPiece.getType().toString(),
							selectedPiece.getColor().toString(),
							selectedPiecePreCol,
							selectedPiecePreRow,
							newCol,
							newRow
					));

					// Obsługa promocji pionka
					if (selectedPiece.getType() == Type.PAWN && selectedPiece.canPromote()) {
						((Pawn) selectedPiece).handlePromotion();
						switchTurn();
					}

					// Sprawdzenie, czy jest mat
					PieceColor currentColor = selectedPiece.getColor();
					PieceColor opponentColor = (currentColor == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
					if (isCheckmate(opponentColor)) {
						System.out.println("Checkmate: " + currentColor + " wins!");
						gameOver = true;
						gameOverWindow();
					} else {
						switchTurn();
					}
				} else {
					// Niepoprawny ruch - powrót do poprzedniej pozycji
					pieceView.setX(selectedPiecePreCol * tileSize);
					pieceView.setY(selectedPiecePreRow * tileSize);
					System.out.println("Invalid move!");
				}

				selectedPiece = null;
			}
		});

		pieces.add(piece);
		getChildren().add(pieceView);
	}
	// Funkcja do poprawnego przeliczania kolumny względem koloru gracza
	/**
	 * Metoda odpowiadająca za przeliczenie kolumnu względem koloru gracza (związane z obróceniem szachownicy u drugiego klienta).
	 */
	private int getCorrectedColumn(double mouseX) {
		if (localColor == PieceColor.BLACK) {
			return 7 - (int)(mouseX / tileSize);
		} else {
			return (int)(mouseX / tileSize);
		}
	}

	// Funkcja do poprawnego przeliczania wiersza względem koloru gracza
	/**
	 * Metoda odpowiadająca za przeliczenie wiersza względem koloru gracza (związane z obróceniem szachownicy u drugiego klienta).
	 */
	private int getCorrectedRow(double mouseY) {
		if (localColor == PieceColor.BLACK) {
			return 7 - (int)(mouseY / tileSize);
		} else {
			return (int)(mouseY / tileSize);
		}
	}

	/**
	 * Metoda odpowiadająca za zmianę tury.
	 */
	private void switchTurn(){
		currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
		System.out.println("Now it's " + currentTurn + "'s turn.");
	}
	/**
	 * Metoda sprawdzająca turę.
	 */
	private boolean isCurrentTurn(PieceColor pieceColor){
		return pieceColor == currentTurn;
	}



	/**
	 * Metody sieciowe:
	 */
	
	/**
	 * Metoda wywoływana, gdy lokalny gracz wykona ruch i che poinformowac o nim serwer.
	 * @param startCol pole kolumny, z którego zaczyna ruch figura.
	 * @param startRow pole wiersza, z którego zaczyna ruch figura.
	 * @param endCol pole kolumny, na którym kończy ruch figura.
	 * @param endRow pole wiersza, na którym kończy ruch figura.

	 */
	public void sendMove(int startCol, int startRow, int endCol, int endRow){
		System.out.println("DEBUG: Board.sendMove(...) was called.");
		System.out.println("       chessClient = " + chessClient);
		System.out.println("       coords: " + startCol + "," + startRow + " -> " + endCol + "," + endRow);

		if(chessClient != null) {
		   chessClient.sendMove(startCol, startRow, endCol, endRow);
		} else {
			System.out.println("DEBUG: chessClient is null, can't send!");
		}
	}

	/**
	 * Metoda wywoływana, gdy przeciwnik (połączony przez sieć) wykonał ruch a serwer wysłał o tym ingormacje.
	 * Należy odwzorować ruch na lokalnej szchownicy.
	 * @param startCol pole kolumny, z którego zaczyna ruch figura.
	 * @param startRow pole wiersza, z którego zaczyna ruch figura.
	 * @param endCol pole kolumny, na którym kończy ruch figura.
	 * @param endRow pole wiersza, na którym kończy ruch figura.

	 */
	
	public void applyMove(int startCol, int startRow, int endCol, int endRow){
		Piece movedPiece = getPiece(startCol, startRow);
		if(movedPiece != null){
			// jesli na polu targetowanym znajduje sie figura to nastepuje bicie
			Piece capturedPiece = getPiece(endCol, endRow);
			if(capturedPiece != null)
				removePiece(endCol, endRow);

			// aktualizacja pozycji
			movedPiece.setColumn(endCol);
			movedPiece.setRow(endRow);

			// przesuniecie obrazu
			ImageView imv = movedPiece.getImageView();
			imv.setX(endCol * tileSize);
			imv.setY(endRow * tileSize);

			switchTurn();
		}
	}

	/**
	 * Setter dla chessCkient
	 *
	 */
	public void setChessClient(ChessClient c){
		this.chessClient = c;
	}

	/**
	 * Metoda sprawdzająca czy na danym polu stoi jakaś fiigura.
	 * @param col kolumna. 
	 * @param row wiersz.
	 * @return true jeśli pole jest zajęte, false jeśli puste.
	 */
	public boolean isSquareQccupied(int col, int row) 
	{
		for (int i = 0; i < pieces.size(); i++) 
		{
			piece = pieces.get(i);
			if (piece.getColumn() == col && piece.getRow() == row) 
			{
				System.out.println("Square occupied by: " + piece.getType() + " at (" + col + ", " + row + ")");
				return true;
			}
		}
		return false;
	}

	/**
	 * Metoda obracająca szachownicę dal drugiego klienta.
	 */
	public void flipBoard(){
		this.setRotate(180);

		for(Piece p : pieces){
			p.getImageView().setRotate(180);
		}
	}
	/**
	 * Metoda ustawiająca kolor dla szachownicy.
	 */
	public void setLocalColor(PieceColor color){
		this.localColor = color;
	}

	/**
	* Metoda sprawdzająca czy pole jest pod atakiem.
	* @param col kolumna.
	* @param row wiersz.
	* @param defendingColor (unikanie sprawdzania ataku dla tego samego koloru bierek).
	* @param type typ bierki.
	* @return true jeśli pole jest pod atakiem, false jeśli nie.
	*/
	public boolean isUnderAttack(int col, int row, PieceColor defendingColor, Type type)
	{
		for(Piece piece : pieces)
		{
			if(piece.getColor() != defendingColor)
			{
				if(piece.canAttack(piece.getColumn(), piece.getRow(), col, row))
				{
					System.out.println("Square (" + col + ", " + row + ") is under attack by " + piece.getClass().getSimpleName() + " at (" 
                                   + piece.getColumn() + ", " + piece.getRow() + ").");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Metoda sprawdzająca czy król jest w szachu.
	 * @param color kolor figury.
	 * @return metodę isUnderAttack, która sprawdza, czy pole jest atakowane.
	 */
	public boolean isKingInCheck(PieceColor color)
	{
		King king = null;
		for(Piece piece : pieces)
		{
			if(piece.getType() == Type.KING && piece.getColor() == color)
			{
				king = (King) piece;
				break;
			}
		}

		// awaryjnie jesli nie ma krola
		if(king == null)
			return false;

		int kingCol = king.getColumn();
		int kingRow = king.getRow();

		return isUnderAttack(kingCol, kingRow, color, Type.KING);
		
	}

	/**
	* Tymczasowo wykonuje ruch figurą 'piece' na pole (targetCol, targetRow),
	* sprawdza, czy król nadal jest w szachu. Jeżeli tak – cofa ruch.
	* Jeśli nie – pozostawia figurę na nowym polu.
	*
	* @param piece     figura, którą próbujemy ruszyć
	* @param targetCol kolumna docelowa
	* @param targetRow wiersz docelowy
	* @return true, jeśli ruch jest legalny (król nie w szachu),
	*         false w przeciwnym razie (ruch cofnięty)
	*/
	public boolean tryMovePiece(Piece piece, int targetCol, int targetRow) 
	{
		int oldCol = piece.getColumn();
		int oldRow = piece.getRow();
  
		Piece captured = getPiece(targetCol, targetRow);

		// jesli jest figura zbij
		if (captured != null) 
		{
			removePiece(targetCol, targetRow);
		}
		
		// aktualizacja pozycjib
		piece.setColumn(targetCol);
		piece.setRow(targetRow);

		boolean stillInCheck = isKingInCheck(piece.getColor());

		if (stillInCheck) 
		{
			// przywróć figurę na starą pozycję
			piece.setColumn(oldCol);
			piece.setRow(oldRow);

			// jesli zbito figure a dalej szach to ja przywroc
			if (captured != null) 
			{
				pieces.add(captured);
				getChildren().add(captured.getImageView());
			}
			return false;
		}
		return true;
	}

	/**
	 * Metoda sprawdzająca czy jest szach z matem.
	 * @param color kolor figury.
	 * @return false, jeśli nie ma, true jeśli jest mat.
	 */
	public boolean isCheckmate(PieceColor color) 
	{
		// jesli nie wystepuje szach to nie moze wystapic mat
		if (!isKingInCheck(color)) 
			return false; 
		
		for(Piece piece : pieces)
		{
			if(piece.getColor() == color)
			{
				int orginalCol = piece.getColumn();
				int originalRow = piece.getRow();

				// Sprawdzenie wszystkich możliwości
				for(int targetCol = 0; targetCol < 8; targetCol++)
					for(int targetRow = 0; targetRow < 8; targetRow++)
						if(piece.canMove(orginalCol, originalRow, targetCol, targetRow))
						{
							// Piece capturedPiece = getPiece(targetCol, targetRow);

							// próba ruchu
							piece.setColumn(targetCol);
							piece.setRow(targetRow);
							// if(capturedPiece != null)
								// removePiece(targetCol, targetRow);

							boolean kingStillInCheck = isKingInCheck(color);

							// cofniecie ruchu, powrót do starej pozycji
							piece.setColumn(orginalCol);
							piece.setRow(originalRow);
							// if(capturedPiece != null)
							// {
								// pieces.add(capturedPiece);
								// getChildren().add(capturedPiece.getImageView());
							// }

							if(!kingStillInCheck)
								return false;
						}
			}
		}
		return true; // mat
	}

	/**
	 * Metoda wyświetlająca okno po zakończeniu gry z wyborem wyjścia, bądz rozpoczęcia gry na nowo. 
	 */
	public void gameOverWindow()
	{
		if(gameOver)
			{
				System.out.println("Game Over");
				// board.removePiece(col, row);
				// board.addPiece(col, row, Type.QUEEN, pieceColor);

				VBox gameOptions = new VBox(10);
				gameOptions.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
				gameOptions.setAlignment(Pos.CENTER);
		
				Text gameOverText = new Text("Game Over.");
				gameOverText.setFill(Color.WHITE);

				Button playAgainButton = new Button("Play again");
				Button quitButton = new Button("Quit");

				//Styl przyciskó
				playAgainButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
				quitButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

				//Zdarzenia przycisków
				playAgainButton.setOnAction(e -> {
					resetBoard();
					removeGameOverOptions(gameOptions);
				});

				quitButton.setOnAction(e -> {
					removeGameOverOptions(gameOptions);
					System.exit(0);
				});

				gameOptions.getChildren().addAll(gameOverText, playAgainButton, quitButton);
			
				// dodanie do sceny
				StackPane root = (StackPane) getScene().getRoot();
				root.getChildren().add(gameOptions);
		}
	
	}

	/**
	 * Metoda usuwająca scenę po wyborze.
	 */
	private void removeGameOverOptions(VBox gameOptions) 
	{
		StackPane root = (StackPane) getScene().getRoot();
		root.getChildren().remove(gameOptions);
	}
	
	/**
	 * Metoda resetująca stan szachownicy do początkowego po wybraniu opcji "playAgain".
	 */
	private void resetBoard()
	{
		// usuniecie wszystkich bierek
		getChildren().removeIf(node -> node instanceof ImageView);
		pieces.clear();

		// ponowne ustawieni
		setupPieces();

		//reset stanul;
		gameOver = false;
		selectedPiece = null;
	}

	/**
	 * Getter do pobrania figury z szachownicy.
	 * @return figura, jeśli nie pobrano null.
	 */
	public Piece getPiece(int col, int row)
	{
		for(Piece p : pieces)
		{
			if(p.getColumn() == col && p.getRow() == row)
				return p;
		}
		return null;
	}

	/**
	 * Getter do pobrania rozmiaru kafelka.
	 * @return tileSize.
	 */
	public double getTileSize()
	{
		return tileSize;
	}

	/**
	 * Metoda usuwająca figurę z szachownicy podczas bicia.
	 * @param col kolumna.
	 * @param row wiersz.
	 * @return true jeśli zbito, false jeśli nie.
	 */
	public void removePiece(int col, int row)
	{
		pieces.removeIf(piece -> {
			if (piece.getColumn() == col && piece.getRow() == row) {
				getChildren().remove(piece.getImageView()); // Usunięcie graficznej reprezentacji
				return true;
			}
			return false;
		});

		System.out.println("Piece removed from (" + col + ", " + row + ")");
	}
	
	public void clearChildren(int col, int row)
	{
		if(piece.getColumn() == col && piece.getRow() == row)
			getChildren().remove(piece.getImageView());
	}

}


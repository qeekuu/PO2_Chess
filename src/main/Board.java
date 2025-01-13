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

public class Board extends Pane
{
	private int columns = 8;
	private int rows = 8;
	private double tileSize = 80;
	private Piece selectedPiece = null;
	private int selectedPiecePreCol;
	private int selectedPiecePreRow;
	private ImageView selectedPieceView;
	private Rectangle currentTileLight = null;
	private List<Piece> pieces = new ArrayList<>(); // zapamietanie pozycji
	private Piece piece;
	private boolean gameOver = false;
	
	public Board()
	{
		drawBoard();	
		setupPieces();
	}

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
	
private void setupPieces()
{
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

public void addPiece(int col, int row, Type type, PieceColor pieceColor) 
{
    Piece piece;

    // Tworzymy odpowiedni typ figury w zależności od podanego typu
    switch (type) 
	{
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

	// ruch - umiescic w klasie mouse albo podobnej
	pieceView.setOnMousePressed(event -> {
		if(gameOver)
			return;
		selectedPiece = piece;
		selectedPieceView = pieceView;
		selectedPiecePreCol = piece.getColumn();
		selectedPiecePreRow = piece.getRow();
		System.out.println("Selected piece :" + selectedPiece);
	});

	pieceView.setOnMouseDragged(event -> {
		if(gameOver)
			return;
		if(selectedPiece != null)
		{
			if(selectedPiece != null)
			{
				pieceView.setX(event.getSceneX() - tileSize / 2);
				pieceView.setY(event.getSceneY() - tileSize / 2);
			}
		}

	});

pieceView.setOnMouseReleased(event -> {
	if(gameOver)
		return;
    if (selectedPiece != null) {
        // Finalizacja ruchu z zaokrągleniem do najbliższego kafelka
        int newCol = (int) (event.getSceneX() / tileSize);
        int newRow = (int) (event.getSceneY() / tileSize);

        boolean validMove = false;

        switch (selectedPiece.getType()) {
            case KING:
            case KNIGHT:
            case ROOK:
            case BISHOP:
			case QUEEN:
			case PAWN:
                validMove = selectedPiece.canMove(selectedPiecePreCol, selectedPiecePreRow, newCol, newRow);
                break;
            default:
                validMove = true;
                break;
        }

        if (validMove) 
		{
			if(selectedPiece.getType() == Type.KING && ((King) selectedPiece).hasJustCastled())
			{
				System.out.println("Castlinh");
			}
			else
			{
				selectedPiece.setColumn(newCol);
				selectedPiece.setRow(newRow);
				pieceView.setX(newCol * tileSize);
				pieceView.setY(newRow * tileSize);
				System.out.println("Moved " + selectedPiece.getType().toString().toLowerCase() + " to: Column: " + newCol + ", Row: " + newRow);
				
				// Aktualizacja po promocji
                if (selectedPiece.getType() == Type.PAWN && selectedPiece.canPromote()) 
				{
					((Pawn) selectedPiece).handlePromotion();
                }
				//mat
				PieceColor currentColor = selectedPiece.getColor();
				PieceColor opponetColor = (currentColor == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
				if(isCheckmate(opponetColor))
				{
					System.out.println("Checkmate: " + currentColor + " wins!");
					gameOver = true;
					gameOverWindow();
				}
			}
		}	
		else 
		{
            pieceView.setX(selectedPiece.getColumn() * tileSize);
            pieceView.setY(selectedPiece.getRow() * tileSize);
            System.out.println("Invalid move!");
        }

        selectedPiece = null;
    }
});

	pieces.add(piece);
    getChildren().add(pieceView);
}
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
	* @param defendingColor (unikanie sprawdzania ataku dla tego samego koloru bierek)
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
							Piece capturedPiece = getPiece(targetCol, targetRow);

							// próba ruchu
							piece.setColumn(targetCol);
							piece.setRow(targetRow);
							if(capturedPiece != null)
								removePiece(targetCol, targetRow);

							boolean kingStillInCheck = isKingInCheck(color);

							// cofniecie ruchu, powrót do starej pozycji
							piece.setColumn(orginalCol);
							piece.setRow(originalRow);
							if(capturedPiece != null)
							{
								pieces.add(capturedPiece);
								getChildren().add(capturedPiece.getImageView());
							}

							if(!kingStillInCheck)
								return false;
						}
			}
		}
		return true; // mat
	}

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

	private void removeGameOverOptions(VBox gameOptions) 
	{
		StackPane root = (StackPane) getScene().getRoot();
		root.getChildren().remove(gameOptions);
	}
	
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

	public Piece getPiece(int col, int row)
	{
		for(Piece p : pieces)
		{
			if(p.getColumn() == col && p.getRow() == row)
				return p;
		}
		return null;
	}

	public double getTileSize()
	{
		return tileSize;
	}

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


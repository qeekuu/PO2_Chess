package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;


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
	
	boolean canMove;
	boolean validSquare;

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

private void addPiece(int col, int row, Type type, PieceColor pieceColor) 
{
    Piece piece;

    // Tworzymy odpowiedni typ figury w zależności od podanego typu
    switch (type) {
        case PAWN:
            piece = new Pawn(pieceColor, col, row);
            break;
        case KNIGHT:
            piece = new Knight(pieceColor, col, row);
            break;
        case BISHOP:
            piece = new Bishop(pieceColor, col, row);
            break;
        case ROOK:
            piece = new Rook(pieceColor, col, row);
            break;
        case QUEEN:
            piece = new Queen(pieceColor, col, row);
            break;
        case KING:
        default:
            piece = new King(pieceColor, col, row);
            break;
    }
	
    ImageView pieceView = piece.getImageView();

    // Pozycjonowanie
    pieceView.setX(col * tileSize);
    pieceView.setY(row * tileSize);

	// ruch - umiescic w klasie mouse albo podobnej
	pieceView.setOnMousePressed(event -> {
		selectedPiece = piece;
		selectedPieceView = pieceView;
		selectedPiecePreCol = piece.getColumn();
		selectedPiecePreRow = piece.getRow();
		System.out.println("Selected piece :" + selectedPiece);
	});

	pieceView.setOnMouseDragged(event -> {
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
		if(selectedPiece != null)
		{
			// finalizacje ruchu z zaokroagleniem do najblizszego kafelka
			int newCol = (int) (event.getSceneX() / tileSize);
			int newRow = (int) (event.getSceneY() / tileSize);
			
			// Sprawdzenie czy roch jest poprawny dla wybranej figury
			if(selectedPiece.getType() == Type.KING)
			{
				if(selectedPiece.canMove(selectedPiecePreCol, selectedPiecePreRow, newCol, newRow))
				{
					selectedPiece.setColumn(newCol);
					selectedPiece.setRow(newRow);
					pieceView.setX(newCol * tileSize);
					pieceView.setY(newRow * tileSize);
					System.out.println("Moved King to: " + "Column: " + newCol + " Row" + newRow);
				}
				else
				{
					pieceView.setX(selectedPiece.getColumn() * tileSize);
					pieceView.setY(selectedPiece.getRow() * tileSize);
					System.out.println("Invalid move!");	
				}

			}
			else
			{
				piece.setColumn(newCol);
				piece.setRow(newRow);
				pieceView.setX(newCol * tileSize);
				pieceView.setY(newRow * tileSize);
				System.out.println("Piece moved to: Column " + newCol + ", Row " + newRow);
			}

			selectedPiece = null;
		}
	});

    getChildren().add(pieceView);
}
}


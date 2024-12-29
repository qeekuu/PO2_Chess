package main;

import java.util.ArrayList;

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

import java.util.ArrayList;
import java.util.List;

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

        if (validMove) {
            selectedPiece.setColumn(newCol);
            selectedPiece.setRow(newRow);
            pieceView.setX(newCol * tileSize);
            pieceView.setY(newRow * tileSize);
            System.out.println("Moved " + selectedPiece.getType().toString().toLowerCase() + " to: Column: " + newCol + ", Row: " + newRow);
        } else {
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
	
	public Piece getPiece(int col, int row)
	{
		return piece;
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

}


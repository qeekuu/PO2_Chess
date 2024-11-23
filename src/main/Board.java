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
    
	}

public void addPiece(int col, int row, Type type, PieceColor pieceColor) {
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

    getChildren().add(pieceView);
}


}


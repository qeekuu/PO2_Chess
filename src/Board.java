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

import pieces.Piece;
import pieces.PieceColor;
import pieces.Type;

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
        // Czarne figury
        addPiece(0, 0, PieceColor.BLACK, Type.ROOK);
		addPiece(1, 0, PieceColor.BLACK, Type.KNIGHT);
        addPiece(2, 0, PieceColor.BLACK, Type.BISHOP);
        addPiece(3, 0, PieceColor.BLACK, Type.QUEEN);
        addPiece(4, 0, PieceColor.BLACK, Type.KING);
        addPiece(5, 0, PieceColor.BLACK, Type.BISHOP);
        addPiece(6, 0, PieceColor.BLACK, Type.KNIGHT);
        addPiece(7, 0, PieceColor.BLACK, Type.ROOK);

        for (int col = 0; col < columns; col++) 
		{
            addPiece(col, 1, PieceColor.BLACK, Type.PAWN);
        }	

        // Białe figury
        addPiece(0, 7, PieceColor.WHITE, Type.ROOK);
        addPiece(1, 7, PieceColor.WHITE, Type.KNIGHT);
        addPiece(2, 7, PieceColor.WHITE, Type.BISHOP);
        addPiece(3, 7, PieceColor.WHITE, Type.QUEEN);
        addPiece(4, 7, PieceColor.WHITE, Type.KING);
        addPiece(5, 7, PieceColor.WHITE, Type.BISHOP);
        addPiece(6, 7, PieceColor.WHITE, Type.KNIGHT);
        addPiece(7, 7, PieceColor.WHITE, Type.ROOK);

        for (int col = 0; col < columns; col++) 
		{
            addPiece(col, 6, PieceColor.WHITE, Type.PAWN);
        }  
	}

	public void addPiece(int col, int row, PieceColor color, Type type)
	{
		Piece piece = new Piece(col, row, color == PieceColor.WHITE, type.name().toLowerCase(), 1);
		piece.setType(type);
		piece.setColor(color);


		ImageView pieceView = piece.getImageView();
		
		pieceView.setX(col * tileSize);
		pieceView.setY(row * tileSize);
		getChildren().add(pieceView);
	}

}


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
        addPiece(0, 0, false, "rook");
		addPiece(1, 0, false, "knight");
		addPiece(2, 0, false, "bishop");
		addPiece(3, 0, false, "queen");
		addPiece(4, 0, false, "king");
		addPiece(5, 0, false, "bishop");
		addPiece(6, 0, false, "knight");
		addPiece(7, 0, false, "rook");

        for (int col = 0; col < columns; col++) {
            addPiece(col, 1, false, "pawn");
        }

        // Białe figury
        addPiece(0, 7, true, "rook");
		addPiece(1, 7, true, "knight");
		addPiece(2, 7, true, "bishop");
		addPiece(3, 7, true, "queen");
		addPiece(4, 7, true, "king");
		addPiece(5, 7, true, "bishop");
		addPiece(6, 7, true, "knight");
		addPiece(7, 7, true, "rook");

        for (int col = 0; col < columns; col++) {
            addPiece(col, 6, true, "pawn");
        }
    }

	public void addPiece(int col, int row, boolean isWhite, String name)
	{
		Piece piece = new Piece(col, row, isWhite, name, 1);
		ImageView pieceView = piece.getImageView();

		pieceView.setX(col * tileSize);
		pieceView.setY(row * tileSize);
		getChildren().add(pieceView);
	}
}


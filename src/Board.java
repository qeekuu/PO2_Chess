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
	private double tileSize = 120;

	public Board()
	{
		drawBoard();	
		addPiece(0, 1, true, "pawn");
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
					tile.setFill(Color.GREY);
				else
					tile.setFill(Color.WHITESMOKE);
				getChildren().add(tile);
			}
		}
		setPrefSize(columns * tileSize, rows * tileSize);
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


package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import main.Main;
import main.Board;

/**
 *	Klasa abstrakcyjna reprezentująca figury szachowe.
 */


public abstract class Piece 
{
    public int col, row, preCol, preRow;
    public boolean isWhite;
    public int value;
	public Type pieceType;
	public PieceColor pieceColor;

	protected Board board;
	protected boolean hasMoved = false;

    private Image spriteSheet;
    private ImageView imageView;

    public Piece(PieceColor pieceColor, int col, int row, Type pieceType, Board board) 
	{
		// this.color = color;
		this.col = col;
		this.row = row;
		this.pieceType = pieceType;
		this.pieceColor = pieceColor;
		preCol = col;
		preRow = row;
		this.board = board;

        loadSprite();
		updateSprite();
	}

	/**
	 * Gettery, settery.
	 */
	public int getColumn()
	{
		return col;
	}

	public void setColumn(int col)
	{
		System.out.println("Updating column: previous = " + this.col + ", new = " + col);
		preCol = this.col;
		this.col = col;
	}

	public int getRow()
	{
		return row;
	}

	public void setRow(int row)
	{
		System.out.println("Updating row: previous = " + this.row + ", new = " + row);
		preRow = this.row;
		this.row = row;
	}

	public int getX(int col)
	{
		return col * 80;
	}

	public int getY(int row)
	{
		return row * 80;
	}
	
	public Type getType()
	{
		return pieceType;
	}
	
	public void setType(Type pieceType)
	{
		this.pieceType = pieceType;
		updateSprite();
	}
	
	public PieceColor getColor()
	{
		return pieceColor;
	}

	public void setColor(PieceColor pieceColor)
	{
		this.pieceColor = pieceColor;
		updateSprite();
	}
	
	// abstract method for moving pieces
	/**
	 * Abstrakcyjna metoda realizująca możliwość ruchu.
	 * @param selectedPiecePreCol kolumna, na której znajdowała się figura.
	 * @param selectedPiecePreRow wiersz, na którym znajdowała się figura.
	 * @param targetCol kolumna docelowa.
	 * @param targetRow wiersz docelowy.
	 */
	public abstract boolean canMove(int selectedPiecePreCol, int selectedPiecePreRow, int targetCol, int targetRow);

	/**
	 * Abstrakcyjna metoda realizująca możliwość ataku.
	 * @param selectedPiecePreCol kolumna, na której znajdowała się figura.
	 * @param selectedPiecePreRow wiersz, na którym znajdowała się figura.
	 * @param targetCol kolumna docelowa.
	 * @param targetRow wiersz docelowy.
	 */
	public abstract boolean canAttack(int selectedPiecePreCol, int selectedPiecePreRow, int targetCol, int targetRow);

	/**
	 * Metoda sprawdzająca czy pionek znajduje się na polu, z którego moze dokonać promoci.
	 * @return true jeśli tak, false jeślie nie.
	 */
	public boolean canPromote()
	{
		if(pieceType == Type.PAWN)
		{
			if(pieceColor == PieceColor.WHITE && row == 0 || pieceColor == PieceColor.BLACK && row == 7)
				return true;
		}

		return false;
	}

	/**
	 * Metoda zajmująca się oknem promocji jeśli canPromote() zwraca turue.
	 */
	public void handlePromotion()
	{
		if(canPromote())
		{
			System.out.println("Promotion available. Promotnig pawn...");
			// board.removePiece(col, row);
			// board.addPiece(col, row, Type.QUEEN, pieceColor);

			VBox promotionOptions = new VBox(10);
			promotionOptions.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
			promotionOptions.setAlignment(Pos.CENTER);
			
			Text promotionText = new Text("Choose a piece for promotion: ");
			promotionText.setFill(Color.WHITE);

			Button queenButton = new Button("Queen");
			Button rookButton = new Button("Rook");
			Button bishopButton = new Button("Bishop");
			Button knightButton = new Button("Knight");

			//Styl przyciskó
			queenButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
			rookButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
			bishopButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
			knightButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");	

			//Zdarzenia przycisków
			queenButton.setOnAction(e -> {
				promoteTo(Type.QUEEN);
				removePromotionOptions(promotionOptions);
			});

			rookButton.setOnAction(e -> {
				promoteTo(Type.ROOK);
				removePromotionOptions(promotionOptions);
			});

			bishopButton.setOnAction(e -> {
				promoteTo(Type.BISHOP);
				removePromotionOptions(promotionOptions);
			});

			knightButton.setOnAction(e -> {
				promoteTo(Type.KNIGHT);
				removePromotionOptions(promotionOptions);
			});

			promotionOptions.getChildren().addAll(promotionText, queenButton, rookButton, bishopButton, knightButton);
			
			// dodanie do sceny
			StackPane root = (StackPane) board.getScene().getRoot();
			root.getChildren().add(promotionOptions);
		}
	}

	/**
	 * Metoda usuwająca opcje promocji po wyborze.
	 */
	private void removePromotionOptions(VBox promotionOptions) 
	{
		StackPane root = (StackPane) board.getScene().getRoot();
		root.getChildren().remove(promotionOptions);
	}
	
	/**
	 * Metoda realizująca promocję.
	 * @newType nowy typ figury (promocja).
	 */
	private void promoteTo(Type newType) 
	{
		board.removePiece(col, row);
		board.addPiece(col, row, newType, pieceColor);
		System.out.println("Pawn promoted to " + newType);
	}

	/**
	 * Metoda sprawdzająca, czy figura się ruszyła.
	 * @param preCol poprzednia kolumna, na której znajdowałą się figura.
	 * @param preRow poprzedni wiersz, na którym znajdowałą się figura.
	 * @param col jeśli nastąpił ruch to przyjmuje wartość nowej kolumny, jeśli nie pozostaje niezmienony.
	 * @param row jeśli nastąpił ruch to przyjmuje wartość nowej kolumny, jeśli nie pozostaje niezmienony.
	 * @return true, jeśli tak, false w przeciwnym.
	 */
	public boolean moved(int preCol, int preRow, int col, int row)
	{
		if(preCol != col || preRow != row)
		{
			hasMoved = true;
			return true;
		}
		return false;
	}

	public boolean getHasMoved()
	{
		return hasMoved;
	}

	/**
	 * Metoda sprawdzająca, czy ruch jest w obrębie szachownicy.
	 * @param targetRow kolumna docelowa.
	 * @param targetRow wirsz docelowy.
	 * @return true, jeśli w obrębie, false jeśli nie.
	 */
	public boolean isWithinBoard(int targetCol, int targetRow)
	{
		if(targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7)
		{
			return true;
		}
		return false;
	}

	/**
	 * Metoda sprawdzjąca czy na pozycji nie znajduje się bierka o takim samym kolorze.
	 * Wykorzystuje metodę z klasy board isSquareQccupied.
	 * @param targetCol kolumna docelowa.
	 * @param targetRow wirsz coelowy.
	 * @return false, jeśli nie, true jeśli tak.
	 */
	public boolean isTheSamePieceColor(int targetCol, int targetRow)
	{
		if((board.isSquareQccupied(targetCol, targetRow)) && (board.getPiece(targetCol, targetRow).getColor().equals(this.pieceColor)))
				return false;
		return true;
	}

	public boolean isWhitePieceColor(int targetCol, int targetRow)
	{
		if((board.isSquareQccupied(targetCol, targetRow)) && (board.getPiece(targetCol, targetRow).getColor().equals(PieceColor.WHITE)))
			return false;
		return true;
	}

	public boolean isBlackPieceColor(int targetCol, int targetRow)
	{
		if((board.isSquareQccupied(targetCol, targetRow)) && (board.getPiece(targetCol, targetRow).getColor().equals(PieceColor.BLACK)))
			return false;
		return true;
	}

	/**
	 * Metoda sprawdzjąca, czy żadna figura nie stoi na przeszkodzie w pionie i w poziomie.
	 * @param targetCol kolumna docelowa.
	 * @param targetRow wiersz docelowy.
	 * @param preCol poprzednia kolumna.
	 * @param preRow poprzedni wiersz.
	 * @return false, jeśli nie, true jeśli tak.
	 */
	public boolean isOnVertivalOrHorizontalLine(int targetCol, int targetRow, int preCol, int preRow)
	{
		// HORIZONTAL
		if(preRow == targetRow)
		{
			int firstCol = Math.min(preCol, targetCol);
			int lastCol = Math.max(preCol, targetCol);
			for(int col = firstCol + 1; col < lastCol; col++)
				if(board.isSquareQccupied(col, preRow))
					return false;
		}

		//VERTICAL
		else if(preCol == targetCol)
		{
			int firstRow = Math.min(preRow, targetRow);
			int lastRow = Math.max(preRow, targetRow);
			for(int row = firstRow + 1; row < lastRow; row++)
				if(board.isSquareQccupied(preCol, row))
					return false;
		}
		return true;
	}

	/**
	 * Metoda sprawdzjąca, czy żadna figura nie stoi na przeszkodzie w lini diagonalnej.
	 * @param targetCol kolumna docelowa.
	 * @param targetRow wiersz docelowy.
	 * @param preCol poprzednia kolumna.
	 * @param preRow poprzedni wiersz.
	 * @return false, jeśli nie, true jeśli tak.
	 */
	
	public boolean isOnDiagonalLine(int targetCol, int targetRow, int preCol, int preRow) 
	{
		for(int i = 1; i < Math.abs(targetCol - preCol); i++)
		{
			int col = preCol + (targetCol > preCol ? i : -i);
			int row = preRow + (targetRow > preRow ? i : -i);

			if(board.isSquareQccupied(col, row))
				return false;
		}
		return true;
	}

	/**
	 * Metoda wczytująca sprite figur.
	 */
    private void loadSprite() {
        String filePath = "/pieces160x480.png";
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        imageView = new ImageView(spriteSheet);
    }

	/**
	 * Metoda dzieląca wczytany sprite na pojedyncze figury.
	 */
	public void updateSprite()
	{
		int spriteWidth = 80;
		int spriteHeight = 80;

		int spriteX;
		switch(getType())
		{
			case PAWN:
				spriteX = 5 * spriteWidth;
				break;
			case ROOK:
				spriteX = 4 * spriteWidth;
				break;
			case KNIGHT:
				spriteX = 3 * spriteWidth;
				break;
			case BISHOP:
				spriteX = 2 * spriteWidth;
				break;
			case QUEEN:
				spriteX = 1 * spriteWidth;
				break;
			case KING:
			default:
				spriteX = 0;
					break;
		}

		// int spriteY = (getColor() == PieceColor.WHITE) ? 0 : spriteHeight;
		int spriteY = (pieceColor == PieceColor.WHITE) ? 0 : spriteHeight;
		imageView.setViewport(new Rectangle2D(spriteX, spriteY, spriteWidth, spriteHeight));
	}

    public ImageView getImageView() {
        return imageView;
    }
}


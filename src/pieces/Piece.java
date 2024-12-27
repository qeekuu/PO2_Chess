package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import main.Main;
import main.Board;

/**
 *	Absract class for pieces
 *
 *
 * */


public abstract class Piece 
{
    public int col, row, preCol, preRow;
    public boolean isWhite;
    public int value;
	public Type pieceType;
	public PieceColor pieceColor;

	protected Board board;

    private Image spriteSheet;
    private ImageView imageView;
	private String color;
	private int x;
	private int y;

    public Piece(PieceColor pieceColor, int col, int row, Type pieceType, Board board) 
	{
		// this.color = color;
		this.col = col;
		this.row = row;
		this.pieceType = pieceType;
		this.pieceColor = pieceColor;
		x = getX(col);
		y = getY(row);
		preCol = col;
		preRow = row;
		this.board = board;

        loadSprite();
		updateSprite();
	}
	
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
	public abstract boolean canMove(int selectedPiecePreCol, int selectedPiecePreRow, int targetCol, int targetRow);

	public boolean isWithinBoard(int targetCol, int targetRow)
	{
		if(targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7)
		{
			return true;
		}
		return false;
	}
	
	public boolean isTheSamePieceColor(int targetCol, int targetRow)
	{
		if((board.isSquareQccupied(targetCol, targetRow)) && (board.getPiece(targetCol, targetRow).getColor().equals(this.pieceColor)))
				return false;
		return true;
	}
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

	public boolean isOnDiagonalLine(int targetCol, int targetRow, int preCol, int preRow) 
	{
		if(Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow))
		{

		}
		return true;
	}
	
    private void loadSprite() {
        String filePath = "/resources/pieces160x480.png";
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        imageView = new ImageView(spriteSheet);
    }

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


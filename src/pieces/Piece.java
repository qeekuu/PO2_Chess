package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import main.Main;

/**
 *	Absract class for pieces
 *
 *
 * */


public abstract class Piece 
{
    public int col, row, preCol, preRow;
    public boolean isWhite;
    public String name;
    public int value;
	public Type pieceType;
	public PieceColor pieceColor;

    private Image spriteSheet;
    private ImageView imageView;
	private String color;
	private int x;
	private int y;

    public Piece(PieceColor pieceColor, int col, int row, Type pieceType) 
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

        loadSprite();
		updateSprite();
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


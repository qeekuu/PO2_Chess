package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;


public class Piece 
{
    public int column, row, preCol, preRow;
    public boolean isWhite;
    public String name;
    public int value;
	public int x, y;
	public Type pieceType;
	public PieceColor pieceColor;

    private Image spriteSheet;
    private ImageView imageView;

    public Piece(int column, int row, boolean isWhite, String name, int value) 
	{
        this.column = column;
        this.row = row;
        this.isWhite = isWhite;
        this.name = name;
        this.value = value;
		this.pieceColor = isWhite ? PieceColor.WHITE : PieceColor.BLACK;
		x = getX(column);
		y = getY(row);
		preCol = column;
		preRow = row;
        loadSprite();
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

	public Piece getPiece(Type type, PieceColor color)
	{
		switch(type)
		{
			case PAWN:
				return new Pawn(color);
			case KNIGHT:
				return new Knight(color);
			case BISHOP:
				return new Bishop(color);
			case ROOK:
				return new Rook(color);
			case QUEEN:
				return new Queen(color);
			default:
				return new King(color);
		}
	}

    private void loadSprite() {
        String filePath = "/resources/pieces160x480.png";
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        imageView = new ImageView(spriteSheet);
		updateSprite();		
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

		int spriteY = (getColor() == PieceColor.WHITE) ? 0 : spriteHeight;
		imageView.setViewport(new Rectangle2D(spriteX, spriteY, spriteWidth, spriteHeight));
	}

    public ImageView getImageView() {
        return imageView;
    }
}


package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

enum Type
{
	PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
}

enum Color
{
	WHITE, BLACK
}

public class Piece {
    public int column, row, preCol, preRow;
    public boolean isWhite;
    public String name;
    public int value;
	public int x, y;
	public Type pieceType;
	public Color pieceColor;

    private Image spriteSheet;
    private ImageView imageView;

    public Piece(int column, int row, boolean isWhite, String name, int value) {
        this.column = column;
        this.row = row;
        this.isWhite = isWhite;
        this.name = name;
        this.value = value;
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
	}
	
	public Color getColor()
	{
		return pieceColor;
	}

	public void setColor(Color pieceColor)
	{
		this.pieceColor = pieceColor;
	}

	public Piece getPiece(Type type, Color color)
	{
		switch(type)
		{
			case PAWN:
				return new Pawn(color);
			case KNIGHT:
				return new KNIGHT(color);
			case BISHOP:
				return new BISHOP(color);
			case ROOK:
				return new ROOK(color);
			case QUEEN:
				return new QUEEN(color);
			default:
				return new KING(color);
		}
	}

    private void loadSprite() {
        String filePath = "/resources/pieces160x480.png";
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));

        int spriteWidth = 80;
        int spriteHeight = 80;

        imageView = new ImageView(spriteSheet);
		
		int spriteX = 0;
		if(name.equals("pawn"))
			spriteX = 5 * spriteWidth;
		else if(name.equals("rook"))
			spriteX = 4 * spriteWidth;
		else if(name.equals("knight"))
			spriteX = 3 * spriteWidth;
		else if(name.equals("bishop"))
			spriteX = 2 * spriteWidth;
		else if(name.equals("queen"))
			spriteX = 1 * spriteWidth;
		else if(name.equals("king"))
			spriteX = 0 * spriteWidth;
		
		int spriteY = isWhite ? 0 : spriteHeight; // y koordynat 0 jesli biala, 80 jesli czarna
		
		imageView.setViewport(new Rectangle2D(spriteX, spriteY, spriteWidth, spriteHeight));
    }

    public ImageView getImageView() {
        return imageView;
    }
}


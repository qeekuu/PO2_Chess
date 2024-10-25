package pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece
{
	public int column;
	public int row;
	public int posX;
	public int posY;
	public boolean isWhite;
	public String name;
	public int value;

	private Image spriteSheet;
	private ImageView imageView;

	public Piece(int column, int row, boolean isWhite, String name, int value)
	{
		this.column = column;
		this.row = row;
		this.isWhite = isWhite;
		this.name = name;
		this.value = value;

		loadSprite();
	}

	private void loadSprite()
	{
		String filePath = "resources/pieces.png";
		this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
		
		int spriteWidth = 64;
		int spriteHeight = 64;
		int xOffset = isWhite ? 0 : spriteWidth;
		int yOffset = 0;

		if(name.equals("pawn"))
			xOffset += 0;
		else if(name.equals("rook"))
			xOffset += spriteWidth;
		// reszta figur

		imageView = new ImageView(spriteSheet);
		imageView.setViewport(new javafx.geometry.Rectangle2D(xOffset, yOffset, spriteWidth, spriteHeight));
	}

	public ImageView getImageView()
	{
		return imageView;
	}
}

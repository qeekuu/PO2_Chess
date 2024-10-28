package pieces;

public class Knight extends Piece
{
	Knight(PieceColor pieceColor)
	{
		super(3, 7, pieceColor == PieceColor.WHITE, "Knight", 1);

		setType(Type.KNIGHT);
		setColor(pieceColor);
	}
}

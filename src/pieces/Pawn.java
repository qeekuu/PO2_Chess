package pieces;

public class Pawn extends Piece
{
	public Pawn(String color, int col, int row)
	{
		super(color, col, row);
		setType(Type.PAWN);
		setColor(pieceColor);
	}
}

package pieces;

public class Pawn extends Piece
{
	Pawn(PieceColor pieceColor)
	{
		setType(Type.PAWN);
		setColor(pieceColor);
	}
}

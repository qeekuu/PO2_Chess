package pieces;

public class King extends Piece
{
	public King(PieceColor pieceColor, int col, int row)
	{
		super(pieceColor, col, row, Type.KING);
		this.preCol = col;
		this.preRow = row;
	}

	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow) 
	{
		System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");

		if (isWithinBoard(targetCol, targetRow)) 
		{
			if (Math.abs(targetCol - preCol) <= 1 && Math.abs(targetRow - preRow) <= 1) 
			{
				System.out.println("Move allowed.");
				return true;
			} 
			else 
			{				
				System.out.println("Move invalid: Too far.");
			}
		} 
		else 
		{
			System.out.println("Move invalid: Out of bounds.");
		}
		return false;
	}		
}

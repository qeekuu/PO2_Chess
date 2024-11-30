package pieces;

public class Knight extends Piece
{
	public Knight(PieceColor pieceColor, int col, int row)
	{
		super(pieceColor, col, row, Type.KNIGHT);
		this.preCol = col;
		this.preRow = row;
	}

	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
	{
		System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


		if(isWithinBoard(targetCol, targetRow))
		{
			// 1:2 or 2:1
			if(Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2)
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

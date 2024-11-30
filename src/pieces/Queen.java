package pieces;

public class Queen extends Piece
{
	public Queen(PieceColor pieceColor, int col, int row)
	{
		super(pieceColor, col, row, Type.QUEEN);
	}

	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
		{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


			if(isWithinBoard(targetCol, targetRow))
			{
				
				if((targetCol == preCol || targetRow == preRow) || (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)))
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

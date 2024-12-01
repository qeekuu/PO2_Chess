package pieces;

import main.Board;

public class Rook extends Piece
{
	public Rook(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.ROOK, board);
	}

	@Override
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
		{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


			if(isWithinBoard(targetCol, targetRow))
			{
				
				if(targetCol == preCol || targetRow == preRow)
				{
					System.out.println("Move allowed.");	
					return isOnVertivalOrHorizontalLine(targetCol, targetRow, preCol, preRow);
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

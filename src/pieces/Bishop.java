package pieces;

import main.Board;

public class Bishop extends Piece
{
	public Bishop(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.BISHOP, board);
	}

	@Override
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
		{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


			if(isWithinBoard(targetCol, targetRow))
			{
				
				if((Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) && (isTheSamePieceColor(targetCol, targetRow)))
				{
					System.out.println("Move allowed.");	
					return isOnDiagonalLine(targetCol, targetRow, preCol, preRow);
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

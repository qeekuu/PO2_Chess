package pieces;

import main.Board;

public class Queen extends Piece
{
	public Queen(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.QUEEN, board);
	}
	
	@Override
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
		{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


			if(isWithinBoard(targetCol, targetRow))
			{
				
				if(((targetCol == preCol || targetRow == preRow) || (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow))) 
						&& (isTheSamePieceColor(targetCol, targetRow)) && (isOnVertivalOrHorizontalLine(targetCol, targetRow, preCol, preRow)) 
						&& (isOnDiagonalLine(targetCol, targetRow, preCol, preRow)))
				{
					if(!board.tryMovePiece(this, targetCol, targetRow))
					{
						System.out.println("King is under attack!");
						return false;
					}
					System.out.println("Move allowed.");	
					// return isOnVertivalOrHorizontalLine(targetCol, targetRow, preCol, preRow) && isOnDiagonalLine(targetCol, targetRow, preCol, preRow);
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

	@Override
	public boolean canAttack(int preCol, int preRow, int targetCol, int targetRow) 
	{
		if (!isWithinBoard(targetCol, targetRow)) 
			return false;

			if(((targetCol == preCol || targetRow == preRow) || (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)))
					&& (isTheSamePieceColor(targetCol, targetRow)))
				return isOnVertivalOrHorizontalLine(targetCol, targetRow, preCol, preRow) && isOnDiagonalLine(targetCol, targetRow, preCol, preRow);
	

		return false;
	}


}

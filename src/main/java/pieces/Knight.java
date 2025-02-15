package pieces;

import main.Board;

public class Knight extends Piece
{
	public Knight(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.KNIGHT, board);
		this.preCol = col;
		this.preRow = row;
	}

	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
	{
		System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


		if(isWithinBoard(targetCol, targetRow))
		{
			if((Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				if(!board.tryMovePiece(this, targetCol, targetRow))
				{
					System.out.println("King is under attack!");
					return false;
				}
	
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

	@Override
	public boolean canAttack(int preCol, int preRow, int targetCol, int targetRow)
	{
		if(!isWithinBoard(targetCol, targetRow))
			return false;
		if((Math.abs(targetCol - preCol) * Math.abs(targetRow - preRow) == 2) && (isTheSamePieceColor(targetCol, targetRow)))
			return true;

		return false;
	}
}

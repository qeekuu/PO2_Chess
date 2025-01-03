package pieces;

import main.Board;

public class King extends Piece
{
	public King(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.KING, board);
		this.preCol = col;
		this.preRow = row;
	}

	@Override
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow) 
	{
		System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");

		if (isWithinBoard(targetCol, targetRow)) 
		{
			if ((Math.abs(targetCol - preCol) <= 1 && Math.abs(targetRow - preRow) <= 1) && (isTheSamePieceColor(targetCol, targetRow))) 
			{
				System.out.println("Move allowed.");
				board.removePiece(targetCol, targetRow);
				moved(preCol, preRow, targetCol, targetRow);
				return true;
			}
			else if((Math.abs(targetCol - preCol) == 3) && (Math.abs(targetRow - preRow) == 0))
			{
				if(!hasMoved)
				{
					Piece rook;

					moved(preCol, preRow, targetCol, targetRow); // brak możliwego ruchu takiego jak w warunku po roszadzie
					if(board.isSquareQccupied(5, targetRow) && (board.isSquareQccupied(6, targetRow)))
						return false;

					if(pieceColor == PieceColor.WHITE && row == 7)
					{
						System.out.println("Kingside castling, move allowed.");
						return true;
					}
					if(pieceColor == PieceColor.BLACK && row == 0)
					{
						System.out.println("Kingside castling, move allowed.");
						return true;
					}

				}
				else 
				{				
					System.out.println("Move invalid: Too far.");
				}
			}
		}
		else 
		{
			System.out.println("Move invalid: Out of bounds.");
		}
		return false;
	}		
}

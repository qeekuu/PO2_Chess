package pieces;

import main.Board;

public class Pawn extends Piece
{
	public Pawn(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.PAWN, board);
	}
	
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
		{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


		if (isWithinBoard(targetCol, targetRow))
		{
			if ((Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				if (pieceColor == PieceColor.WHITE && targetRow < preRow)
				{
					System.out.println("Move allowed.");
					return true;
				}
				else if (pieceColor == PieceColor.BLACK && targetRow > preRow)
				{
					System.out.println("Move allowed.");
					return true;
				}
				else
				{
					System.out.println("Move invalid: Pionek nie może się cofnąć.");
				}
			}
			else if((Math.abs(targetCol - preCol) == 0) && (Math.abs(targetRow - preRow) == 2) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				if(pieceColor == PieceColor.WHITE && row == 6 && targetRow < preRow)
					return true;
				
				else if(pieceColor == PieceColor.BLACK && row == 1 && targetRow > preRow)
					return true;

			}
			else if((Math.abs(targetCol - preCol) == 1) && (Math.abs(targetRow - preRow) == 1) && (board.isSquareQccupied(targetCol, targetRow)) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				System.out.println("Movle allowed - capturing the piece.");
				board.removePiece(targetCol, targetRow);
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

package pieces;

import main.Board;

public class Pawn extends Piece
{
	private boolean enPassant = false;
	public Pawn(PieceColor pieceColor, int col, int row, Board board)
	{
		super(pieceColor, col, row, Type.PAWN, board);
	}
	
	public boolean canMove(int preCol, int preRow, int targetCol, int targetRow)
	{
			System.out.println("Attempting move from (" + preCol + ", " + preRow + ") to (" + targetCol + ", " + targetRow + ")");


		if (isWithinBoard(targetCol, targetRow))
		{
			// o jedno pole
			if ((Math.abs(targetCol - preCol) + Math.abs(targetRow - preRow) == 1) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				// isBlackPieceColor, isWhitePieceColor - brak wchodzenia na pionki
				if (pieceColor == PieceColor.WHITE && targetRow < preRow && (isBlackPieceColor(targetCol, targetRow)))
				{
					System.out.println("Move allowed.");
					return true;
				}
				else if (pieceColor == PieceColor.BLACK && targetRow > preRow && (isWhitePieceColor(targetCol, targetRow)))
				{
					System.out.println("Move allowed.");
					return true;
				}
				else
				{
					System.out.println("Move invalid.");
				}
			}
			// o dwa pola
			else if((Math.abs(targetCol - preCol) == 0) && (Math.abs(targetRow - preRow) == 2) && (isTheSamePieceColor(targetCol, targetRow)))
			{
				if(pieceColor == PieceColor.WHITE && row == 6 && targetRow < preRow && (isBlackPieceColor(targetCol, targetRow)))
				{
					setEnPassant(true);
					return true;
				}
				
				else if(pieceColor == PieceColor.BLACK && row == 1 && targetRow > preRow && (isWhitePieceColor(targetCol, targetRow)))
				{
					setEnPassant(true);
					return true;
				}

			}
			// bicie p oprzekątnej
			else if((Math.abs(targetCol - preCol) == 1) && (Math.abs(targetRow - preRow) == 1) && (board.isSquareQccupied(targetCol, targetRow))
					&& (isTheSamePieceColor(targetCol, targetRow)))
			{
				board.removePiece(targetCol, targetRow);
				return true;
			}
			// En passant
			else if((Math.abs(targetCol - preCol) == 1) && (Math.abs(targetRow - preRow) == 1) && (!board.isSquareQccupied(targetCol, targetRow)))
			{
				int enPassantRow;
				if(pieceColor == PieceColor.WHITE)
					enPassantRow = targetRow + 1; // dla białych pionek jest poniżej kafelka ataku
				else
					enPassantRow = targetRow - 1; // jest nad

				Piece targetPwan = board.getPiece(targetCol, enPassantRow);

				if(targetPwan != null && targetPwan instanceof Pawn)
				{
					Pawn possiblePawn = (Pawn) targetPwan; // rzutowanie do pionka

					if(possiblePawn.isEnPassant())
					{
						board.removePiece(targetCol, enPassantRow);
						System.out.println("En passant caputre allowed.");
						return true;
					}
				}
				else
				{
					System.out.println("En Passant not allowed.");
				}
			}
        else
        {
            System.out.println("Move invalid.");
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
		if((Math.abs(targetCol - preCol) == 1) && (Math.abs(targetRow - preRow) == 1) && (isTheSamePieceColor(targetCol, targetRow)))
			return true;
		return false;
	}

	public boolean isEnPassant()
	{
		return enPassant;
	}

	public void setEnPassant(boolean enPassant)
	{
		this.enPassant = enPassant;
	}

}

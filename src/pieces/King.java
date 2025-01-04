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
					Piece rook = board.getPiece(7, targetRow); // pobór wieży
					if(rook == null || rook.getHasMoved())
					{
						System.out.println("Castling invalid: Rook moved or does not exist.");
						return false;
					}
					if (!(rook instanceof Rook)) 
					{
						System.out.println("Invalid piece: Not a rook at the expected position.");
						return false;
					}
					
					
					if(board.isSquareQccupied(5, targetRow) || (board.isSquareQccupied(6, targetRow)))
						return false;
					if(board.isUnderAttack(5, targetRow) || (board.isUnderAttack(6, targetRow)))
						return false;

					moved(preCol, preRow, targetCol, targetRow); // brak możliwego ruchu takiego jak w warunku po roszadzie
					int rookTargetCol = 5;
					System.out.println("ROOK:");
					rook.setColumn(rookTargetCol);
					rook.setRow(targetRow);
					rook.getImageView().setX(rookTargetCol * board.getTileSize());
					rook.getImageView().setY(targetRow * board.getTileSize());
					
					// aktualizacja pozycja dla króla
					// Piece king = board.getPiece(4, targetRow);
					// int kingTargetCol = 6;
					// System.out.println("KING");
					// king.setColumn(kingTargetCol);
					// king.setRow(targetRow);
					// king.getImageView().setX(kingTargetCol * board.getTileSize());
					// king.getImageView().setY(targetRow * board.getTileSize());
					System.out.println("Kingside castling allowed.");
					return true;

				}
				else 
				{				
					System.out.println("Move invalid: Too far.");
				}
			}
			else if((Math.abs(targetCol * preCol) == 0) && (Math.abs(targetRow - preRow) == 0))
			{
				if(!hasMoved)
				{
					Piece rook = board.getPiece(0, targetRow); // pobór wieży
					if(rook == null || rook.getHasMoved())
					{
						System.out.println("Castling invalid: Rook moved or does not exist.");
						return false;
					}
					if(!(rook instanceof Rook))
					{
						System.out.println("Invalid piece: Not a rook at the expected position.");
						return false;
					}

					if(board.isSquareQccupied(1, targetRow) && (board.isSquareQccupied(2, targetRow)) && (board.isSquareQccupied(3, targetRow)))
						return false;

					moved(preCol, preRow, targetCol, targetRow);
					int rookTargetCol = 3;
					rook.setColumn(rookTargetCol);
					rook.setRow(targetRow);
					rook.getImageView().setX(rookTargetCol * board.getTileSize());
					rook.getImageView().setY(targetRow * board.getTileSize());
					System.out.println("Queenside castling allowed.");
					return true;
				}
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
		return false;
	}
}

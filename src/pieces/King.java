package pieces;

import main.Board;
import database.*;

public class King extends Piece
{
	private boolean justCastled = false;
	private boolean isCheck = false;
	private MoveDataBaseSaveReading moveDB = new MoveDataBaseSaveReading();
	private int currentGameId = 1;

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
			// ruch
			if ((Math.abs(targetCol - preCol) <= 1 && Math.abs(targetRow - preRow) <= 1) && (isTheSamePieceColor(targetCol, targetRow))) 
			{
				// szach
				if(board.isUnderAttack(targetCol, targetRow, pieceColor, pieceType))
				{
					System.out.println("King cannot move into check.");
					return false;
				}
				if(board.isKingInCheck(this.pieceColor))
				{
					if(!board.isUnderAttack(targetCol, targetRow, pieceColor, pieceType))
						return true;
					System.out.println("Check");
					return false;
				}

					System.out.println("Move allowed.");
					board.removePiece(targetCol, targetRow);
					moved(preCol, preRow, targetCol, targetRow);
					return true;
			}
			// roszada
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

					if(board.isUnderAttack(5, targetRow, this.pieceColor, this.pieceType) || (board.isUnderAttack(6, targetRow, this.pieceColor, this.pieceType)))
						return false;

					this.setColumn(6);
					this.setRow(preRow);
					this.getImageView().setX(6 * board.getTileSize());
					this.getImageView().setY(preRow * board.getTileSize());

					int rookTargetCol = 5;
					System.out.println("ROOK:");
					rook.setColumn(rookTargetCol);
					rook.setRow(targetRow);
					rook.getImageView().setX(rookTargetCol * board.getTileSize());
					rook.getImageView().setY(targetRow * board.getTileSize());
					
					this.moved(preCol, preRow, 6, targetRow);
					rook.moved(7, preRow, 5, targetRow);

					// Zapis ruchu do bazy
					moveDB.saveMove(new Move(currentGameId,rook.getType().toString(),rook.getColor().toString(),rook.preCol,rook.preRow,rookTargetCol,targetRow));

					justCastled = true;
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

					if(board.isSquareQccupied(1, targetRow) || (board.isSquareQccupied(2, targetRow)) || (board.isSquareQccupied(3, targetRow)))
						return false;
					if(board.isUnderAttack(1, targetRow, this.pieceColor, this.pieceType) 
							|| (board.isUnderAttack(2, targetRow, this.pieceColor, this.pieceType)) 
							|| (board.isUnderAttack(3, targetRow, this.pieceColor, this.pieceType)))
						return false;
					
					this.setColumn(2);
					this.setRow(preRow);
					this.getImageView().setX(2 * board.getTileSize());
					this.getImageView().setY(preRow * board.getTileSize());

					int rookTargetCol = 3;
					rook.setColumn(rookTargetCol);
					rook.setRow(targetRow);
					rook.getImageView().setX(rookTargetCol * board.getTileSize());
					rook.getImageView().setY(targetRow * board.getTileSize());

					this.moved(preCol, preRow, 2, targetRow);
					rook.moved(0, preRow, 3, targetRow);

					justCastled = true;
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
		if(!isWithinBoard(targetCol, targetRow))
			return false;
		if((Math.abs(targetCol - preCol) <= 1 && Math.abs(targetRow - preRow) <= 1) && (isTheSamePieceColor(targetCol, targetRow)))
			return true;

		return false;
	}

	public boolean hasJustCastled()
	{
		return justCastled;
	}
}

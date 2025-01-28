package database;

import java.sql.Timestamp;

public class Move 
{
    private int id;
    private int gameId;
    private String pieceType;
    private String pieceColor;
    private int startCol;
    private int startRow;
    private int endCol;
    private int endRow;
    private Timestamp moveTime;


    //Konstruktor dla zapisu ruchu
    public Move(int gameId, String pieceType, String pieceColor, int startCol, int startRow, int endCol, int endRow) 
    {
    	this.id = id;
        this.gameId = gameId;
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.startCol = startCol;
        this.startRow = startRow;
        this.endCol = endCol;
        this.endRow = endRow;
    }

    //Konstruktor dla odczytu ruchu
    public Move(int id, int gameId, String pieceType, String pieceColor, int startCol, int startRow, int endCol, int endRow, Timestamp moveTime) 
    {
    	this.id = id;
        this.gameId = gameId;
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.startCol = startCol;
        this.startRow = startRow;
        this.endCol = endCol;
        this.endRow = endRow;
        this.moveTime = moveTime;
    }
    //Gettery i Settery
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public int getGameId() 
	{
		return gameId;
	}

	public void setGameId(int gameId) 
	{
		this.gameId = gameId;
	}

	public String getPieceType() 
	{
		return pieceType;
	}

	public void setPieceType(String pieceType) 
	{
		this.pieceType = pieceType;
	}

	public String getPieceColor() 
	{
		return pieceColor;
	}

	public void setPieceColor(String pieceColor) 
	{
		this.pieceColor = pieceColor;
	}

	public int getStartCol() 
	{
		return startCol;
	}

	public void setStartCol(int startCol) 
	{
		this.startCol = startCol;
	}

	public int getStartRow() 
	{
		return startRow;
	}

	public void setStartRow(int startRow) 
	{
		this.startRow = startRow;
	}

	public int getEndCol() 
	{
		return endCol;
	}

	public void setEndCol(int endCol) 
	{
		this.endCol = endCol;
	}

	public int getEndRow() 
	{
		return endRow;
	}

	public void setEndRow(int endRow) 
	{
		this.endRow = endRow;
	}

	public Timestamp getMoveTime() 
	{
		return moveTime;
	}

	public void setMoveTime(Timestamp moveTime) 
	{
		this.moveTime = moveTime;
	}

}

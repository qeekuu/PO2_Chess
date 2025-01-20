package database;

import java.sql.*;

public class MoveDataBaseSaveReading {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "oracle";

    public void saveMove(Move move) {
        String sql = "INSERT INTO Moves (game_id, piece_type, piece_color, start_col, start_row, end_col, end_row) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            // Sprawdzenie załadowania sterownika JDBC
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Sterownik Oracle JDBC załadowany poprawnie.");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, move.getGameId());
                stmt.setString(2, move.getPieceType());
                stmt.setString(3, move.getPieceColor());
                stmt.setInt(4, move.getStartCol());
                stmt.setInt(5, move.getStartRow());
                stmt.setInt(6, move.getEndCol());
                stmt.setInt(7, move.getEndRow());
                
                stmt.executeUpdate();
                System.out.println("Ruch zapisany do bazy danych.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Sterownik Oracle JDBC nie został znaleziony!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Błąd SQL podczas zapisywania ruchu.");
            e.printStackTrace();
        }
    }
}


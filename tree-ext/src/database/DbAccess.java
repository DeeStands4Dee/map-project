package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final String DBMS = "jdbc:mysql";
    private String SERVER = "localhost";
    private String DATABASE = "MapDB";
    private final int PORT = 3306;
    private String USER_ID = "MapUser";
    private String PASSWORD = "map";
    private Connection conn;

    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            conn = DriverManager.getConnection(
                DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE +
                "?user=" + USER_ID + "&password=" + PASSWORD +
                "&serverTimezone=UTC"
            );
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("Driver non trovato: " + e.getMessage());
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connessione fallita: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
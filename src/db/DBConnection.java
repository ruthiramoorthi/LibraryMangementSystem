package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    private static final String URL ="jdbc:mysql://localhost:3306/library_db?useSSl=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Rooth@123";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASS);
    }

}
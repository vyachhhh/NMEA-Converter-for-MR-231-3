package org.example.models;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс ConnectionString нужен для подключаения к базе данных
 */
public class ConnectionString {
    /**
     * Метод getConnection нужен для подключаения к БД
     * @return Подключение посредством JDBC
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        //String URL = "jdbc:sqlserver://MINT;databaseName=NMEA;encrypt=false;";
        //String USER = "vyach";
        //String PASSWORD = "1";
        //return DriverManager.getConnection(URL,USER,PASSWORD);
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=NMEA;" +
                "encrypt=false;" +
                "user=vyach;password=1;");
    }
}

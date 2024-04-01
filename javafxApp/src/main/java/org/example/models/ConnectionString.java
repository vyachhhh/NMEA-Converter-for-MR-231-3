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
        return DriverManager.getConnection("jdbc:sqlserver://172.17.0.2:1433;databaseName=NMEA;" +
                "trustServerCertificate=true;" +
                "user=sa;password=P@ssw0rd;");
    }
}

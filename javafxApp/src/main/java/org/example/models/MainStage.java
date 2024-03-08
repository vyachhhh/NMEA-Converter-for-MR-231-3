package org.example.models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Класс MainStage служит костылем для обновления данных в таблице tableViewNmea при добавлении
 * нового NMEA-сообщения
 */
public class MainStage extends Stage {
    public static Stage mainStage;
    public static void setMainStage(Stage stage){
        mainStage = stage;
    }
    public static void setMainScene(Scene scene){
        mainStage.setTitle("NMEA-Converter");
        mainStage.setScene(scene);
    }
}

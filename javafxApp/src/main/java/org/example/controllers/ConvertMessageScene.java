package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.DbNmeaMessage;
import org.example.models.MainStage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Класс ConvertMessageScene приходится контроллером для resources/convertMessageScene.fxml
 * Он позволяет конвертировать NMEA-сообщение
 */
public class ConvertMessageScene {
    public static Stage convertMessageStage;
    public static void setConvertMessageStage(Stage stage){
        convertMessageStage = stage;
    }
    public static void setConvertMessageScene(Scene scene){
        convertMessageStage.setScene(scene);
        convertMessageStage.setTitle("Convert NMEA-Message");
    }
    @FXML
    private Button btnAddMessage;
    @FXML
    private TextField textFieldMessage;

    /**
     * Метод btnConvertMessageClicked происходит при нажатии на кнопку btnConvertMessage
     * Если элемент textFieldMessage заполнен строкой соответствующей TTM, RSD или VHW,
     * то строка конвертируется и добавляется в базу данных.
     * В противном случае, выводится сообщение о неверности формата строки.
     */
    @FXML
    private void btnConvertMessageClicked(){
        try {
            DbNmeaMessage.addMessage(textFieldMessage.getText());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/mainScene.fxml"));
            Parent root = loader.load();

            // Метод MainStage.setMainScene() здесь необходим для перезагрузки основной сцены,
            // чтобы данные в таблице обновились.
            MainStage.setMainScene(new Scene(root));
            MainStage.mainStage.show();
            convertMessageStage.close();


        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

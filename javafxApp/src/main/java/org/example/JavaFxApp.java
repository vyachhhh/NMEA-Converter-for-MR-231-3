package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.models.MainStage;

import java.io.IOException;

/**
 * Класс JavaFxApp запускает приложение JavaFX
 * В качестве Scene задается resources/mainScene.fxml
 */
public class JavaFxApp extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/mainScene.fxml"));

        Parent root = loader.load();
        MainStage.setMainStage(primaryStage);
        MainStage.setMainScene(new Scene(root));
        MainStage.mainStage.show();

    }

    public static void main(String[] args){
        Application.launch();
    }

}

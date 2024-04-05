package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.models.DbNmeaMessage;
import org.example.models.NmeaMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Класс MainSceneController является контроллером для resources/mainScene.fxml
 * Он загружает данные из базы данных
 */
public class MainSceneController implements Initializable {
    // Инициализация
    // Задача свойств для колонок таблица tableViewNmea
    // Загрузка данных из БД
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewNmea.getSelectionModel().selectedItemProperty().addListener(
                (observableList,oldValue,newValue) -> tableViewNmeaSelectionChanged(newValue));

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
        columnRecTime.setCellValueFactory(new PropertyValueFactory<>("recTime"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        
        loadData();
    }
    @FXML
    private TableView<NmeaMessage> tableViewNmea;
    @FXML
    private TableColumn<NmeaMessage, Integer> columnId;
    @FXML
    private TableColumn<NmeaMessage, String> columnFormat;
    @FXML
    private TableColumn<NmeaMessage, Timestamp> columnRecTime;
    @FXML
    private TableColumn<NmeaMessage, Long> columnTime;
    @FXML
    private TableColumn<NmeaMessage, String> columnMessage;
    @FXML
    private Button btnConvertMsg;
    @FXML
    private Button btnDeleteMsg;
    @FXML
    private AnchorPane anchorPane;
    static List<NmeaMessage> list = new ArrayList<>();
    static ObservableList<NmeaMessage> observableList;
    int selectedIndex = 0;

    /**
     * Метод loadData() загружает данные из БД
     */
    public void loadData() {
        try {
            list = DbNmeaMessage.LOAD_DATA();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        observableList = FXCollections.observableList(list);
        tableViewNmea.setItems(observableList);
    }
    @FXML
    TextField textFieldFormat;

    /**
     * Метод btnConvertMsgClicked происходит при нажатии на кнопку btnConvertMsg
     * Он окрывает новое окно, с возможностью конвертации NMEA-сообщения и добавления его в БД
     */
    @FXML
    private void btnConvertMsgClicked(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/convertMessageScene.fxml"));
            Parent root = loader.load();
            Stage convertMessageStage = new Stage();

            ConvertMessageScene.setConvertMessageStage(convertMessageStage);
            ConvertMessageScene.setConvertMessageScene(new Scene(root));
            ConvertMessageScene.convertMessageStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод btnDeleteMsgClicked происходит при нажатии на кнопку btnDeleteMsg
     * Он удаляет выбранную в таблице строку и заново загружает данные из БД
     * Если строка не выбрана, удаления не произойдет
     * @throws SQLException
     */
    @FXML
    private void btnDeleteMsgClicked() throws SQLException {
        DbNmeaMessage.deleteMessage(list.get(selectedIndex).getId());
        loadData();
        anchorPane.getChildren().setAll();
    }

    /**
     * Метод tableViewNmeaSelecitonChanged обрабатывает изменения в выборе строки таблицы tableViewNmea
     * Загржает anchorPane в соответствии с NMEA-форматом выбранной строки
     * @param message
     */
    @FXML
    private void tableViewNmeaSelectionChanged(NmeaMessage message){
        selectedIndex = tableViewNmea.getSelectionModel().getSelectedIndex();
        try {
            String pathToPane = String.format("/fxml/anchorPane%s.fxml",message.getFormat());
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToPane));
            textFieldFormat.setText(message.getFormat());

            AnchorPane pane = loader.load();
            anchorPane.getChildren().setAll(pane.getChildren());

            AnchorPaneController controller = loader.getController();
            controller.initData(message);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.toString());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}

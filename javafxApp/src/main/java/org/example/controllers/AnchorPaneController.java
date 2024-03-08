package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.models.DbNmeaMessage;
import org.example.models.NmeaMessage;
import org.example.searadar.mr2313.message.Mr2313TrackedTargetMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.WaterSpeedHeadingMessage;

import java.sql.SQLException;

/**
 * Класс AnchorPaneController является контроллером для anchorPaneTTM, anchorPaneRSD и anchorPaneVHW
 * Нужный anchorPane загружается в соотвествии с выбранной строкой на основе колонки
 * FormatName таблицы tableViewNmea класса MainSceneController
 */
public class AnchorPaneController {
    NmeaMessage message;

    /**
     * Метод initData принимает NMEA-сообщение и задает его значение полю message
     * @param message NMEA-сообщение
     */
    public void initData(NmeaMessage message){
        this.message = message;
        getMessageInfo(message.getId());
    }
    @FXML
    private TextField textFieldCourse, textFieldDistance,
            textFieldBearing, textFieldTargetNumber,
            textFieldSpeed, textFieldType, textFieldStatus,
            textFieldIff, textFieldInterval, textFieldInitDistance,
            textFieldInitBearing, textFieldMovingCircle, textFieldDistanceFromShip,
            textFieldBearing2,textFieldDistanceScale,
            textFieldDistanceUnit,textFieldDisplayOrientation,textFieldWorkingMode,
            textFieldCourseAttr,textFieldSpeedUnit;
    private SearadarStationMessage nmeaMessage = new SearadarStationMessage();

    /**
     * Метод getMessageInfo загружает информацию о NMEA-сообщении в соответствии с ID
     * @param id ID выбранного NMEA-сообщения в таблице tableViewNmea класса MainSceneController
     */
    public void getMessageInfo(int id){
        try {
            nmeaMessage = DbNmeaMessage.getMessageInfo(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setTextFieldValues();
    }

    /**
     * Метод setTextFieldValues задает зачения элементам textField в соответствии с NMEA-форматом
     * NMEA-сообщения
     */
    private void setTextFieldValues(){

        switch (message.getFormat()){
            case "TTM":

                textFieldTargetNumber.setText(((Mr2313TrackedTargetMessage) nmeaMessage)
                        .getTargetNumber().toString());
                textFieldDistance.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getDistance().toString());
                textFieldBearing.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getBearing().toString());
                textFieldCourse.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getCourse().toString());
                textFieldSpeed.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getSpeed().toString());
                textFieldType.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getType().toString());
                textFieldStatus.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getStatus().toString());
                textFieldIff.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getIff().toString());
                textFieldInterval.setText(((Mr2313TrackedTargetMessage)nmeaMessage)
                        .getInterval().toString());

                break;

            case "RSD":

                textFieldInitDistance.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getInitialDistance().toString());
                textFieldInitBearing.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getInitialBearing().toString());
                textFieldMovingCircle.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getMovingCircleOfDistance().toString());
                textFieldBearing.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getBearing().toString());
                textFieldDistanceFromShip.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getDistanceFromShip().toString());
                textFieldBearing2.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getBearing2().toString());
                textFieldDistanceScale.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getDistanceScale().toString());
                textFieldDistanceUnit.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getDistanceUnit());
                textFieldDisplayOrientation.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getDisplayOrientation());
                textFieldWorkingMode.setText(((RadarSystemDataMessage)nmeaMessage)
                        .getWorkingMode());

                break;

            case "VHW":

                textFieldCourse.setText(((WaterSpeedHeadingMessage)nmeaMessage)
                        .getCourse().toString());
                textFieldCourseAttr.setText(((WaterSpeedHeadingMessage)nmeaMessage)
                        .getCourseAttr());
                textFieldSpeed.setText(((WaterSpeedHeadingMessage)nmeaMessage)
                        .getSpeed().toString());
                textFieldSpeedUnit.setText(((WaterSpeedHeadingMessage)nmeaMessage)
                        .getSpeedUnit());

                break;

        }

    }

}

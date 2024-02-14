package org.example.searadar.mr2313.convert;

import org.apache.camel.Exchange;
import org.example.searadar.mr2313.message.Mr2313TrackedTargetMessage;
import ru.oogis.searadar.api.convert.SearadarExchangeConverter;
import ru.oogis.searadar.api.message.*;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс Mr2313Converter позволяет конвертировать NMEA-сообщения (TTM и RSD)
 * в массив данных поля fields[]
 */

public class Mr2313Converter implements SearadarExchangeConverter{
    /**
     * Поле хранит шкалу делений дистанции типа Double в виде массива
     */
    private static final Double[] DISTANCE_SCALE = {0.125, 0.25, 0.5, 1.5, 3.0, 6.0, 12.0, 24.0, 48.0, 96.0};
    /**
     * Строковый массив полей NMEA-сообщения
     */
    private String[] fields;
    /**
     * Поле типа NMEA-сообщения
     */
    private String msgType;

    /**
     * Метод преобразования NMEA-предложения типа Exchange в лист сообщений msgList
     * @param exchange NMEA-предложение
     * @return Лист полей и значений NMEA-сообщения
     */
    @Override
    public List<SearadarStationMessage> convert(Exchange exchange) {
        return convert(exchange.getIn().getBody(String.class));
    }

    /**
     * Метод преобразования NMEA-предложения строкового типа в лист сообщений msgList
     * Проверяет соответствие сообщения одному из двух типов
     * TrackedTargetMessage (TTM) / RadarSystemData (RSD)
     * @param message NMEA-предложение
     * @return Лист полей и значений NMEA-сообщения
     */
    public List<SearadarStationMessage> convert(String message) {

        List<SearadarStationMessage> msgList = new ArrayList<>();

        readFields(message);

        switch (msgType) {

            case "TTM" : msgList.add(getTTM());
                break;

            case "RSD" : {

                RadarSystemDataMessage rsd = getRSD();
                InvalidMessage invalidMessage = checkRSD(rsd);

                if (invalidMessage != null)  msgList.add(invalidMessage);
                else msgList.add(rsd);
                break;
            }

        }

        return msgList;
    }

    /**
     * Метод чтения полей NMEA-сообщения
     * Преобразовывает сообщение в массив подстрок, вырезая первые три символа и символы
     * после знака '*', и использует знак запятой ',' как разграничитель
     * @param msg NMEA-сообщение строкового типа
     */
    private void readFields(String msg) {

        String temp = msg.substring( 3, msg.indexOf("*") ).trim();

        fields = temp.split(Pattern.quote(","));
        msgType = fields[0];

    }

    /**
     * Метод получения сообщения TrackedTargetMessage (TTM) из NMEA-сообщения
     * для станции МР-231-3
     * @return Массив полей и значений TTM-сообщения
     */

    private TrackedTargetMessage getTTM() {

        Mr2313TrackedTargetMessage ttm = new Mr2313TrackedTargetMessage();
<<<<<<< HEAD

        Long msgRecTimeMillis = System.currentTimeMillis();
=======
        Long msgRecTimeMillis = System.currentTimeMillis();

>>>>>>> ba72fc1c7ecbedfd8c4dc7efe2f4f40b57f5ea5d
        ttm.setMsgTime(msgRecTimeMillis);
        TargetStatus status = TargetStatus.UNRELIABLE_DATA;
        IFF iff = IFF.UNKNOWN;
        TargetType type = TargetType.UNKNOWN;

        switch (fields[12]) {
            case "L" : status = TargetStatus.LOST;
                break;

            case "Q" : status = TargetStatus.UNRELIABLE_DATA;
                break;

            case "T" : status = TargetStatus.TRACKED;
                break;
        }

        switch (fields[11]) {
            case "b" : iff = IFF.FRIEND;
                break;

            case "p" : iff = IFF.FOE;
                break;

            case "d" : iff = IFF.UNKNOWN;
                break;
        }

        ttm.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        ttm.setTargetNumber(Integer.parseInt(fields[1]));
        ttm.setDistance(Double.parseDouble(fields[2]));
        ttm.setBearing(Double.parseDouble(fields[3]));
        ttm.setCourse(Double.parseDouble(fields[6]));
        ttm.setSpeed(Double.parseDouble(fields[5]));
        ttm.setStatus(status);
        ttm.setIff(iff);
        ttm.setInterval(Long.parseLong(fields[14]));

        ttm.setType(type);

        return ttm;
    }

    /**
     * Метод получения сообщения RadarSystemData (RSD) из NMEA-сообщения
     * @return Массив полей и значений RSD-сообщения
     */

    private RadarSystemDataMessage getRSD() {

        RadarSystemDataMessage rsd = new RadarSystemDataMessage();

        rsd.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        rsd.setInitialDistance(Double.parseDouble(fields[1]));
        rsd.setInitialBearing(Double.parseDouble(fields[2]));
        rsd.setMovingCircleOfDistance(Double.parseDouble(fields[3]));
        rsd.setBearing(Double.parseDouble(fields[4]));
        rsd.setDistanceFromShip(Double.parseDouble(fields[9]));
        rsd.setBearing2(Double.parseDouble(fields[10]));
        rsd.setDistanceScale(Double.parseDouble(fields[11]));
        rsd.setDistanceUnit(fields[12]);
        rsd.setDisplayOrientation(fields[13]);
        rsd.setWorkingMode(fields[14]);

        return rsd;
    }

    /**
     * Метод проверки RSD-сообщения
     * Если указанная в сообщении применяемая шкала дальности
     * не соотетствует ни одному из делений шкалы DISTANCE_SCALE,
     * то возвращается сообщение об ошибке InvalidMessage
     * @param rsd RSD-сообщение
     * @return Сообщение об ошибке типа InvalidMessage либо null
     */

    private InvalidMessage checkRSD(RadarSystemDataMessage rsd) {

        InvalidMessage invalidMessage = new InvalidMessage();
        String infoMsg = "";

        if (!Arrays.asList(DISTANCE_SCALE).contains(rsd.getDistanceScale())) {

            infoMsg = "RSD message. Wrong distance scale value: " + rsd.getDistanceScale();
            invalidMessage.setInfoMsg(infoMsg);
            return invalidMessage;
        }

        return null;
    }

}

package org.example.models;

import javafx.scene.control.Alert;
import org.example.searadar.mr231.convert.Mr231Converter;
import org.example.searadar.mr231.station.Mr231StationType;
import org.example.searadar.mr2313.convert.Mr2313Converter;
import org.example.searadar.mr2313.message.Mr2313TrackedTargetMessage;
import org.example.searadar.mr2313.station.Mr2313StationType;
import ru.oogis.searadar.api.message.*;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс DbNmeaMessage обрабатывает SQL-запросы к базе данных
 */
public class DbNmeaMessage {
    /**
     * Метод LOAD_DATA() загружает данные из бд с помощью выборки из представления vNmeaMessage
     * @return
     * @throws SQLException
     */
    public static List<NmeaMessage> LOAD_DATA() throws SQLException {
        List<NmeaMessage> list = new ArrayList<>();
        Connection conn = null;
        try{
            conn = ConnectionString.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from vNmeaMessage");

            while(resultSet.next()){
                list.add(new NmeaMessage(resultSet.getInt("MessageID"),
                        resultSet.getString("FormatName"),
                        resultSet.getTimestamp("MessageRecTime"),
                        resultSet.getLong("MessageTime"),
                        resultSet.getString("Message")));
            }
        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        conn.close();
        return list;
    }

    /**
     * Метод getFormatByMessageId возвращает NMEA-формат в соответствии с ID NMEA-сообщения
     * @param id ID NMEA-сообщения
     * @return NMEA-формат в строковом виде
     * @throws SQLException
     */
    public static String getFormatByMessageId(int id) throws SQLException {
        String format = null;
        Connection conn = null;
        try{
            conn = ConnectionString.getConnection();
            Statement statement = conn.createStatement();
            String query = String.format("Select * from fGetFormatByMessageId(%s)",id);
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                format = resultSet.getString("FormatName");
            }

        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        conn.close();
        return format;
    }

    /**
     * Метод getMessageInfo получает дополнительную информацию о NMEA-сообщении
     * в соответствии с его форматом
     * @param id ID NMEA-сообщения
     * @return NMEA-сообщение
     * @throws SQLException
     */
    public static SearadarStationMessage getMessageInfo(int id) throws SQLException {
        SearadarStationMessage messageInfo = new SearadarStationMessage();
        Connection conn = null;
        try{
            conn = ConnectionString.getConnection();
            Statement statement = conn.createStatement();
            String query = null;
            String format = getFormatByMessageId(id);
            ResultSet resultSet;
            switch (format){
                case "TTM":
                    query = String.format("Select * from fGetMessageTTM(%s)",id);
                    Mr2313TrackedTargetMessage ttm = new Mr2313TrackedTargetMessage();

                    resultSet = statement.executeQuery(query);
                    while(resultSet.next()) {
                        ttm.setTargetNumber(resultSet.getInt("TargetNumber"));
                        ttm.setDistance(resultSet.getDouble("Distance"));
                        ttm.setBearing(resultSet.getDouble("Bearing"));
                        ttm.setCourse(resultSet.getDouble("Course"));
                        ttm.setSpeed(resultSet.getDouble("Speed"));
                        ttm.setType(TargetType.valueOf(resultSet.getString("TypeName")));
                        ttm.setStatus(TargetStatus.valueOf(resultSet.getString("StatusName")));
                        ttm.setIff(IFF.valueOf(resultSet.getString("IffName")));
                        try {
                            ttm.setInterval(resultSet.getLong("Interval"));
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText(e.toString());
                            alert.showAndWait();
                        }
                    }
                    return ttm;


                case "RSD":
                    query = String.format("Select * from fGetMessageRSD(%s)",id);
                    RadarSystemDataMessage rsd = new RadarSystemDataMessage();

                    resultSet = statement.executeQuery(query);
                    while(resultSet.next()){
                        rsd.setInitialDistance(resultSet.getDouble("InitialDistance"));
                        rsd.setInitialBearing(resultSet.getDouble("InitialBearing"));
                        rsd.setMovingCircleOfDistance(resultSet.getDouble("MovingCircleOfDistance"));
                        rsd.setBearing(resultSet.getDouble("Bearing"));
                        rsd.setDistanceFromShip(resultSet.getDouble("DistanceFromShip"));
                        rsd.setBearing2(resultSet.getDouble("Bearing2"));
                        rsd.setDistanceScale(resultSet.getDouble("DistanceScale"));
                        rsd.setDistanceUnit(resultSet.getString("DistanceUnitChar"));
                        rsd.setDisplayOrientation(resultSet.getString("DisplayOrientationChar"));
                        rsd.setWorkingMode(resultSet.getString("WorkingModeChar"));
                    }

                    return rsd;

                case "VHW":
                    query = String.format("Select * from fGetMessageVHW(%s)",id);
                    WaterSpeedHeadingMessage vhw = new WaterSpeedHeadingMessage();

                    resultSet = statement.executeQuery(query);
                    while(resultSet.next()){
                        vhw.setCourse(resultSet.getDouble("Course"));
                        vhw.setCourseAttr(resultSet.getString("CourseAttrChar"));
                        vhw.setSpeed(resultSet.getDouble("Speed"));
                        vhw.setSpeedUnit(resultSet.getString("SpeedUnitChar"));
                    }

                    return vhw;

            }

        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        conn.close();
        return messageInfo;
    }

    /**
     * Метод addMessage добавляет новое NMEA-сообщение в БД
     * @param message
     * @throws SQLException
     */
    public static void addMessage(String message) throws SQLException {
        Connection conn = null;
        try{
            Mr231StationType mr231 = new Mr231StationType();
            Mr231Converter converter = mr231.createConverter();
            List<SearadarStationMessage> searadarMessages;
            String format;
            int id = 0;
            String query = null;

            searadarMessages = converter.convert(message);


            // Обработка InvalidMessage в случае наличия ошибки в NMEA-сообщении
            // В случае удачного преобразования в InvalidMessage, NMEA-сообщение не добавится в БД
            try{
                for (SearadarStationMessage ssm : searadarMessages){
                    InvalidMessage m = (InvalidMessage) ssm;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(m.getInfoMsg());
                    alert.showAndWait();
                    return;
                }
            } catch (Exception e){

            }


            // Установка значения поля format в соответсвии с классом
            if (searadarMessages.get(0) instanceof TrackedTargetMessage){
                format = "TTM";
                Mr2313StationType mr2313 = new Mr2313StationType();
                Mr2313Converter mr2313converter = mr2313.createConverter();
                searadarMessages = mr2313converter.convert(message);
            } else if (searadarMessages.get(0) instanceof RadarSystemDataMessage) {
                format = "RSD";
            } else if (searadarMessages.get(0) instanceof WaterSpeedHeadingMessage) {
                format = "VHW";
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Проверьте верность написания NMEA-сообщения.");
                alert.showAndWait();
                return;
            }

            conn = ConnectionString.getConnection();
            Statement statement = conn.createStatement();


            // Добавление записи в таблицу NmeaMessage
            String queryAddMessage = "exec pAddNmeaMessage ?,?,?,?";
            PreparedStatement preparedStatement = conn.prepareStatement(queryAddMessage);
            preparedStatement.setString(1,format);
            preparedStatement.setTimestamp(2,searadarMessages.get(0).getMsgRecTime());
            preparedStatement.setLong(3,0);
            preparedStatement.setString(4,message);
            preparedStatement.execute();

            String queryGetLastMessageId = "select * from fGetLastMessageId()";
            ResultSet resultSet = statement.executeQuery(queryGetLastMessageId);

            while(resultSet.next()){
                id = resultSet.getInt("MessageID");
            }


            // Установка значения поля query в соответствии с форматом
            // и добавление записи в связанные таблицы
            switch (format){
                case "TTM":
                    Mr2313TrackedTargetMessage ttm = (Mr2313TrackedTargetMessage) searadarMessages.get(0);
                    query = String.format("exec pAddTTM %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            id, ttm.getTargetNumber(),ttm.getDistance(),ttm.getBearing(),
                            ttm.getCourse(),"T",ttm.getSpeed(),ttm.getType(),
                            ttm.getStatus(),ttm.getIff(), ttm.getInterval(),
                            ttm.getMsgTime());
                    break;

                case "RSD":
                    RadarSystemDataMessage rsd = (RadarSystemDataMessage) searadarMessages.get(0);
                    query = String.format("exec pAddRSD %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            id,rsd.getInitialDistance(),rsd.getInitialBearing(),rsd.getMovingCircleOfDistance(),
                            rsd.getBearing(),rsd.getDistanceFromShip(),rsd.getBearing2(),rsd.getDistanceScale(),
                            rsd.getDistanceUnit(),rsd.getDisplayOrientation(),rsd.getWorkingMode());
                    break;

                case "VHW":
                    WaterSpeedHeadingMessage vhw = (WaterSpeedHeadingMessage) searadarMessages.get(0);
                    query = String.format("exec pAddVHW %s,%s,%s,%s,%s",
                            id,vhw.getCourse(),vhw.getCourseAttr(),vhw.getSpeed(),vhw.getSpeedUnit());
                    break;

            }
            statement.execute(query);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("NMEA-Message converted.");
            alert.showAndWait();
        } catch (StringIndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Wrong message format.");
            alert.showAndWait();
        }
        conn.close();
    }

    /**
     * Метод deleteMessage удаляет запись из таблицы NmeaMessage
     * @param id ID NMEA-сообщения
     * @throws SQLException
     */
    public static void deleteMessage(int id) throws SQLException {
        Connection conn = null;
        try{
            conn = ConnectionString.getConnection();
            Statement statement = conn.createStatement();
            String query = String.format("exec pDeleteNmeaMessage %s",id);
            statement.execute(query);

        } catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        conn.close();
    }
}

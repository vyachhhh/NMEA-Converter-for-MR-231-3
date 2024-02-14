package mr2313ConverterTest;


import com.sun.rmi.rmid.ExecOptionPermission;
import org.example.searadar.mr2313.convert.Mr2313Converter;
import org.example.searadar.mr2313.message.Mr2313TrackedTargetMessage;
import org.example.searadar.mr2313.station.Mr2313StationType;
import org.junit.jupiter.api.Test;
import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.TrackedTargetMessage;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Mr2313ConverterTest {
    String message;
    Mr2313StationType mr2313 = new Mr2313StationType();
    Mr2313Converter converter = mr2313.createConverter();
    List<SearadarStationMessage> searadarMessages;
    @Test
    public void testTTM(){
        message = "$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,457362,А*42";

        searadarMessages = converter.convert(message);
        Mr2313TrackedTargetMessage messageToGetTime = (Mr2313TrackedTargetMessage) searadarMessages.get(0);

        Mr2313TrackedTargetMessage ttm = new Mr2313TrackedTargetMessage();
        ttm.setMsgRecTime(searadarMessages.get(0).getMsgRecTime());
        ttm.setMsgTime(messageToGetTime.getMsgTime());
        ttm.setTargetNumber(66);
        ttm.setDistance(28.71);
        ttm.setBearing(341.1);
        ttm.setCourse(24.5);
        ttm.setSpeed(57.6);
        ttm.setType(TargetType.UNKNOWN);
        ttm.setIff(IFF.FRIEND);
        ttm.setStatus(TargetStatus.LOST);
        ttm.setInterval((long)457362);

        assertEquals(ttm.toString()
                ,searadarMessages.get(0).toString());
    }
    @Test
    public void testRSD(){
        message = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28";
        searadarMessages = converter.convert(message);

        RadarSystemDataMessage rsd = new RadarSystemDataMessage();
        rsd.setInitialDistance(50.5);
        rsd.setInitialBearing(309.9);
        rsd.setMovingCircleOfDistance(64.8);
        rsd.setBearing(132.3);
        rsd.setDistanceFromShip(52.6);
        rsd.setBearing2(155.0);
        rsd.setDistanceScale(48.0);
        rsd.setDistanceUnit("K");
        rsd.setDisplayOrientation("N");
        rsd.setWorkingMode("S");

        assertEquals( rsd.toString()
                ,searadarMessages.get(0).toString());
    }
    @Test
    public void testRsdInvalidDistanceScale(){
        message = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.1,K,N,S*28";
        searadarMessages = converter.convert(message);
        InvalidMessage im = (InvalidMessage) searadarMessages.get(0);

        assertEquals( "RSD message. Wrong distance scale value: 48.1"
                ,im.getInfoMsg());
    }
    @Test
    public void testWrongMessageFormat(){
        message = "$RAVHW, 115.6,T,,,46.0,N,,*71";
        searadarMessages = converter.convert(message);

        assertThrows(IndexOutOfBoundsException.class, () ->{
           System.out.println(searadarMessages.get(0).toString());
        });
    }

}

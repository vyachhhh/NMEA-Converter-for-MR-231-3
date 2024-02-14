package org.example;

import org.example.searadar.mr231.convert.Mr231Converter;
import org.example.searadar.mr231.station.Mr231StationType;
import org.example.searadar.mr2313.convert.Mr2313Converter;
import org.example.searadar.mr2313.station.Mr2313StationType;
import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Практическое задание направлено на проверку умения создавать функциональные модули и работать с технической
 * документацией.
 * Задача написать парсер сообщений для протокола МР-231-3 на основе парсера МР-231.
 * Приложение к заданию файлы:
 * - Протокол МР-231.docx
 * - Протокол МР-231-3.docx
 * <p>
 * Требования:
 * 1. Перенести контрольный пример из "Протокола МР-231.docx" в метод main, по образцу в методе main;
 * 2. Проверить правильность работы конвертера путём вывода контрольного примера в консоль, по образцу в методе main;
 * 3. Создать модуль с именем mr-231-3 и добавить его в данный проект <module>../mr-231-3</module> и реализовать его
 * функционал в соответствии с "Протоколом МР-231-3.docx" на подобии модуля mr-231;
 * 4. Создать для модуля контрольный пример и проверить правильность работы конвертера путём вывода контрольного
 * примера в консоль
 *
 * <p>
 *     Примечание:
 *     1. Данные в пакете ru.oogis не изменять!!!
 *     2. Весь код должен быть покрыт JavaDoc
 */

public class App {
    public static void main(String[] args) {
        // Контрольный пример для МР-231
        String mr231_TTM = "$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,457362,А*42";
        String mr231_VHW = "$RAVHW, 115.6,T,,,46.0,N,,*71";
        String mr231_RSD = "$RARSD,14.0,0.0,96.9,306.4,,,,,97.7,11.6, 0.3,K,N,S*20";
        String mr231_TTM_2 = "$RATTM,23,13.88,137.2,T,63.8,094.3,T,9.2,79.4,N,b,T,793344,A*42";
        String mr231_TTM_3 = "$RATTM,54,19.1,139.7,T,07.4,084.1,T,2.1, -95.8,N,d,L,,849019,A*7e";
        String mr231_TTM_4 = "$RATTM,46,05.14,123.4,T,52.8,139.5,T,6.3,-96.6,N,b,L,,496256,A*7f";
        String mr231_TTM_5 = "$RATTM,28,28.99,160.0,T,88.4,064.0,T,4.7,77.7,N,b,L,774920,A*59";
        String mr231_RSD_2 = "$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33";
        String mr231_RSD_3 = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28";
        String mr231_VHW_2 = "$RAVHW,356.7,T,,,50.4,N,,*76";

        // Проверка работы конвертера
        System.out.println("MR-231");
        Mr231StationType mr231 = new Mr231StationType();
        Mr231Converter converter = mr231.createConverter();

        List<SearadarStationMessage> searadarMessages = converter.convert(mr231_TTM);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_VHW);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_RSD);
        searadarMessages.forEach(System.out::println);
        //Вывод RSD-сообщения об ошибке
        for (SearadarStationMessage ssm : searadarMessages){
            InvalidMessage m = (InvalidMessage) ssm;
            System.out.println(m.getInfoMsg() + "\n");
        }

        searadarMessages = converter.convert(mr231_TTM_2);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_TTM_3);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_TTM_4);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_TTM_5);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_RSD_2);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_RSD_3);
        searadarMessages.forEach(System.out::println);

        searadarMessages = converter.convert(mr231_VHW_2);
        searadarMessages.forEach(System.out::println);


        // Контрольный пример для МР-231-3
        String mr2313_TTM = "$RATTM,66,28.71,341.1,T,57.6,024.5,T,0.4,4.1,N,b,L,,457362,А*42";
        String mr2313_RSD = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.0,K,N,S*28";
        String mr2313_WrongMessageFormat = "$RAVHW, 115.6,T,,,46.0,N,,*71";
        String mr2313_RSD_2 = "$RARSD,50.5,309.9,64.8,132.3,,,,,52.6,155.0,48.2,K,N,S*28";

        // Проверка работы конвертера МР-231-3
        System.out.println("\nMR-231-3");
        Mr2313StationType mr2313 = new Mr2313StationType();
        Mr2313Converter mr2313Converter = mr2313.createConverter();

        searadarMessages = mr2313Converter.convert(mr2313_TTM);
        searadarMessages.forEach(System.out::println);

        searadarMessages = mr2313Converter.convert(mr2313_RSD);
        searadarMessages.forEach(System.out::println);

        /*
          Попытка принятия станцией МР-231-3 NMEA-сообщения типа Water Speed and Heading (VHW).
          Не выводит результат преобразования сообщения,
          т.к. МР-231-3 не работает с VHW-сообщениями
         */
        searadarMessages = mr2313Converter.convert(mr2313_WrongMessageFormat);
        searadarMessages.forEach(System.out::println);

        searadarMessages = mr2313Converter.convert(mr2313_RSD_2);
        searadarMessages.forEach(System.out::println);

        //Вывод RSD-сообщения об ошибке
        for (SearadarStationMessage ssm : searadarMessages){
            InvalidMessage m = (InvalidMessage) ssm;
            System.out.println(m.getInfoMsg() + "\n");
        }

    }
}

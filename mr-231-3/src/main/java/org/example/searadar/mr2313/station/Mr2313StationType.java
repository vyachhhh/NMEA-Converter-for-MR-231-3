package org.example.searadar.mr2313.station;

import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.example.searadar.mr231.station.Mr231StationType;
import org.example.searadar.mr2313.convert.Mr2313Converter;

import java.nio.charset.Charset;

/**
 * Класс Mr2313StationType нужен для реализации класса Mr2313Converter
 */

public class Mr2313StationType {

    /**
     * Поле, хранящее тип станции
     */

    private static final String STATION_TYPE = "МР-231-3";
    /**
     * Поле, хранящее закодированный тип станции
     */
    private static final String CODEC_NAME = "mr2313";

    /**
     * Инициализация
     */

    protected void doInitialize() {
        TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
                Charset.defaultCharset(),
                LineDelimiter.UNIX,
                LineDelimiter.CRLF
        );

    }

    /**
     * Метод создания преобразователя Mr2313Converter
     * @return Возвращает преобразователь Mr2313Converter
     */

    public Mr2313Converter createConverter() {
        return new Mr2313Converter();
    }
}

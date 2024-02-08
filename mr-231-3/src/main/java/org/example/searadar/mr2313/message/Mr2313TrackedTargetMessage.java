package org.example.searadar.mr2313.message;

import ru.oogis.searadar.api.message.TrackedTargetMessage;

/**
 * Класс Mr2313TrackedTargetMessage расширяет класс TrackedTargetMessage
 *  таким образом, чтобы записывался интервал времени в миллисекундах между
 *  сигналом «Север», поступающим в изделие по каналу аналоговой информации,
 *  и моментом зондирования цели НРЛС
 */

public class Mr2313TrackedTargetMessage extends TrackedTargetMessage {

    /**
     * Поле interval хранит интервал времени в миллисекундах между
     *  сигналом «Север» и моментом зондирования цели НРЛС
     */

    private Long interval;

    /**
     * Конструктор класса Mr2313TrackedTargetMessage вызывает родительский Конструктор
     */

    public Mr2313TrackedTargetMessage() { super(); }

    /**
     * Метод для установки значения поля interval
     * @param interval Интервал времени в миллисекундах
     */
    public void setInterval(Long interval) { this.interval = interval; }

    /**
     * Метод для получения значения поля interval
     * @return Значение поля interval
     */
    public Long getInterval() { return interval; }

    /**
     * Метод возвращает иинформацию о TTM-сообщении, выводя поле и его значение
     * @return Возвращает строку с информацией о TTM-сообщении
     */
    @Override
    public String toString(){
        return "TrackedTargetMessage{" +
                "msgRecTime=" + getMsgRecTime() +
                ", msgTime=" + getMsgTime() +
                ", targetNumber=" + getTargetNumber() +
                ", distance=" + getDistance() +
                ", bearing=" + getBearing() +
                ", course=" + getCourse() +
                ", speed=" + getSpeed() +
                ", type=" + getType() +
                ", status=" + getStatus() +
                ", iff=" + getIff() +
                ", interval=" + getInterval() +
                '}';
    }
}

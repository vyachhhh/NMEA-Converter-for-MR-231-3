package org.example.models;

import java.sql.Timestamp;

/**
 * Класс NmeaMessage является моделью для данных из таблицы NmeaMessage
 */
public class NmeaMessage {
    private int id;
    private String format;
    private Timestamp recTime;
    private long time;
    private String message;

    
    public int getId(){return id;}
    public void setId(int id){
        this.id = id;
    }
    public String getFormat(){return format;}
    public void setFormat(String format){
        this.format = format;
    }
    public Timestamp getRecTime(){return recTime;}
    public void setRecTime(Timestamp recTime){
        this.recTime = recTime;
    }
    public long getTime(){return time;}
    public void setTime(long time){
        this.time = time;
    }
    public String getMessage(){return message;}
    public void setMessage(String message){
        this.message = message;
    }
    public NmeaMessage(int id, String format,Timestamp recTime,
                       long time, String message){
        this.id = id;
        this.format = format;
        this.recTime = recTime;
        this.time = time;
        this.message = message;
    }
}

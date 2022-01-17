package com.novartis.winnovators.chat.messages;

public class Message_item {

    private final  int id,fromUserId,toUserId;
    private final String message,time,date,type,fileFormat,filePath;

    public Message_item(int id, int fromUserId, int toUserId, String message, String time, String date, String type, String fileFormat, String filePath) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.time = time;
        this.date = date;
        this.type = type;
        this.fileFormat = fileFormat;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public String getFilePath() {
        return filePath;
    }
}


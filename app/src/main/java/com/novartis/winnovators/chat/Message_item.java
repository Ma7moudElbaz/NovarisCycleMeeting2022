package com.novartis.winnovators.chat;

public class Message_item {

    private final String nickname,message,type,fileFormat,filePath,fromUserId,toUserId,toSocketId,time,date;

    public Message_item(String nickname, String message, String type, String fileFormat, String filePath, String fromUserId, String toUserId, String toSocketId, String time, String date) {
        this.nickname = nickname;
        this.message = message;
        this.type = type;
        this.fileFormat = fileFormat;
        this.filePath = filePath;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.toSocketId = toSocketId;
        this.time = time;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
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

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getToSocketId() {
        return toSocketId;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}


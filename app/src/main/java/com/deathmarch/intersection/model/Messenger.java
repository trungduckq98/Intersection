package com.deathmarch.intersection.model;

public class Messenger {
    private String from;
    private String type;
    private String content;
    private String seen;
    private long time;

    public Messenger(String from, String type, String content, String seen, long time) {
        this.from = from;
        this.type = type;
        this.content = content;
        this.seen = seen;
        this.time = time;
    }

    public Messenger() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

package com.novartis.winnovators.agenda;

public class Agenda_item {

    final private int id;
    final private String title, from, to;


    public Agenda_item(int id, String title, String from, String to) {
        this.id = id;
        this.title = title;
        this.from = from;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}

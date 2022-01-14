package com.novartis.winnovators.voting_system;

public class Voting_item {

    final private int id;
    final private String title, craeated_at;

    public Voting_item(int id, String title, String craeated_at) {
        this.id = id;
        this.title = title;
        this.craeated_at = craeated_at;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCraeated_at() {
        return craeated_at;
    }
}

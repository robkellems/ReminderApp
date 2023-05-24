package com.example.c323finalapp;

public class Reminder {
    private String title, desc, time, date;

    public Reminder() {}

    public void setTitle(String t) { title = t; }

    public void setDesc(String t) { desc = t; }

    public void setTime(String t) { time = t; }

    public void setDate(String t) { date = t; }

    public String getTitle() { return title; }

    public String getDesc() { return desc; }

    public String getTime() { return time; }

    public String getDate() { return date; }

    public Reminder(String t, String d, String time, String date) {
        title = t;
        desc = d;
        this.time = time;
        this.date = date;
    }

}

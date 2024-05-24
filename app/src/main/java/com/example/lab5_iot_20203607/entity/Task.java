package com.example.lab5_iot_20203607.entity;

import java.io.Serializable;
import java.util.Date;


public class Task implements Serializable {
    private String title;
    private String description;
    private int reminderHour = -1;
    private int reminderMinute = -1;
    private String importance;
    private Date dueDate; // la fecha cuando vence


    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public void setReminderMinute(int reminderMinute) {
        this.reminderMinute = reminderMinute;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    public void setReminderTime(int hour, int minute) {
        this.reminderHour = hour;
        this.reminderMinute = minute;
    }
}


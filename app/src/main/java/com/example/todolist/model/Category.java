package com.example.todolist.model;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Category {
    int id;
    String task;
    boolean isSelected;




    public Category(int id, String task, boolean isSelected) {
        this.id = id;
        this.task = task;
        this.isSelected = isSelected;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

package com.example.todoido.ViewModel;

public class DayTask {
    private String id;
    private String startTime;
    private String endTime;
    private String text;
    private String spinnerSelection;
    private boolean isChecked;

    public DayTask() {
    }

    public DayTask(String startTime, String endTime, String text, String spinnerSelection, boolean isChecked) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
        this.spinnerSelection = spinnerSelection;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getText() {
        return text;
    }

    public String getSpinnerSelection() {
        return spinnerSelection;
    }

    public boolean isChecked() {
        return isChecked;
    }
}

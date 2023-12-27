package com.example.todoido.ViewModel;

public class DayTask {
    private String id;
    private String date;
    private String startTime;
    private String endTime;
    private String text;
    private String spinnerSelection;
    private boolean isChecked;
    private boolean isHeader;

    public DayTask(String date) {
        this.date = date;
        this.isHeader = true; // 생성자에서 헤더를 생성하는 경우에만 true로 설정
    }

    public DayTask(String date, String startTime, String endTime, String text, String spinnerSelection, boolean isChecked) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
        this.spinnerSelection = spinnerSelection;
        this.isChecked = isChecked;
        this.isHeader = false; // 기본값은 false로 설정
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

    // Getter 및 Setter 추가
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }


}

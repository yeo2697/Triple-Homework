package com.triple.homework.mileage;

public enum MileageLog {
    // 마일리지 로그 내용

    FIRST_REVIEW("FIRST_REVIEW"),
    UPLOAD_TEXT("UPLOAD_TEXT"),
    UPLOAD_PHOTOS("UPLOAD_PHOTOS");

    private final String label;

    MileageLog(String label){
        this.label = label;
    }

    public String label() {
        return label;
    }
}

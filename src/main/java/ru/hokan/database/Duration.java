package ru.hokan.database;

public enum Duration {
    MINUTE("minute"),
    HOUR("hour"),
    DAY("day");

    private final String value;

    Duration(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

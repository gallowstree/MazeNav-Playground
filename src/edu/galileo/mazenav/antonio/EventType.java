package edu.galileo.mazenav.antonio;

public enum EventType {
    DISCOVERED("DSC"),
    LOCATION("LOC");

    public final String prefix;

    EventType(String prefix) {
        this.prefix = prefix;
    }
}

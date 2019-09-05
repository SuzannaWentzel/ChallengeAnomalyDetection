package com.example.practicing;

public class AnomalyPoint {

    private float x, y;
    private String severity;

    public AnomalyPoint(float x, float y, String severity) {
        this.x = x;
        this.y = y;
        this.severity = severity;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public String getSeverity() {
        return this.severity;
    }
}


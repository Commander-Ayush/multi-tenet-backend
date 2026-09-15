package com.growthmul.app.lawnmover_fs.dto;

// Request body for PUT /admin/schedule/settings. Plain setters/getters —
// this is a wire shape, not something JPA touches directly.
public class ScheduleSettingsRequest {
    private boolean autoEnabled;
    private int busyThreshold;
    private int fullThreshold;

    public boolean isAutoEnabled() { return autoEnabled; }
    public void setAutoEnabled(boolean autoEnabled) { this.autoEnabled = autoEnabled; }
    public int getBusyThreshold() { return busyThreshold; }
    public void setBusyThreshold(int busyThreshold) { this.busyThreshold = busyThreshold; }
    public int getFullThreshold() { return fullThreshold; }
    public void setFullThreshold(int fullThreshold) { this.fullThreshold = fullThreshold; }
}

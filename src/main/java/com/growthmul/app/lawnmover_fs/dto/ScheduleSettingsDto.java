package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Company;

public class ScheduleSettingsDto {
    private boolean autoEnabled;
    private int busyThreshold;
    private int fullThreshold;

    public static ScheduleSettingsDto from(Company company) {
        ScheduleSettingsDto dto = new ScheduleSettingsDto();
        dto.autoEnabled = company.isAutoScheduleEnabled();
        dto.busyThreshold = company.getBusyThreshold();
        dto.fullThreshold = company.getFullThreshold();
        return dto;
    }

    public boolean isAutoEnabled() { return autoEnabled; }
    public int getBusyThreshold() { return busyThreshold; }
    public int getFullThreshold() { return fullThreshold; }
}

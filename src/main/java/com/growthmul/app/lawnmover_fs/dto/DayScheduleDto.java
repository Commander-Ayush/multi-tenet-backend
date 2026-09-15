package com.growthmul.app.lawnmover_fs.dto;

// One entry in the GET /admin/schedule/month response map (keyed by
// "YYYY-MM-DD"). manualStatus is null when the day has no admin override —
// the frontend then computes the auto status itself from bookingCount and
// the settings it already has (see schedule.js effectiveStatus()).
public class DayScheduleDto {
    private int bookingCount;
    private String manualStatus; // null | "avail" | "busy" | "full"

    public DayScheduleDto(int bookingCount, String manualStatus) {
        this.bookingCount = bookingCount;
        this.manualStatus = manualStatus;
    }

    public int getBookingCount() { return bookingCount; }
    public String getManualStatus() { return manualStatus; }
}

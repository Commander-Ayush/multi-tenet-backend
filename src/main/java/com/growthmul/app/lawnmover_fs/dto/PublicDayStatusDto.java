package com.growthmul.app.lawnmover_fs.dto;

// One entry in the public GET /booking/availability response map (keyed by
// "YYYY-MM-DD"). Deliberately narrower than DayScheduleDto — the storefront
// booking calendar only needs the final status, never the raw booking
// count or whether it came from an override (that's admin-only detail).
public class PublicDayStatusDto {
    private String status; // "avail" | "busy" | "full"

    public PublicDayStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }
}

package com.growthmul.app.lawnmover_fs.dto;

import java.time.LocalDate;

// Request body for PUT /admin/schedule/day.
// status is null (clear override, revert to auto) | "avail" | "busy" | "full".
public class DaySetRequest {
    private LocalDate date;
    private String status;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

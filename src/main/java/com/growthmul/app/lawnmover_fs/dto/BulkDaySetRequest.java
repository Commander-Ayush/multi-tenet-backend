package com.growthmul.app.lawnmover_fs.dto;

import java.time.LocalDate;
import java.util.List;

// Request body for PUT /admin/schedule/days — the multi-select "Select
// Dates" bulk-apply action in the admin Schedule page.
// status is null (clear overrides, revert all to auto) | "avail" | "busy" | "full".
public class BulkDaySetRequest {
    private List<LocalDate> dates;
    private String status;

    public List<LocalDate> getDates() { return dates; }
    public void setDates(List<LocalDate> dates) { this.dates = dates; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

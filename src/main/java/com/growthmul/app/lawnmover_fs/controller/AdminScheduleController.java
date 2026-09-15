package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.*;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Backs the admin Schedule page (schedule.html / schedule.js). Every route
// here matches /admin/** so SecurityConfig already requires a valid JWT —
// nothing extra needed there. Matches the contract schedule.js's
// AdminScheduleApi was already written against.
@RestController
@RequestMapping("/admin/schedule")
public class AdminScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // GET /admin/schedule/settings -> { autoEnabled, busyThreshold, fullThreshold }
    @GetMapping("/settings")
    public ScheduleSettingsDto getSettings() {
        return scheduleService.getSettings(CurrentAdmin.companyId());
    }

    // PUT /admin/schedule/settings  body: { autoEnabled, busyThreshold, fullThreshold }
    @PutMapping("/settings")
    public ScheduleSettingsDto saveSettings(@RequestBody ScheduleSettingsRequest req) {
        return scheduleService.saveSettings(CurrentAdmin.companyId(), req);
    }

    // GET /admin/schedule/month?year=YYYY&month=MM
    // -> { "YYYY-MM-DD": { bookingCount, manualStatus }, ... }
    @GetMapping("/month")
    public Map<String, DayScheduleDto> getMonth(@RequestParam int year, @RequestParam int month) {
        return scheduleService.getMonth(CurrentAdmin.companyId(), year, month);
    }

    // PUT /admin/schedule/day  body: { date, status }
    @PutMapping("/day")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setDayStatus(@RequestBody DaySetRequest req) {
        scheduleService.setDayStatus(CurrentAdmin.companyId(), req);
    }

    // PUT /admin/schedule/days  body: { dates: [...], status }
    // Powers the "Select Dates" multi-select bulk-apply action.
    @PutMapping("/days")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void bulkSetDayStatus(@RequestBody BulkDaySetRequest req) {
        scheduleService.bulkSetDayStatus(CurrentAdmin.companyId(), req);
    }
}

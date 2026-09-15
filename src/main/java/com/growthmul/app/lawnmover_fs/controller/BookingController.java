package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.BookingSubmitRequest;
import com.growthmul.app.lawnmover_fs.dto.PublicDayStatusDto;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.service.BookingService;
import com.growthmul.app.lawnmover_fs.service.PublicTenantResolver;
import com.growthmul.app.lawnmover_fs.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PublicTenantResolver tenantResolver;

    @PostMapping("/booking")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitBooking(@RequestHeader(value = "Origin", required = false) String origin,
                               @RequestBody BookingSubmitRequest req) {
        bookingService.submitBooking(origin, req);
    }

    // GET /booking/availability?year=YYYY&month=MM
    // -> { "YYYY-MM-DD": { status: "avail"|"busy"|"full" }, ... }
    // Backs the storefront booking calendar (Api.getAvailability in the
    // customer-facing script.js). Tenant resolved from Origin, same as
    // every other public route — never exposes booking counts or which
    // days are manually overridden, only the final status.
    @GetMapping("/booking/availability")
    public Map<String, PublicDayStatusDto> getAvailability(
            @RequestHeader(value = "Origin", required = false) String origin,
            @RequestParam int year,
            @RequestParam int month) {
        Company company = tenantResolver.resolve(origin);
        return scheduleService.getPublicAvailability(company, year, month);
    }
}

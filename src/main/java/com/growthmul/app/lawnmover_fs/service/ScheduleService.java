package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.*;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.ScheduleOverride;
import com.growthmul.app.lawnmover_fs.repository.BookingRepository;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import com.growthmul.app.lawnmover_fs.repository.ScheduleOverrideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduleService {

    @Autowired private CompanyRepo companyRepo;
    @Autowired private BookingRepository bookingRepo;
    @Autowired private ScheduleOverrideRepository overrideRepo;

    // ───────────────────────── ADMIN ─────────────────────────

    public ScheduleSettingsDto getSettings(Long companyId) {
        return ScheduleSettingsDto.from(companyOrThrow(companyId));
    }

    public ScheduleSettingsDto saveSettings(Long companyId, ScheduleSettingsRequest req) {
        if (req.getBusyThreshold() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Busy threshold must be at least 1");
        }
        if (req.getFullThreshold() <= req.getBusyThreshold()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full threshold must be greater than the Busy threshold");
        }

        Company company = companyOrThrow(companyId);
        company.setAutoScheduleEnabled(req.isAutoEnabled());
        company.setBusyThreshold(req.getBusyThreshold());
        company.setFullThreshold(req.getFullThreshold());
        companyRepo.save(company);

        return ScheduleSettingsDto.from(company);
    }

    // Admin month view: every day gets its raw booking count AND its
    // manual override (if any) — the frontend combines these with the
    // settings it already has to decide the color. Deliberately not
    // pre-computing the final status here, so schedule.js's "what would
    // Auto say" preview in the day modal stays accurate without a second
    // round trip.
    public Map<String, DayScheduleDto> getMonth(Long companyId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        Map<LocalDate, Integer> countsByDate = new HashMap<>();
        for (Object[] row : bookingRepo.countByDateForCompany(companyId, start, end)) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            countsByDate.put(date, count.intValue());
        }

        Map<LocalDate, String> overridesByDate = new HashMap<>();
        for (ScheduleOverride override : overrideRepo.findByCompanyIdAndDateBetween(companyId, start, end)) {
            overridesByDate.put(override.getDate(), override.getStatus());
        }

        Map<String, DayScheduleDto> result = new HashMap<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            int count = countsByDate.getOrDefault(d, 0);
            String manualStatus = overridesByDate.get(d);
            result.put(d.toString(), new DayScheduleDto(count, manualStatus));
        }
        return result;
    }

    public void setDayStatus(Long companyId, DaySetRequest req) {
        if (req.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date is required");
        }
        applyOverride(companyOrThrow(companyId), req.getDate(), req.getStatus());
    }

    public void bulkSetDayStatus(Long companyId, BulkDaySetRequest req) {
        if (req.getDates() == null || req.getDates().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dates is required");
        }
        Company company = companyOrThrow(companyId);
        for (LocalDate date : req.getDates()) {
            applyOverride(company, date, req.getStatus());
        }
    }

    private void applyOverride(Company company, LocalDate date, String status) {
        if (status == null || status.isBlank()) {
            // "Auto" selected — clear any existing override for this day.
            overrideRepo.deleteByCompanyIdAndDate(company.getId(), date);
            return;
        }
        if (!status.equals("avail") && !status.equals("busy") && !status.equals("full")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status must be avail, busy, full, or null");
        }

        ScheduleOverride override = overrideRepo.findByCompanyIdAndDate(company.getId(), date)
                .orElseGet(() -> {
                    ScheduleOverride o = new ScheduleOverride();
                    o.setCompany(company);
                    o.setDate(date);
                    return o;
                });
        override.setStatus(status);
        overrideRepo.save(override);
    }

    // ───────────────────────── PUBLIC ─────────────────────────
    // Used by the storefront booking calendar (see Api.getAvailability in
    // the customer-facing script.js). Only the final computed status is
    // exposed — never booking counts or which days are overridden, since
    // that's internal business detail.

    public Map<String, PublicDayStatusDto> getPublicAvailability(Company company, int year, int month) {
        Map<String, DayScheduleDto> adminView = getMonth(company.getId(), year, month);
        Map<String, PublicDayStatusDto> result = new HashMap<>();
        for (Map.Entry<String, DayScheduleDto> entry : adminView.entrySet()) {
            String status = computeEffectiveStatus(company, entry.getValue());
            result.put(entry.getKey(), new PublicDayStatusDto(status));
        }
        return result;
    }

    // Same rule schedule.js's computeAutoStatus()/effectiveStatus() apply
    // client-side for the admin calendar's preview — kept here too so the
    // public endpoint's numbers can never drift from what the admin sees.
    private String computeEffectiveStatus(Company company, DayScheduleDto day) {
        if (day.getManualStatus() != null) {
            return day.getManualStatus();
        }
        if (!company.isAutoScheduleEnabled()) {
            return "avail";
        }
        if (day.getBookingCount() >= company.getFullThreshold()) {
            return "full";
        }
        if (day.getBookingCount() >= company.getBusyThreshold()) {
            return "busy";
        }
        return "avail";
    }

    private Company companyOrThrow(Long companyId) {
        return companyRepo.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}

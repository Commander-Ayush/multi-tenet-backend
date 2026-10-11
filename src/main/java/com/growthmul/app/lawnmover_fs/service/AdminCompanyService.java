package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.AboutSettingsDto;
import com.growthmul.app.lawnmover_fs.dto.AboutSettingsRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// Backs the admin About & Contact page (about.html). Mutates the same
// Company row every other admin settings screen (e.g. ScheduleService)
// reads/writes — there's one Company per tenant, so "settings" just
// means "fields on that row".
@Service
public class AdminCompanyService {

    @Autowired private CompanyRepo companyRepo;

    public AboutSettingsDto getSettings(Long companyId) {
        return AboutSettingsDto.from(companyOrThrow(companyId));
    }

    public AboutSettingsDto saveSettings(Long companyId, AboutSettingsRequest req) {
        Company company = companyOrThrow(companyId);
        company.setAboutText(req.getAboutText());
        company.setPhone(req.getPhone());
        company.setEmail(req.getEmail());
        company.setAddress(req.getAddress());
        companyRepo.save(company);
        return AboutSettingsDto.from(company);
    }

    private Company companyOrThrow(Long companyId) {
        return companyRepo.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}

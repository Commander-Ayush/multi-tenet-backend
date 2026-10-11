package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.AboutSettingsDto;
import com.growthmul.app.lawnmover_fs.dto.AboutSettingsRequest;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.AdminCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Backs the admin About & Contact page (about.html / admin-ui.js's
// loadAboutSettings/submitAboutForm). Matches the /admin/about contract
// admin-api.js was already written against. Every route here matches
// /admin/** so SecurityConfig already requires a valid JWT.
@RestController
@RequestMapping("/admin/about")
public class AdminCompanyController {

    @Autowired
    private AdminCompanyService companyService;

    // GET /admin/about -> { aboutText, phone, email, address }
    @GetMapping
    public AboutSettingsDto getSettings() {
        return companyService.getSettings(CurrentAdmin.companyId());
    }

    // PUT /admin/about  body: { aboutText, phone, email, address }
    @PutMapping
    public AboutSettingsDto saveSettings(@RequestBody AboutSettingsRequest req) {
        return companyService.saveSettings(CurrentAdmin.companyId(), req);
    }
}

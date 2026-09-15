package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Year;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private Integer foundedYear;
    private Integer yardsServed;
    private Integer yearsExperience;
    private Long phone;
    private String email;

    // Public storefront requests are matched to a Company by this column,
    // read from the browser's Origin header (see PublicTenantResolver).
    // Store lowercase, no "www.", no protocol — e.g. "clienta.com".
    @Column(unique = true)
    private String domain;

    // ── Schedule availability rules ──
    // Drives the admin Schedule page's auto-status calculation: a day's
    // effective status is "full" once its booking count reaches
    // fullThreshold, "busy" once it reaches busyThreshold, else
    // "avail" — unless a ScheduleOverride exists for that date, which
    // always wins. See ScheduleService.computeEffectiveStatus.
    private boolean autoScheduleEnabled = true;
    private int busyThreshold = 3;
    private int fullThreshold = 6;
}

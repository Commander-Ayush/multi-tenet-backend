package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

// A manual admin override for one calendar day, tenant-scoped. When a row
// exists for (company, date), its status always wins over whatever the
// auto-threshold calculation would produce for that day — see
// ScheduleService.computeEffectiveStatus. No row for a date means "let the
// auto-calculation decide" (or plain "avail" if auto-calc is off).
@Entity
@Table(name = "schedule_overrides", uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "date"}))
@Data
public class ScheduleOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    // "avail" | "busy" | "full" — never null while the row exists; clearing
    // an override means deleting the row (see ScheduleService.setDayStatus),
    // not writing null here.
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}

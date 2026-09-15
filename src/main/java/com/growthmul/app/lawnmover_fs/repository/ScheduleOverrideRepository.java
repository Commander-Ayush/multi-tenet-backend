package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.ScheduleOverride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleOverrideRepository extends JpaRepository<ScheduleOverride, Long> {
    List<ScheduleOverride> findByCompanyIdAndDateBetween(Long companyId, LocalDate start, LocalDate end);
    Optional<ScheduleOverride> findByCompanyIdAndDate(Long companyId, LocalDate date);
    void deleteByCompanyIdAndDate(Long companyId, LocalDate date);
}

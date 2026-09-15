// BookingRepository.java
package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByCompanyIdOrderBySubmittedAtDesc(Long companyId);
    List<BookingRequest> findByCompanyIdAndCompleted(Long companyId, boolean completed);
    long countByCompanyId(Long companyId);

    // Powers the Schedule page (and the public availability endpoint): one
    // row per date-with-at-least-one-booking, in the given month/range, for
    // this tenant only. Object[] is {LocalDate preferredDate, Long count} —
    // see ScheduleService.getMonth for how this gets turned into a map.
    @Query("SELECT b.preferredDate, COUNT(b) FROM BookingRequest b " +
           "WHERE b.company.id = :companyId AND b.preferredDate BETWEEN :start AND :end " +
           "GROUP BY b.preferredDate")
    List<Object[]> countByDateForCompany(@Param("companyId") Long companyId,
                                          @Param("start") LocalDate start,
                                          @Param("end") LocalDate end);
}
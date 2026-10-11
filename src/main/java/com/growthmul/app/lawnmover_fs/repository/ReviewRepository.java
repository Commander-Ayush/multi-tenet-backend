package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findByCompanyIdAndApprovedTrueOrderBySubmittedAtDesc(Long companyId);

    // Paginated version backing the public reviews endpoint — the "Show
    // All" view pulls 10 at a time instead of the whole table at once.
    org.springframework.data.domain.Page<Review> findByCompanyIdAndApprovedTrueOrderBySubmittedAtDesc(
            Long companyId, org.springframework.data.domain.Pageable pageable);

    List<Review> findByCompanyIdOrderBySubmittedAtDesc(Long companyId);

    long countByCompanyIdAndApprovedFalse(Long companyId);
}

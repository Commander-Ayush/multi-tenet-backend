package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<GalleryImage, Long> {
    // Oldest-first (upload order) — matches how the admin grid and the
    // storefront's gallery-container both display photos.
    List<GalleryImage> findByCompanyIdOrderByIdAsc(Long companyId);
    long countByCompanyId(Long companyId);
}

package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.GalleryImageDto;
import com.growthmul.app.lawnmover_fs.dto.GalleryImageRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.GalleryImage;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import com.growthmul.app.lawnmover_fs.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GalleryService {

    @Autowired private GalleryRepository galleryRepo;
    @Autowired private CompanyRepo companyRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC (storefront) ───────────────────────── //

    public List<GalleryImageDto> getGallery(String origin) {
        Company company = tenantResolver.resolve(origin);
        return galleryRepo.findByCompanyIdOrderByIdAsc(company.getId())
                .stream().map(GalleryImageDto::from).toList();
    }

    // ───────────────────────── ADMIN ───────────────────────── //

    public List<GalleryImageDto> getGalleryForAdmin(Long companyId) {
        return galleryRepo.findByCompanyIdOrderByIdAsc(companyId)
                .stream().map(GalleryImageDto::from).toList();
    }

    private static final int MAX_GALLERY_IMAGES = 5;

    public GalleryImageDto addImage(Long companyId, GalleryImageRequest req) {
        if (req.getImageUrl() == null || req.getImageUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "imageUrl is required");
        }
        if (galleryRepo.countByCompanyId(companyId) >= MAX_GALLERY_IMAGES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gallery is limited to " + MAX_GALLERY_IMAGES + " photos. Remove one before adding another.");
        }
        GalleryImage entity = new GalleryImage();
        entity.setImageUrl(req.getImageUrl());
        entity.setCompany(companyRepo.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found")));
        return GalleryImageDto.from(galleryRepo.save(entity));
    }

    public void deleteImage(Long companyId, Long imageId) {
        GalleryImage entity = galleryRepo.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));
        if (!entity.getCompany().getId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your photo");
        }
        galleryRepo.delete(entity);
    }
}

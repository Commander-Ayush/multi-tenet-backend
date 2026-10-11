package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.GalleryImage;

// Same shape for both the public storefront (/gallery) and the admin
// panel (/admin/gallery) — there's nothing admin-only to hide here,
// unlike products (no stock/pricing fields on a photo).
public class GalleryImageDto {
    private Long id;
    private String imageUrl;

    public static GalleryImageDto from(GalleryImage entity) {
        GalleryImageDto dto = new GalleryImageDto();
        dto.id = entity.getId();
        dto.imageUrl = entity.getImageUrl();
        return dto;
    }

    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
}

package com.growthmul.app.lawnmover_fs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "gallery_images")
@Data
public class GalleryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cloudinary secure_url, uploaded unsigned straight from the admin
    // browser — same convention as Product.image. The backend never
    // touches the image bytes, just stores the resulting link.
    @Column(length = 1000, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}

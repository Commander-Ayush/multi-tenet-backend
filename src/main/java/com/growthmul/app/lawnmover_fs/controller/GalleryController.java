package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.GalleryImageDto;
import com.growthmul.app.lawnmover_fs.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    // GET /gallery -> [ { id, imageUrl }, ... ]
    // Backs the storefront's "Our Recent Work" section.
    @GetMapping("/gallery")
    public List<GalleryImageDto> getGallery(@RequestHeader(value = "Origin", required = false) String origin) {
        return galleryService.getGallery(origin);
    }
}

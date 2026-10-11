package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.GalleryImageDto;
import com.growthmul.app.lawnmover_fs.dto.GalleryImageRequest;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Backs the admin Gallery page (gallery.html / admin-ui.js's
// loadGallery/handleGalleryFileChosen/handleDeleteGalleryImage). Every
// route here matches /admin/** so SecurityConfig already requires a
// valid JWT.
@RestController
@RequestMapping("/admin/gallery")
public class AdminGalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping
    public List<GalleryImageDto> getGallery() {
        return galleryService.getGalleryForAdmin(CurrentAdmin.companyId());
    }

    // Body's `imageUrl` is the Cloudinary secure_url the admin browser
    // already got back from its own unsigned upload — this endpoint
    // never touches raw image bytes, it just persists the finished link.
    @PostMapping
    public GalleryImageDto add(@RequestBody GalleryImageRequest req) {
        return galleryService.addImage(CurrentAdmin.companyId(), req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        galleryService.deleteImage(CurrentAdmin.companyId(), id);
    }
}

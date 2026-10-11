package com.growthmul.app.lawnmover_fs.dto;

// Request body for POST /admin/gallery. `imageUrl` is the Cloudinary
// secure_url the admin browser already got back from its own unsigned
// upload (see uploadImageToCloudinary in admin-cloudinary.js) — this
// endpoint never touches raw image bytes, it just persists the link.
public class GalleryImageRequest {
    private String imageUrl;

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

package com.growthmul.app.lawnmover_fs.dto;

// Request body for PUT /admin/about. Field names match submitAboutForm()
// in admin-ui.js exactly: { aboutText, phone, email, address }.
public class AboutSettingsRequest {
    private String aboutText;
    private String phone;
    private String email;
    private String address;

    public String getAboutText() { return aboutText; }
    public void setAboutText(String aboutText) { this.aboutText = aboutText; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Company;

// Backs GET/PUT /admin/about. Deliberately excludes company name — it
// renders in the storefront topbar/logo before any API call resolves,
// so making it editable through a slow network round-trip risks a
// visible pop-in or blank flash. It stays a static value in the
// storefront markup instead of coming from here.
public class AboutSettingsDto {
    private String aboutText;
    private String phone;
    private String email;
    private String address;

    public static AboutSettingsDto from(Company c) {
        AboutSettingsDto dto = new AboutSettingsDto();
        dto.aboutText = c.getAboutText();
        dto.phone = c.getPhone();
        dto.email = c.getEmail();
        dto.address = c.getAddress();
        return dto;
    }

    public String getAboutText() { return aboutText; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}

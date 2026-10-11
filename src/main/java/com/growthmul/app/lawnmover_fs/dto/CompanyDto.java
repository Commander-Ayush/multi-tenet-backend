package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.Company;

import java.time.Year;

public class CompanyDto {
    private String name;
    private String city;
    private Integer foundedYear;
    private Integer yardsServed;
    private Integer yearsExperience;
    private String phone;
    private String email;
    private String aboutText;
    private String address;

    public static CompanyDto from(Company c) {
        CompanyDto dto = new CompanyDto();
        dto.name = c.getName();
        dto.city = c.getCity();
        dto.foundedYear = c.getFoundedYear();
        dto.yardsServed = c.getYardsServed();
        dto.yearsExperience = Year.now().getValue() - c.getFoundedYear();
        dto.phone = c.getPhone();
        dto.email = c.getEmail();
        dto.aboutText = c.getAboutText();
        dto.address = c.getAddress();
        return dto;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public Integer getFoundedYear() { return foundedYear; }
    public Integer getYardsServed() { return yardsServed; }
    public Integer getYearsExperience() { return yearsExperience; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAboutText() { return aboutText; }
    public String getAddress() { return address; }
}

package com.growthmul.app.lawnmover_fs.dto;

import com.growthmul.app.lawnmover_fs.entity.BookingRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String service;
    private LocalDate preferredDate;
    private LocalDateTime submittedAt;
    private boolean completed;
    private String notes;

    public static BookingDto from(BookingRequest entity) {
        BookingDto dto = new BookingDto();
        dto.id = entity.getId();
        dto.firstName = entity.getFirstName();
        dto.lastName = entity.getLastName();
        dto.email = entity.getEmail();
        dto.phone = entity.getPhone();
        dto.service = entity.getServiceOffering() != null ? entity.getServiceOffering().getName() : "Unspecified";
        dto.preferredDate = entity.getPreferredDate();
        dto.submittedAt = entity.getSubmittedAt();
        dto.completed = entity.isCompleted();
        dto.notes = entity.getNotes();
        return dto;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getService() { return service; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public boolean isCompleted() { return completed; }
    public String getNotes(){ return notes;}
}

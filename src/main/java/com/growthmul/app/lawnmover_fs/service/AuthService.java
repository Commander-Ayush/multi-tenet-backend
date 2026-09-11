package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.LoginResponse;
import com.growthmul.app.lawnmover_fs.entity.AdminUser;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.repository.AdminUserRepository;
import com.growthmul.app.lawnmover_fs.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired private AdminUserRepository adminUserRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private PublicTenantResolver tenantResolver;

    public LoginResponse login(String origin, String email, String password) {
        Company company = tenantResolver.resolve(origin); // throws 400/404 on missing/unknown domain

        AdminUser user = adminUserRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password"));

        boolean passwordOk = passwordEncoder.matches(password, user.getPasswordHash());
        boolean domainOk = user.getCompany().getId().equals(company.getId());

        if (!passwordOk || !domainOk) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getCompany().getId());
        return new LoginResponse(token);
    }

    public void changePassword(Long companyId, String currentPassword, String newPassword) {
        AdminUser user = adminUserRepo.findByCompanyId(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin account not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 8 characters");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        adminUserRepo.save(user);
    }
}

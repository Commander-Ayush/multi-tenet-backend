package com.growthmul.app.lawnmover_fs.config;

import com.growthmul.app.lawnmover_fs.entity.AdminUser;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.repository.AdminUserRepository;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired private CompanyRepo companyRepo;
    @Autowired private AdminUserRepository adminUserRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private static final String DEV_DOMAIN = "127.0.0.1:5500";
    private static final String DEV_ADMIN_EMAIL = "admin@demo.local";
    private static final String DEV_ADMIN_PASSWORD = "ChangeMe123!"; // dev only — change immediately for any real use

    @Override
    public void run(ApplicationArguments args) {
        System.out.println((passwordEncoder.encode(DEV_ADMIN_PASSWORD)));
        if (companyRepo.count() > 0) return; // real data already exists — never touch it

        Company demo = new Company();
        demo.setName("GreenCut Lawn Services");
        demo.setCity("Austin");
        demo.setFoundedYear(2015);
        demo.setYardsServed(4800);
        demo.setYearsExperience(11);
        demo.setPhone("8004736288");
        demo.setEmail("hello@greencut.com");
        demo.setDomain(DEV_DOMAIN);
        demo.setAboutText("Providing top-tier lawn care and peace of mind since 2015.");
        demo.setAddress("100 Technology Dr, Suite 400");
//        companyRepo.save(demo);

        AdminUser admin = new AdminUser();
        admin.setEmail(DEV_ADMIN_EMAIL);
        admin.setPasswordHash(passwordEncoder.encode(DEV_ADMIN_PASSWORD));
        admin.setCompany(demo);
//        adminUserRepo.save(admin);

        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Seeded a demo company for local dev:");
        System.out.println("   domain:   " + DEV_DOMAIN + "  (must match storefront's Origin exactly)");
        System.out.println("   admin login: " + DEV_ADMIN_EMAIL + " / " + DEV_ADMIN_PASSWORD);
        System.out.println("─────────────────────────────────────────────────────");
    }
}

package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.LoginRequest;
import com.growthmul.app.lawnmover_fs.dto.LoginResponse;
import com.growthmul.app.lawnmover_fs.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestHeader(value = "Origin", required = false) String origin,
                               @RequestBody LoginRequest req) {
        return authService.login(origin, req.getEmail(), req.getPassword());
    }
}

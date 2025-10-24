package com.jamuara.crs.common.controller;

import com.jamuara.crs.common.service.TboAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class TboAuthController {
    @Autowired
    private TboAuthService tboAuthService;

    @GetMapping("tbo-auth")
    public String authenticate() {
        tboAuthService.authenticate();
        return "authenticated";
    }
}

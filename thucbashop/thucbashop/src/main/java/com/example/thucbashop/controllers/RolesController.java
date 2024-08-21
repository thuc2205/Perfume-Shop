package com.example.thucbashop.controllers;

import com.example.thucbashop.models.Role;
import com.example.thucbashop.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "*", maxAge = 4200)
@RequestMapping("${api.prefix}/roles")
@RestController
@RequiredArgsConstructor
public class RolesController {
    private final RoleService roleService;
    @CrossOrigin
    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}

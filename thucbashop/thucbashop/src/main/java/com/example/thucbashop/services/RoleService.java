package com.example.thucbashop.services;

import com.example.thucbashop.models.Role;
import com.example.thucbashop.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepo roleRepo;
    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}

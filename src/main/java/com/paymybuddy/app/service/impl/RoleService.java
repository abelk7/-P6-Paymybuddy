package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service("roleService")
public class RoleService implements IRoleService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleUser() {
        return roleRepository.getRoleUser();
    }

    @Override
    public Role getRoleAdmin() {
        return roleRepository.getRoleAdmin();
    }
}

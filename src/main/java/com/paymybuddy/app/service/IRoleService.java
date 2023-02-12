package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Role;

import java.util.List;

public interface IRoleService {
    List<Role> getAllRoles();

    Role getRoleUser();

    Role getRoleAdmin();
}

package com.gws.crm.authentication.service;

import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface SuperAdminAuthService {

    ResponseEntity<?> createAdmin(AdminRegistrationDto adminRegistrationDto, Transition transition);
}

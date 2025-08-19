package com.gws.crm.core.employee.service;

import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.common.entities.Transition;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface SuperAdminManagementService {

    ResponseEntity<?> createAdmin(AdminRegistrationDto adminRegistrationDto, Transition transition);

    ResponseEntity<?> getAdmins(Transition transition);

    ResponseEntity<?> deleteAdmin(long id, Transition transition);

    ResponseEntity<?> editAdmin(@Valid AdminRegistrationDto adminRegistrationDto, Transition transition) throws Exception;

    ResponseEntity<?> toggleAdminLockStatus(long adminId, Transition transition);

    ResponseEntity<?> getAdmin(Long adminId, Transition transition);

    ResponseEntity<?> restoreAdmin(long id, Transition transition);
}

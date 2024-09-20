package com.gws.crm.core.admin.service;

import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface SuperAdminManagementService {

    ResponseEntity<?> getAdmins(Transition transition);

    ResponseEntity<?> deleteAdmin(long id, Transition transition);
}

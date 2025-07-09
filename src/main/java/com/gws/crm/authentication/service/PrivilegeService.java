package com.gws.crm.authentication.service;


import com.gws.crm.authentication.dto.PrivilegeGroupCriteria;
import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface PrivilegeService {

    ResponseEntity<?> getPrivileges(long privilegeId);

    ResponseEntity<?> getPrivilegesGroups();

    ResponseEntity<?> getPrivileges(PrivilegeGroupCriteria privilegeGroupCriteria, Transition transition);

    ResponseEntity<?> getGroupDetails(long id, Transition transition);
}

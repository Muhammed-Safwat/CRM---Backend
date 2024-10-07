package com.gws.crm.authentication.service;


import org.springframework.http.ResponseEntity;

public interface PrivilegeService {

    ResponseEntity<?> getPrivileges(long privilegeId);

    ResponseEntity<?> getPrivilegesGroups();
}

package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImp implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Override
    public ResponseEntity<?> getPrivileges() {
        List<Privilege> privileges = privilegeRepository.findAll();
        Map<String, List<Privilege>> groupedPrivilegesMap = privileges.stream()
                .collect(Collectors.groupingBy(Privilege::getGroupName));
        return success(groupedPrivilegesMap);
    }
}



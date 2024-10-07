package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.service.PrivilegeService;
import com.gws.crm.core.leads.repository.PrivilegeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImp implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    private final PrivilegeGroupRepository privilegeGroupRepository;

    @Override
    public ResponseEntity<?> getPrivileges(long groupId) {
        List<Privilege> privileges = privilegeRepository.findAllByPrivilegeGroupId(groupId);
        Map<String, List<Privilege>> groupedPrivilegesMap = privileges.stream()
                .collect(Collectors.groupingBy(
                        Privilege::getGroupName,
                        TreeMap::new,
                        Collectors.toList()
                ));
        return success(groupedPrivilegesMap);
    }

    @Override
    public ResponseEntity<?> getPrivilegesGroups() {
        return success(privilegeGroupRepository.getAllPrivilegeGroups());
    }
}



package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.dto.PrivilegeGroupCriteria;
import com.gws.crm.authentication.dto.PrivilegeGroupDetailsRes;
import com.gws.crm.authentication.dto.PrivilegeGroupRes;
import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.authentication.mapper.PrivilegeGroupMapper;
import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.service.PrivilegeService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.mapper.EmployeeMapper;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.repository.PrivilegeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.core.employee.spcification.PrivilegesGroupSpecification.filter;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImp implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeGroupRepository privilegeGroupRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PrivilegeGroupMapper privilegeGroupMapper;

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

    @Override
    public ResponseEntity<?> getPrivileges(PrivilegeGroupCriteria privilegeGroupCriteria, Transition transition) {

        Specification<PrivilegeGroup> privilegeGroupSpc = filter(privilegeGroupCriteria, transition);
        List<PrivilegeGroup> privilegeGroups = privilegeGroupRepository.findAll(privilegeGroupSpc);

        List<PrivilegeGroupRes> responseList = privilegeGroups.stream()
                .map(group -> {
                    long employeeCount = employeeRepository.countByPrivilegeGroupIdAndAdminId(group.getId(), transition.getUserId());
                    return PrivilegeGroupRes.builder()
                            .id(group.getId())
                            .name(group.getJobName())
                            .status(group.isStatus())
                            .employeeCount(String.valueOf(employeeCount))
                            .build();
                })
                .filter(dto -> Integer.parseInt(dto.getEmployeeCount()) > 0)
                .toList();

        return success(responseList);
    }

    @Override
    public ResponseEntity<?> getGroupDetails(long id, Transition transition) {
        PrivilegeGroup privilegeGroup = privilegeGroupRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        Set<Employee> employees = employeeRepository.getEmployeesByPrivilegeGroupId(id);
        List<EmployeeSimpleDTO> employeeSimpleDTOS = employeeMapper.toListSimpleDto(employees);
        PrivilegeGroupDetailsRes privRes = privilegeGroupMapper.toDto(privilegeGroup, employeeSimpleDTOS);
        return success(privRes);
    }

}



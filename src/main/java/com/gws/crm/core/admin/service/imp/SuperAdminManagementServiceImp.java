package com.gws.crm.core.admin.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.mapper.AdminMapper;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.admin.service.SuperAdminManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
public class SuperAdminManagementServiceImp implements SuperAdminManagementService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Override
    public ResponseEntity<?> getAdmins(Transition transition) {
        List<Admin> admins = adminRepository.findAll();
        List<AdminBasicsInfo> adminsBasicsInfo = adminMapper.toDtoList(admins);
        return success(adminsBasicsInfo, "Success");
    }

    @Override
    public ResponseEntity<?> deleteAdmin(long id, Transition transition) {
        adminRepository.deleteById(id);
        return success();
    }


}

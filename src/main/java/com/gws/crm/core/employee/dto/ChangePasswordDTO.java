package com.gws.crm.core.employee.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {

    private String newPassword;
    private String confirmPassword;
    private long employeeId;
}

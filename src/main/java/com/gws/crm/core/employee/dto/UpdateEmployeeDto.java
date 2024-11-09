package com.gws.crm.core.employee.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateEmployeeDto {

    @NotNull(message = "User Id can't be null")
    private long id;

    @NotBlank(message = "Name is required and cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Phone is required and cannot be blank")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    private long jobTitleId;

    @NotEmpty(message = "You must choose at least one privilege")
    private Set<Long> privileges;
    private List<Long> teamIds;
}

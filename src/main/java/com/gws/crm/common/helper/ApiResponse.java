package com.gws.crm.common.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T body;
    private boolean ok;
}

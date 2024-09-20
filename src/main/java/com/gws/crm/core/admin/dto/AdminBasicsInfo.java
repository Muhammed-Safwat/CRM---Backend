package com.gws.crm.core.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AdminBasicsInfo {

    private long id;

    private String name;

    private String username;

    private String image;

    private String phone;

    private boolean locked = false;

    private boolean enabled = false;

    private LocalDateTime accountNonExpired;

    private LocalDateTime credentialsNonExpired;


}

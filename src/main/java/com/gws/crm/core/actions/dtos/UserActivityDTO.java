package com.gws.crm.core.actions.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivityDTO {
    private String action;
    private String description;
    private String timestampFormatted;
}

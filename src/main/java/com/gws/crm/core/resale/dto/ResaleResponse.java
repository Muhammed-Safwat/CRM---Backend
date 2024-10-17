package com.gws.crm.core.resale.dto;

import com.gws.crm.core.lookups.dto.LookupDTO;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleResponse {
    private int id;
    private String name;
    private String phone;
    private String email;
    private LookupDTO project;
    private String BUA;
    private String Phase;
    private String Code;
    private LookupDTO category;
    private LookupDTO property;
    private String note;
    private LookupDTO creator;
}

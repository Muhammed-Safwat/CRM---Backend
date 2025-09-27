package com.gws.crm.core.export.dtos;


import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportRequestDto {
    private ArrayList<Long> ids ;
    private String type ;
    private String referenceType;
}

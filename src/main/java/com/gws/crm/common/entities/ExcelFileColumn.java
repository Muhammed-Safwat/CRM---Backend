package com.gws.crm.common.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcelFileColumn {
    private String header;
    private String key;
    private int width;
}

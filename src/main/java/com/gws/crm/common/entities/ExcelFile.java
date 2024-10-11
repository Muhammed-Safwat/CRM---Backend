package com.gws.crm.common.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ExcelFile {
    private List<ExcelFileColumn> header;
    private Map<String, List<String>> dropdowns;

}

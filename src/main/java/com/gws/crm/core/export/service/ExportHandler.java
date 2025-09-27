package com.gws.crm.core.export.service;

import java.util.List;
import java.util.Map;

public interface ExportHandler {
    boolean supports(String referenceType);
    byte[] generateFile(List<Long> ids, Map<String, Object> options) throws Exception;
    String defaultFilename();

}

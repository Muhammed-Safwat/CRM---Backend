package com.gws.crm.core.actions.service;

import com.gws.crm.core.actions.dtos.UserActivityDTO;

import java.io.OutputStream;
import java.util.List;

public interface UserActivityReportGenerator {
    void generatePdfReport(List<UserActivityDTO> activities, OutputStream out) throws Exception;
}
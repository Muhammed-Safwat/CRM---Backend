package com.gws.crm.common.service;

import com.gws.crm.common.entities.Transition;

import java.util.List;
import java.util.Map;

public interface ExcelSheetService {

    Map<String, List<String>> generateLeadExcelSheetMap(Transition transition);

    Map<String, List<String>> generatePreLeadExcelSheetMap(Transition transition);

    Map<String, List<String>> generateResaleSheetMap(Transition transition);
}


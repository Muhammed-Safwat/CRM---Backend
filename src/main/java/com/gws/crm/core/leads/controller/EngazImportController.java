package com.gws.crm.core.leads.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.service.imp.EngazImportLeadsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/engaz-import")
@RequiredArgsConstructor
public class EngazImportController {

    private final EngazImportLeadsService engazImportLeadsService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadLeads(@RequestParam("file") MultipartFile file, HttpServletRequest request, Transition transition) {
         return engazImportLeadsService.importLeads(file,request,transition);
    }


}

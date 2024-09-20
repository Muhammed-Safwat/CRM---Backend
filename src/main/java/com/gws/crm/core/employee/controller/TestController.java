package com.gws.crm.core.employee.controller;

import com.gws.crm.common.entities.Transition;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
@RequestMapping("test/test")
public class TestController {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ROLE')")
    public String testTransition(Transition transition) {
        log.info(transition.getUserId() + " ******************************** ");
        return "done";
    }
}

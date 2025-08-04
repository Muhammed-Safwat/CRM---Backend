package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actions/employee")
@AllArgsConstructor
public class UserActionController {

    private final UserActionService userActionService;

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getActions(
            @PathVariable long leadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Transition transition
    ) {
        return userActionService.getActions(leadId, page, size, transition);
    }

}

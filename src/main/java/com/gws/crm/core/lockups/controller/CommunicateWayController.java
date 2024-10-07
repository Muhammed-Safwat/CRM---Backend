package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.CommunicateWayDTO;
import com.gws.crm.core.lockups.service.CommunicateWayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lockups/communicate-ways")
@RequiredArgsConstructor
public class CommunicateWayController {

    private final CommunicateWayService communicateWayService;

    @GetMapping
    public ResponseEntity<?> getCommunicateWays(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size, Transition transition) {
        return communicateWayService.getCommunicateWays(page, size, transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommunicateWayById(@PathVariable long id, Transition transition) {
        return communicateWayService.getCommunicateWayById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createCommunicateWay(@Valid @RequestBody CommunicateWayDTO communicateWayDTO, Transition transition) {
        return communicateWayService.createCommunicateWay(communicateWayDTO, transition);
    }

    @PutMapping()
    public ResponseEntity<?> updateCommunicateWay(@Valid @RequestBody CommunicateWayDTO communicateWayDTO, Transition transition) {
        return communicateWayService.updateCommunicateWay(communicateWayDTO, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunicateWay(@PathVariable Long id, Transition transition) {
        return communicateWayService.deleteCommunicateWay(id, transition);
    }

}

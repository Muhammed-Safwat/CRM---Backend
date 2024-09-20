package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.CommunicateWayDTO;
import com.gws.crm.core.lockups.service.CommunicateWayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communicate-ways")
@RequiredArgsConstructor
public class CommunicateWayController {

    private final CommunicateWayService communicateWayService;

    @GetMapping
    public ResponseEntity<?> getCommunicateWays(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return communicateWayService.getCommunicateWays(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommunicateWayById(@PathVariable long id) {
        return communicateWayService.getCommunicateWayById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCommunicateWay(@Valid @RequestBody CommunicateWayDTO communicateWayDTO) {
        return communicateWayService.createCommunicateWay(communicateWayDTO);
    }

    @PutMapping()
    public ResponseEntity<?> updateCommunicateWay(@Valid @RequestBody CommunicateWayDTO communicateWayDTO) {
        return communicateWayService.updateCommunicateWay(communicateWayDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunicateWay(@PathVariable Long id) {
        return communicateWayService.deleteCommunicateWay(id);
    }

}

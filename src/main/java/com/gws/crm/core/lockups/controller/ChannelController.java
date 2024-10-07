package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.ChannelDTO;
import com.gws.crm.core.lockups.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lockups/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<?> getChannels(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size, Transition transition) {
        return channelService.getChannels(page, size,transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelById(@PathVariable Long id, Transition transition) {
        return channelService.getChannelById(id,transition);
    }

    @PostMapping
    public ResponseEntity<?> createChannel(@Valid @RequestBody ChannelDTO channelDTO, Transition transition) {
        return channelService.createChannel(channelDTO,transition);
    }

    @PutMapping()
    public ResponseEntity<?> updateChannel(@RequestBody ChannelDTO channelDTO, Transition transition) {
        return channelService.updateChannel(channelDTO,transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChannel(@PathVariable long id, Transition transition) {
        return channelService.deleteChannel(id,transition);
    }

}

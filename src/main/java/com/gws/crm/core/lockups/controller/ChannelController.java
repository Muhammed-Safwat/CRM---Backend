package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.ChannelDTO;
import com.gws.crm.core.lockups.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<?> getChannels(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return channelService.getChannels(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChannelById(@PathVariable Long id) {
        return channelService.getChannelById(id);
    }

    @PostMapping
    public ResponseEntity<?> createChannel(@Valid @RequestBody ChannelDTO channelDTO) {
        return channelService.createChannel(channelDTO);
    }

    @PutMapping()
    public ResponseEntity<?> updateChannel(@RequestBody ChannelDTO channelDTO) {
        return channelService.updateChannel(channelDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChannel(@PathVariable long id) {
        return channelService.deleteChannel(id);
    }

}

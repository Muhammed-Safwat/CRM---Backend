package com.gws.crm.core.lockups.service;

import com.gws.crm.core.lockups.dto.ChannelDTO;
import org.springframework.http.ResponseEntity;

public interface ChannelService {
    ResponseEntity<?> getChannels(int page, int size);

    ResponseEntity<?> getChannelById(long id);

    ResponseEntity<?> createChannel(ChannelDTO channelDTO);

    ResponseEntity<?> updateChannel(ChannelDTO channelDTO);

    ResponseEntity<?> deleteChannel(long id);
}

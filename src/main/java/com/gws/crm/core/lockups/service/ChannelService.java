package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.LockupDTO;
import org.springframework.http.ResponseEntity;

public interface ChannelService {
    ResponseEntity<?> getChannels(int page, int size, Transition transition);

    ResponseEntity<?> getChannelById(long id, Transition transition);

    ResponseEntity<?> createChannel(LockupDTO channelDTO, Transition transition);

    ResponseEntity<?> updateChannel(LockupDTO channelDTO, Transition transition);

    ResponseEntity<?> deleteChannel(long id, Transition transition);
}

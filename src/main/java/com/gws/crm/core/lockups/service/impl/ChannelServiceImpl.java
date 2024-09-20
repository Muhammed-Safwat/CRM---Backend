package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.lockups.dto.ChannelDTO;
import com.gws.crm.core.lockups.entity.Channel;
import com.gws.crm.core.lockups.repository.ChannelRepository;
import com.gws.crm.core.lockups.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ResponseEntity<?> getChannels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Channel> channelPage = channelRepository.findAll(pageable);
        return success(channelPage);
    }

    @Override
    public ResponseEntity<?> getChannelById(long id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(channel);
    }

    @Override
    public ResponseEntity<?> createChannel(ChannelDTO channelDTO) {
        Channel channel = Channel.builder().name(channelDTO.getName()).build();
        Channel savedChannel = channelRepository.save(channel);
        return success(savedChannel);
    }

    @Override
    public ResponseEntity<?> updateChannel(ChannelDTO channelDTO) {
        Channel channel = channelRepository.findById(channelDTO.getId())
                .orElseThrow(NotFoundResourceException::new);
        channel.setName(channelDTO.getName());
        channelRepository.save(channel);
        return success(channel);
    }

    @Override
    public ResponseEntity<?> deleteChannel(long id) {
        channelRepository.deleteById(id);
        return success("Channel delete successfully");
    }
}

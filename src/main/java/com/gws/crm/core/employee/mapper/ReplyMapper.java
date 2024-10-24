package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.ReplayResponse;
import com.gws.crm.core.employee.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReplyMapper {

    public ReplayResponse toDto(Reply reply) {
        return ReplayResponse.builder()
                .reply(reply.getReply())
                .createdAt(reply.getCreatedAt())
                .name(reply.getEmployee().getName())
                .image(reply.getEmployee().getImage())
                .jobTile(reply.getEmployee().getJobName().getJobName())
                .build();
    }

    public List<ReplayResponse> toDto(List<Reply> replies) {
        return replies.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<ReplayResponse> toDto(Page<Reply> replies) {
        return replies.map(this::toDto);
    }

}

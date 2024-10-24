package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.CommentResponse;
import com.gws.crm.core.employee.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ReplyMapper replyMapper;

    public CommentResponse toDto(Comment comment) {
        return CommentResponse.builder()
                .comment(comment.getComment())
                .name(comment.getEmployee().getName())
                .image(comment.getEmployee().getImage())
                .jobTile(comment.getEmployee().getJobName().getJobName())
                .createdAt(comment.getCreatedAt())
                .replies(replyMapper.toDto(comment.getReplies()))
                .build();
    }

    public List<CommentResponse> toDto(List<Comment> comments) {
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<CommentResponse> toDto(Page<Comment> comments) {
        return comments.map(this::toDto);
    }

}

package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.CommentDto;
import com.gws.crm.core.employee.dto.CommentResponse;
import com.gws.crm.core.employee.dto.ReplayResponse;
import com.gws.crm.core.employee.dto.ReplyDto;
import com.gws.crm.core.employee.entity.Comment;
import com.gws.crm.core.employee.entity.Reply;
import com.gws.crm.core.employee.mapper.CommentMapper;
import com.gws.crm.core.employee.mapper.ReplyMapper;
import com.gws.crm.core.employee.repository.CommentRepository;
import com.gws.crm.core.employee.service.CommentService;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;
    private final BaseLeadRepository leadRepository;
    private final CommentMapper commentMapper;
    private final ReplyMapper replyMapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> getComments(long leadId, int page, Transition transition) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Comment> commentPage = commentRepository.getCommentByLeadIdOrderByCreatedAtDesc(leadId, pageable);
        Page<CommentResponse> commentResponses = commentMapper.toDto(commentPage);
        return success(commentResponses);
    }

    @Override
    public ResponseEntity<?> addComment(CommentDto commentDto, Transition transition) {
        BaseLead salesLead =
                leadRepository.getReferenceById(commentDto.getLeadId()) ;

        User user = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Comment comment = Comment.builder()
                .comment(commentDto.getComment())
                .createdAt(LocalDateTime.now())
                .replies(new ArrayList<>())
                .user(user)
                .lead(salesLead)
                .build();
        Comment savedComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.toDto(savedComment);
        return success(commentResponse);
    }

    @Override
    public ResponseEntity<?> addReply(ReplyDto replyDto, Transition transition) {
        Comment comment = commentRepository.findById(replyDto.getCommentId())
                .orElseThrow(NotFoundResourceException::new);
        User user = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Reply reply = Reply.builder()
                .reply(replyDto.getReply())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        comment.getReplies().add(reply);
        commentRepository.save(comment);
        ReplayResponse replayResponse = replyMapper.toDto(reply);
        return success(replayResponse);
    }

}

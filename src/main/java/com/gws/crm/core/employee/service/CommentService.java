package com.gws.crm.core.employee.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.CommentDto;
import com.gws.crm.core.employee.dto.ReplyDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<?> getComments(long leadId, int page, Transition transition);

    ResponseEntity<?> addComment(CommentDto commentDto, Transition transition);

    ResponseEntity<?> addReply(ReplyDto replyDto, Transition transition);

}


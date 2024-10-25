package com.gws.crm.core.employee.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.CommentDto;
import com.gws.crm.core.employee.dto.ReplyDto;
import com.gws.crm.core.employee.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getComments(@PathVariable long leadId,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         Transition transition) {
        return commentService.getComments(leadId, page, transition);
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto,
                                        Transition transition) {
        return commentService.addComment(commentDto, transition);
    }

    @PostMapping("reply")
    public ResponseEntity<?> addReply(@RequestBody ReplyDto replyDto,
                                      Transition transition) {
        return commentService.addReply(replyDto, transition);
    }
}

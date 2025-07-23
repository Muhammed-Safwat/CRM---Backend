package com.gws.crm.core.actions.repository.repository;

import com.gws.crm.core.employee.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> getCommentByLeadIdOrderByCreatedAtDesc(long leadId, Pageable pageable);
}

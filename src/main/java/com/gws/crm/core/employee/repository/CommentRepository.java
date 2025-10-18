package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> getCommentByLeadIdOrderByCreatedAtDesc(long leadId, Pageable pageable);
    @Query("""
    select distinct c
    from Comment c
    left join fetch c.user
    left join fetch c.replies r
    left join fetch r.user
    where c.lead.id = :leadId
    order by c.createdAt desc
""")
    Page<Comment> getCommentsWithReplies(long leadId, Pageable pageable);
}

package com.gws.crm.core.notes.repository;

import com.gws.crm.core.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    @Modifying
    @Transactional
    int deleteByIdAndCreatorId(long id, long creatorId);

    Note getNoteByIdAndCreatorId(long id, long creatorId);
}

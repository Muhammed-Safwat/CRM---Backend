package com.gws.crm.core.notes.repository;

import com.gws.crm.core.notes.entity.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTypeRepository extends JpaRepository<NoteType,Long> {

    NoteType findByName(String name);
}

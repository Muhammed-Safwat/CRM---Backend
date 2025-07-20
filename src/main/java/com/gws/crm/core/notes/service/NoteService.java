package com.gws.crm.core.notes.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.notes.dtos.CreateNoteDTO;
import com.gws.crm.core.notes.dtos.NoteCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface NoteService {

    ResponseEntity<?> createNote(CreateNoteDTO dto, Transition transition);

    ResponseEntity<?> getNote(Long id, Transition transition);

    ResponseEntity<?> deleteNote(Long id, Transition transition);

    ResponseEntity<?> getAllNotes(NoteCriteria noteCriteria,  Transition transition);
}

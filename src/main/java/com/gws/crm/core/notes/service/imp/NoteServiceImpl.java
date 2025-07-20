package com.gws.crm.core.notes.service.imp;


import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.notes.dtos.CreateNoteDTO;
import com.gws.crm.core.notes.dtos.NoteCriteria;
import com.gws.crm.core.notes.dtos.NoteResponseDTO;
import com.gws.crm.core.notes.entity.Note;
import com.gws.crm.core.notes.entity.NoteTarget;
import com.gws.crm.core.notes.entity.NoteType;
import com.gws.crm.core.notes.entity.TargetType;
import com.gws.crm.core.notes.mapper.NoteMapper;
import com.gws.crm.core.notes.repository.NoteRepository;
import com.gws.crm.core.notes.repository.NoteTypeRepository;
import com.gws.crm.core.notes.service.NoteService;
import com.gws.crm.core.notes.spcification.NoteSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final UserRepository userRepository;
    private final NoteTypeRepository noteTypeRepository;

    @Override
    public ResponseEntity<?> createNote(CreateNoteDTO dto, Transition transition) {
        Note note = toEntity(dto, transition);
        Note saved = noteRepository.save(note);
        NoteResponseDTO responseDTO = noteMapper.toResponseDTO(saved);
        return success(responseDTO);
    }

    @Override
    public ResponseEntity<?> getNote(Long id, Transition transition) {
        NoteResponseDTO responseDTO  =  noteRepository.findById(id)
                .map(noteMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        return success(responseDTO);
    }

    @Override
    public ResponseEntity<?> deleteNote(Long id, Transition transition) {
        if (!noteRepository.existsById(id)) throw new EntityNotFoundException("Note not found");
        noteRepository.deleteById(id);
        return success();
    }

    @Override
    public ResponseEntity<?> getAllNotes(NoteCriteria criteria, Transition transition) {
        Specification<Note> spec = NoteSpecification.filter(criteria,transition.getUserId(),transition.getUserId());
        Pageable pageable = PageRequest.of(criteria.getPage(),criteria.getSize());
        Page<NoteResponseDTO> notesResponsePage = noteRepository.findAll(spec, pageable)
                .map(noteMapper::toResponseDTO);
        return success(notesResponsePage);
    }

    private Note toEntity(CreateNoteDTO createNoteDTO,Transition transition){
        User user = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        NoteType noteType = noteTypeRepository.findByName("Private");
        return Note.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description(createNoteDTO.getDescription())
                .title(createNoteDTO.getTitle())
                .targetId(user.getId())
                .creator(user)
                .label(createNoteDTO.getLabel())
                .type(noteType)
                .favorite(false)
                .archived(false)
                .build();
    }



}

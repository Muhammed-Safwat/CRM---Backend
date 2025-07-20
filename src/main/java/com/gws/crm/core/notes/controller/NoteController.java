package com.gws.crm.core.notes.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.notes.dtos.CreateNoteDTO;
import com.gws.crm.core.notes.dtos.NoteCriteria;
import com.gws.crm.core.notes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("add")
    public ResponseEntity<?> create(@RequestBody CreateNoteDTO dto, Transition transition) {
        return noteService.createNote(dto, transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id,Transition transition) {
        return noteService.getNote(id,transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,Transition transition) {
      return  noteService.deleteNote(id,transition);
    }

    @PostMapping("all")
    public ResponseEntity<?> getNotes(
            @RequestBody NoteCriteria criteria , Transition transition) {
        return noteService.getAllNotes(criteria, transition);
    }


}


package com.gws.crm.core.notes.mapper;

import com.gws.crm.core.notes.dtos.NoteResponseDTO;
import com.gws.crm.core.notes.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    NoteResponseDTO toResponseDTO(Note note);

    List<NoteResponseDTO> toResponseDTOList(List<Note> notes);

    default Page<NoteResponseDTO> toResponseDTOPage(Page<Note> notePage) {
        return notePage.map(this::toResponseDTO);
    }


}
package com.gws.crm.core.notes.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteTarget {

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;
}

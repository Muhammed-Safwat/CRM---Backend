package com.gws.crm.core.notes.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "note_types",
        indexes = {
                @Index(name = "idx_note_name", columnList = "name")
        })
public class NoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)

    private String name;
}

package com.gws.crm.core.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String reply;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private Employee employee;

}

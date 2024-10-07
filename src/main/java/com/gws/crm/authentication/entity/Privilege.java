package com.gws.crm.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @org.hibernate.annotations.Index(name = "privilege_name_idx")
    private String name;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String labelValue;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "privilege_group_id")
    @JsonIgnore
    private PrivilegeGroup privilegeGroup;

    private boolean defaultValue;
}

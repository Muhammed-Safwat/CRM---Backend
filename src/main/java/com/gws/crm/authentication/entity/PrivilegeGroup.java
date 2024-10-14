package com.gws.crm.authentication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String jobName;

    @OneToMany(mappedBy = "privilegeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Privilege> privileges;

}

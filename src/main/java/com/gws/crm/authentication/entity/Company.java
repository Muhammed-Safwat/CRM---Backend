package com.gws.crm.authentication.entity;

import com.gws.crm.core.admin.entity.Admin;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String logoUrl;
    private String email;
    private String phone;
    private String website;
    private String address;
    @Column(length = 2000)
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin ;

}

package com.gws.crm.core.lookups.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gws.crm.core.employee.entity.Admin;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table( indexes = {
        @Index(name = "idx_project_name", columnList = "name", unique = true)
})
@SQLDelete(sql = "UPDATE base_lookup SET deleted = true WHERE id = ?")
//@SQLRestriction("deleted = false")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinFormula("(select r.id from region r where r.id = region_id)")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinFormula("(select c.id from category c where c.id = category_id)")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinFormula("(select d.id from dev_company d where d.id = dev_company_id)")
    private DevCompany devCompany;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean deleted = false ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;
}

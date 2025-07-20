package com.gws.crm.core.resale.entities;


import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.entity.PropertyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class Resale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(unique = true, nullable = false)
    @org.hibernate.annotations.Index(name = "idx_phone")
    private String phone;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private ResaleType type;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private ResaleStatus status;

    private String BUA;

    private String phase;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    private PropertyType property;

    private String note;

    private String budget;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE,
                    CascadeType.PERSIST, CascadeType.DETACH})
    private Employee salesRep;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime assignAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private boolean deleted;

}

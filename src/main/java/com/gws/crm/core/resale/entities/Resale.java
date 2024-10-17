package com.gws.crm.core.resale.entities;


import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.lookups.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

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
    private ResaleType type ;

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private boolean deleted;

}

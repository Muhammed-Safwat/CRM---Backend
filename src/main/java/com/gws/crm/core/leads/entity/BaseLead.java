package com.gws.crm.core.leads.entity;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<PhoneNumber> phoneNumbers;

    private String email;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    private LocalDateTime createdAt;

    private String country;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private Project project;

    private boolean deleted;

    private LocalDateTime lastActionDate;

    private LocalDateTime nextActionDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserAction> actions = new ArrayList<>();

    @Column(name = "delay")
    private boolean delay;

    private boolean reviewedBySales;

    private boolean archive;

    private String lastActionComment;

}

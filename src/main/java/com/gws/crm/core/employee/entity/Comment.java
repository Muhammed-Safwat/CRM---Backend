package com.gws.crm.core.employee.entity;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.leads.entity.SalesLead;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Reply> replies;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private SalesLead lead;
}

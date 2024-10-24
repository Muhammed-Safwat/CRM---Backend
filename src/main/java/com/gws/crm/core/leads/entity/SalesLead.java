package com.gws.crm.core.leads.entity;

import com.gws.crm.core.employee.entity.ActionOnLead;
import com.gws.crm.core.employee.entity.Comment;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.lookups.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@SuperBuilder
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SalesLead extends BaseLead {

    private String contactTime;

    private String whatsappNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private InvestmentGoal investmentGoal;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private CommunicateWay communicateWay;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private CancelReasons cancelReasons;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    private Employee salesRep;

    private String budget;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(nullable = false)
    private LeadStatus status;

    private String jobTitle;

    private String campaignId;

    private String lastStage;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private Broker broker;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ActionOnLead> actions = new ArrayList<>();
/*
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
*/
}

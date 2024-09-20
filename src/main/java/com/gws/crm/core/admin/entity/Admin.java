package com.gws.crm.core.admin.entity;


import com.gws.crm.authentication.entity.User;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@SuperBuilder
public class Admin extends User {

    // Maximum number of users (employees) the admin can create
    private int maxNumberOfUsers;
/*
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "admin")
    private List<Stage> stages ;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "admin")
    private List<Area> areas;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "admin")
    private List<CommunicateWay> communicateWays;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "admin")
    private List<Channel> channels;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "admin")
    private List<Region> regions;

 */
}

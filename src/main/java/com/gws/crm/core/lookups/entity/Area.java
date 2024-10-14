package com.gws.crm.core.lookups.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.service.impl.BaseLookupServiceImpl;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
public class Area extends BaseLookup {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    private Region region;

}

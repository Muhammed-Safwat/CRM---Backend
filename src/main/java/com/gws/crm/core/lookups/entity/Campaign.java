package com.gws.crm.core.lookups.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Campaign  extends BaseLookup{
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}

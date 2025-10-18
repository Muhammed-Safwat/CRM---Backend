package com.gws.crm.core.lookups.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
@Table( indexes = {
        @Index(name = "idx_area_name", columnList = "name", unique = true)
})
@Data
@SQLDelete(sql = "UPDATE base_lookup SET deleted = true WHERE id = ?")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Area extends BaseLookup {

    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

}
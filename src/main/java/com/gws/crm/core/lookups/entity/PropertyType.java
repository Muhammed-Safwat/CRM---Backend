package com.gws.crm.core.lookups.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table( indexes = {
        @Index(name = "idx_property_type_name", columnList = "name", unique = true)
})
@SQLDelete(sql = "UPDATE base_lookup SET deleted = true WHERE id = ?")
//@SQLRestriction("deleted = false")
@SuperBuilder
@NoArgsConstructor
public class PropertyType extends BaseLookup {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;
}

package com.gws.crm.core.lookups.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table( indexes = {
        @Index(name = "idx_channel_name", columnList = "name", unique = true)
})
@SQLDelete(sql = "UPDATE base_lookup SET deleted = true WHERE id = ?")
//@SQLRestriction("deleted = false")
@SuperBuilder
@Getter
@NoArgsConstructor
public class Channel extends BaseLookup {

}

package com.gws.crm.core.export.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gws.crm.authentication.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "export_task")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String type;

    @Enumerated(EnumType.STRING)
    private ExportStatus status;

    private Long sizeBytes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String filePath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User exportedBy;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "export_task_ids",
            joinColumns = @JoinColumn(name = "export_task_id"))
    @Column(name = "entity_id")
    private List<Long> exportIds;

    @Column(length = 1000)
    private String errorMessage;

    private String referenceType;
}
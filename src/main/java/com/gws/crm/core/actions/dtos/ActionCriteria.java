package com.gws.crm.core.actions.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ActionCriteria {

  private int page;
  private int size;

  private List<LocalDateTime> createdAt;
  private LocalDateTime createdAtFrom;
  private LocalDateTime createdAtTo;

  private String type;
  private List<String> types;

  private Long creatorId;
  private List<Long> creatorIds;
  private String creatorName;

  private Long leadId;
  private List<Long> leadIds;
  private String leadType;

  private String keyword;
  private String description;
  private String comment;

  private LocalDateTime actionDateFrom;
  private LocalDateTime actionDateTo;

  private Boolean hasComment;
  private Boolean hasCallBackTime;
  private Boolean hasNextActionDate;

  private String callOutcome;
  private List<String> callOutcomes;

  private String stage;
  private List<String> stages;

  private String cancellationReason;
  private List<String> cancellationReasons;

  private String sortBy = "createdAt";
  private String sortDirection = "DESC";

}

package com.gws.crm.core.leads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesRepCountDTO {
  private Long userId;
  private String userName;
  private String userImage;
  private Long totalLeads;
}

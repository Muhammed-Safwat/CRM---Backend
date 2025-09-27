package com.gws.crm.core.export.dtos;



import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportResponseDto  {

  private Long id;

  private String filename;

  private String type;

  private String status;

  private Long sizeBytes;

  private LocalDateTime createdAt;

  private String filePath ;

  private EmployeeSimpleDTO exportedBy ;

}
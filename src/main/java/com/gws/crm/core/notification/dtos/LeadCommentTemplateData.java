package com.gws.crm.core.notification.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LeadCommentTemplateData extends BaseTemplateData {

    private String assignedUser;
    private String leadStatus;
    private String commentText;
    private String commentAuthor;
    private String commentDate;
    private String commentType;
    private String commentTypeClass;
    private String leadScore;
    private String daysInPipeline;
    private String lastActivity;
    private String totalComments;

    @Override
    protected void addSpecificVariables(Map<String, String> variables) {
        variables.put("ASSIGNED_USER", assignedUser);
        variables.put("LEAD_STATUS", leadStatus);
        variables.put("COMMENT_TEXT", commentText);
        variables.put("COMMENT_AUTHOR", commentAuthor);
        variables.put("COMMENT_DATE", commentDate);
        variables.put("COMMENT_TYPE", commentType);
        variables.put("COMMENT_TYPE_CLASS", commentTypeClass);
        variables.put("LEAD_SCORE", leadScore);
        variables.put("DAYS_IN_PIPELINE", daysInPipeline);
        variables.put("LAST_ACTIVITY", lastActivity);
        variables.put("TOTAL_COMMENTS", totalComments);
    }
}

package io.tis.zoho.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeLogDTO {
    private String clientName;
    private String projectName;
    private String jobName;
    private String workItem;
    private String description;
    private String workDate;
    private String fromTime;
    private String toTime;
    private boolean billable;
}

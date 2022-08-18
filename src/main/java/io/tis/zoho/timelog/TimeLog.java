package io.tis.zoho.timelog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeLog {
    private String userEmail;
    private String projectName;
    private String jobName;
    private String workItem;
    private String description;
    private String workDate;
    private String fromTime;
    private String toTime;
    private boolean billable;
}

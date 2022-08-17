package io.tis.zoho;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeLog {
    private String projectName;
    private String jobName;
    private String workItem;
    private String description;
    private LocalDateTime workTime;
    private boolean billable;
}

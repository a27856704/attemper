package com.github.attemper.common.result.dispatch.trigger.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonTriggerResult {

    protected String triggerName;

    protected String jobName;

    protected String triggerType;

    protected List<String> calendarNames;

    protected Date startTime;

    protected Date endTime;

    protected String tenantId;

}
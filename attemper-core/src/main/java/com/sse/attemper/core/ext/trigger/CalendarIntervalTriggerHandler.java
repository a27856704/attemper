package com.sse.attemper.core.ext.trigger;

import com.sse.attemper.config.bean.ContextBeanAware;
import com.sse.attemper.core.dao.mapper.job.TriggerMapper;
import com.sse.attemper.sdk.common.constant.SdkCommonConstants;
import com.sse.attemper.sdk.common.param.dispatch.trigger.sub.CalendarIntervalTriggerParam;
import com.sse.attemper.sdk.common.result.dispatch.trigger.sub.CalendarIntervalTriggerResult;
import com.sse.attemper.sys.holder.TenantHolder;
import com.xiaoleilu.hutool.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.Trigger;
import org.quartz.impl.jdbcjobstore.Constants;

import java.util.*;

public class CalendarIntervalTriggerHandler implements TriggerHandler<CalendarIntervalTriggerParam, CalendarIntervalTriggerResult> {

    @Override
    public List<CalendarIntervalTriggerResult> getTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        return ContextBeanAware.getBean(TriggerMapper.class).getCalendarIntervalTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void deleteTriggers(Map<String, Object> jobNameWithTenantIdMap) {
        ContextBeanAware.getBean(TriggerMapper.class).deleteCalendarIntervalTriggers(jobNameWithTenantIdMap);
    }

    @Override
    public void saveTriggers(String jobName, List<CalendarIntervalTriggerParam> paramOfTriggers) {
        List<Map<String, Object>> mapList = new ArrayList<>(paramOfTriggers.size());
        paramOfTriggers.forEach(item -> {
            item.setTriggerType(Constants.TTYPE_CAL_INT);
            if (StringUtils.isBlank(item.getTimeZoneId()) || TimeZone.getTimeZone(item.getTimeZoneId()) == null) {
                item.setTimeZoneId(TimeZone.getDefault().getID());
            }
            Map<String, Object> map = BeanUtil.beanToMap(item);
            map.put(SdkCommonConstants.jobName, jobName);
            map.put(SdkCommonConstants.tenantId, TenantHolder.get().getId());
            mapList.add(map);
        });
        ContextBeanAware.getBean(TriggerMapper.class).saveCalendarIntervalTriggers(mapList);
    }

    @Override
    public Set<Trigger> buildTriggers(String jobName, List<CalendarIntervalTriggerParam> paramOfTriggers) {
        Set<Trigger> quartzTriggers = new HashSet(paramOfTriggers.size());
        paramOfTriggers.forEach(
                item -> {
                    Trigger trigger = triggerBuilder(item).withSchedule(
                            CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                            .inTimeZone(TimeZone.getTimeZone(item.getTimeZoneId()))
                            .withRepeatCount(item.getRepeatCount())
                            .preserveHourOfDayAcrossDaylightSavings(item.isPreserveDayLight())
                            .skipDayIfHourDoesNotExist(item.isSkipDayIfNoHour())
                            .withInterval(item.getInterval(), DateBuilder.IntervalUnit.valueOf(item.getTimeUnit())))
                            .build();
                    quartzTriggers.add(trigger);
                });
        return quartzTriggers;
    }
}

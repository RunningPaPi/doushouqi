package com.artqiyi.doushouqi.service;

import org.quartz.Scheduler;

import com.artqiyi.doushouqi.job.domain.ScheduleJob;
import com.artqiyi.doushouqi.job.domain.ScheduleJobExample;
import com.artqiyi.doushouqi.job.domain.ScheduleJobFailLog;

import java.util.List;


public interface IQuartzService {
    Scheduler getScheduler();
    void startJobs();
    boolean addJob(ScheduleJob job);
    boolean updatejob(ScheduleJob job);
    boolean deletJob(ScheduleJob job);
    boolean startJob(ScheduleJob job);
    boolean pauseJob(ScheduleJob job);
    List<ScheduleJob> selectByExample(ScheduleJobExample scheduleJobExample);
    void updateByPrimaryKeySelective(ScheduleJob scheduleJob);
    void addJobFailLog(ScheduleJobFailLog scheduleJobFailLog);

}

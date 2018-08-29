package com.artqiyi.doushouqi.game.impl;

import com.artqiyi.doushouqi.common.constant.SystemConstant;
import com.artqiyi.doushouqi.common.util.DateUtil;
import com.artqiyi.doushouqi.game.DoushouqiGameService;
import com.artqiyi.doushouqi.game.IGameJobService;
import com.artqiyi.doushouqi.game.vo.GameFightData;
import com.artqiyi.doushouqi.job.domain.ScheduleJob;
import com.artqiyi.doushouqi.service.IQuartzService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 游戏相关定时任务
 */
@Service
public class GameJobServiceImpl implements IGameJobService{
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IQuartzService quartzService;

    /**
     * 保存好友对战游戏数据任务
     */
    public void dealGameData(){
        logger.info("【斗兽棋结算】 设置游戏数据处理任务");
        ScheduleJob job = new ScheduleJob();
        job.setClazzName(null);
        job.setIsConcurrent(false);

        job.setCronExpression("0 0 0 * * ?");
        job.setDescription("game data");
        job.setIsSpringbean(true);
        job.setJobGroup("gameData");
        job.setJobName("dealData4Game");
        job.setJobstatus(SystemConstant.TASK_STATUS_READY);
        job.setTargetMethod("dealData");
        job.setTargetObject("gameStatisticsService");

        quartzService.addJob(job);
        logger.info("【斗兽棋结算】 设置游戏数据处理任务完毕");
    }

    /**
     * 任务移除
     * @param groupName
     * @param JobName
     */
    @Override
    public void removeGameJob(String groupName, String JobName) {
        ScheduleJob jobForEnd = new ScheduleJob();
        jobForEnd.setJobName(JobName);
        jobForEnd.setJobGroup(groupName);
        quartzService.deletJob(jobForEnd);
    }

}

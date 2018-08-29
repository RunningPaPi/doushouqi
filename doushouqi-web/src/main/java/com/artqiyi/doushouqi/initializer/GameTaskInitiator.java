package com.artqiyi.doushouqi.initializer;

import com.artqiyi.doushouqi.game.IGameJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class GameTaskInitiator implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger log = LoggerFactory.getLogger(GameTaskInitiator.class);

    @Autowired
    private IGameJobService gameJobService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {

                gameJobService.dealGameData();
            } catch (Exception e){
                log.error("【定时任务】启动出错!  {}", e);
            }
        }
    }

}

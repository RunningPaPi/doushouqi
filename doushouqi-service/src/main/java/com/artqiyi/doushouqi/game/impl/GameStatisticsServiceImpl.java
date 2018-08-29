package com.artqiyi.doushouqi.game.impl;

import com.artqiyi.doushouqi.common.constant.RedisFiledConstant;
import com.artqiyi.doushouqi.game.GameStatisticsService;
import com.artqiyi.doushouqi.game.vo.GameUserRecord;
import com.artqiyi.doushouqi.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameStatisticsService")
public class GameStatisticsServiceImpl implements GameStatisticsService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public void statistics(Long userId, String type) {
//        String gameKey = null;
        String userKey = null;
        if ("MATCH".equals(type)) {
//            gameKey = RedisFiledConstant.DSQ_GAME_MATCH_RECORD;
            userKey = RedisFiledConstant.DSQ_GAME_MATCH_USER_RECORD;
        } else {
//            gameKey = RedisFiledConstant.DSQ_GAME_FIGHT_RECORD;
            userKey = RedisFiledConstant.DSQ_GAME_FIGHT_USER_RECORD;
        }
//        GameRecord record = redisClient.get(gameKey, GameRecord.class);
//        if (record == null) {
//            record = new GameRecord();
//            record.setContestNum(1);
//            record.setGameType(type);
//            record.setPlayTimes(0);
//            record.setCreateTime(new Date());
//        } else {
//            record.setContestNum(record.getContestNum()+1);
//        }
        //游戏数据
//        redisClient.set(gameKey, record);
        //玩家数据
        GameUserRecord playRecord = redisClient.hGet(userKey, userId.toString(), GameUserRecord.class);
        if (playRecord == null) {
//            //参赛人数+1
//            record.setContestNum(record.getContestNum() + 1);
            playRecord = new GameUserRecord();
            playRecord.setUserId(userId);
            playRecord.setPlayTimes(1);
            playRecord.setWinTimes(0);
        } else {
            playRecord.setPlayTimes(playRecord.getPlayTimes() + 1);
        }
        //玩家数据
        redisClient.hSet(userKey, userId.toString(), playRecord);
    }

    public GameUserRecord getUserRecord(Long userId){
        GameUserRecord match = redisClient.hGet(RedisFiledConstant.DSQ_GAME_MATCH_USER_RECORD, userId.toString(), GameUserRecord.class);
        GameUserRecord fight = redisClient.hGet(RedisFiledConstant.DSQ_GAME_FIGHT_USER_RECORD, userId.toString(), GameUserRecord.class);
        GameUserRecord userRecord = new GameUserRecord();
        int pTimes = match==null?0:match.getPlayTimes()+(fight==null?0:fight.getPlayTimes());
        int mTimes = match==null?0:match.getWinTimes()+(fight==null?0:fight.getWinTimes());

        userRecord.setPlayTimes(pTimes);
        userRecord.setWinTimes(mTimes);
        return userRecord;
    }

    public void dealData(Object o){
        redisClient.del(RedisFiledConstant.DSQ_GAME_MATCH_USER_RECORD);
        redisClient.del(RedisFiledConstant.DSQ_GAME_FIGHT_USER_RECORD);
    }

}

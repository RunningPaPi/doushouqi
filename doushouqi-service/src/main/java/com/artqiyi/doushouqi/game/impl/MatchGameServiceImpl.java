package com.artqiyi.doushouqi.game.impl;

import com.alibaba.fastjson.JSON;
import com.artqiyi.doushouqi.common.constant.RedisFiledConstant;
import com.artqiyi.doushouqi.common.socket.SocketConstant;
import com.artqiyi.doushouqi.common.socket.SocketResponseMsg;
import com.artqiyi.doushouqi.common.util.JSONUtil;
import com.artqiyi.doushouqi.game.DoushouqiGameService;
import com.artqiyi.doushouqi.game.GameStatisticsService;
import com.artqiyi.doushouqi.game.IMatchGameService;
import com.artqiyi.doushouqi.game.vo.GameData;
import com.artqiyi.doushouqi.game.vo.PosData;
import com.artqiyi.doushouqi.redis.RedisClient;
import com.artqiyi.doushouqi.user.service.vo.UserVo;
import com.artqiyi.doushouqi.websocket.service.WebSocketSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.List;
import java.util.Random;

@Service("matchGameService")
public class MatchGameServiceImpl implements IMatchGameService {
    private static Logger log = LoggerFactory.getLogger(MatchGameServiceImpl.class);

    private static final String GAME_TYPE = "MATCH";
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private GameStatisticsService gameStatisticsService;

    private DoushouqiGameService gameService = DoushouqiGameService.INSTANCE;

    private WebSocketSessionService webSocketSessionService = WebSocketSessionService.INSTANCE;

    @Override
    public synchronized void startGame(Long userId) {
        gameStatisticsService.statistics(userId, GAME_TYPE);
        GameData userRecord = redisClient.get(RedisFiledConstant.DSQ_MATCH_USER_WAIT, GameData.class);
        gameService.removeAgainst(userId);
        if (userRecord == null) {
            gameService.setMatchUserId(userId);
            userRecord = initUserGameInfo(userId);
            redisClient.setWithExpire(RedisFiledConstant.DSQ_MATCH_USER_WAIT, userRecord, 30);
        } else if (!userId.equals(userRecord.getUserId())){
            redisClient.del(RedisFiledConstant.DSQ_MATCH_USER_WAIT);
            List<PosData> posData = gameService.genGameData(userRecord.getUserId(), userId);
            userRecord.setGameData(posData);
            GameData matchRecord = initUserGameInfo(userId);
            matchRecord.setGameData(posData);
            Random random = new Random();
            boolean isTurn = random.nextBoolean();
            userRecord.setTurn(isTurn);
            matchRecord.setTurn(!isTurn);

            userRecord.setAgainstGender(matchRecord.getGender());
            userRecord.setAgainstHeadUrl(matchRecord.getHeadUrl());
            userRecord.setAgainstId(matchRecord.getUserId());
            userRecord.setAgainstNickName(matchRecord.getNickName());

            matchRecord.setAgainstGender(userRecord.getGender());
            matchRecord.setAgainstHeadUrl(userRecord.getHeadUrl());
            matchRecord.setAgainstId(userRecord.getUserId());
            matchRecord.setAgainstNickName(userRecord.getNickName());
            gameService.addAgainst(userId, userRecord.getUserId());
            gameService.addAgainst(userRecord.getUserId(), userId);

            sendMessage(userRecord, GAME_TYPE, userRecord.getUserId(), SocketConstant.DSQ_MATCH_START);
            sendMessage(matchRecord, GAME_TYPE, userId, SocketConstant.DSQ_MATCH_START);
        }
    }

    private GameData initUserGameInfo(Long userId) {
        UserVo user = redisClient.hGet(RedisFiledConstant.USER, userId.toString(), UserVo.class);
        GameData userRecord = new GameData();
        userRecord.setNickName(user.getNickName());
        userRecord.setGender(user.getGender());
        userRecord.setHeadUrl(user.getHeadPicUrl());
        userRecord.setUserId(userId);
        return userRecord;
    }


    @Override
    public void transData(Long userId, TextMessage message) {
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null){
            webSocketSessionService.sendMessageToUser(GAME_TYPE, againstId.toString(), message);
        }
    }

    @Override
    public void emo(Long userId, TextMessage message) {
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            webSocketSessionService.sendMessageToUser(GAME_TYPE, againstId.toString(), message);
        }
    }

    @Override
    public void giveUp(long userId) {
        log.info("【对手逃跑】");
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            sendMessage(null, GAME_TYPE, againstId, SocketConstant.DSQ_MATCH_GIVE_UP,"对手逃跑了");
            gameService.addGiveUp(userId);
            return;
        }
        sendMessage(null, GAME_TYPE, userId, SocketConstant.DSQ_MATCH_DATA_TRANS_FAIL, "对手掉线或逃跑");
        log.error("【对手逃跑】 数据未发送，对手数据不存在！");
    }

    @Override
    public void gameOver(Long userId) {
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            gameService.removeAgainst(againstId);
        }
        gameService.removeAgainst(userId);
    }

    @Override
    public void gameDataSave() {

    }

    @Override
    public void offline(Long userId) {
        //如果是正在匹配的对手，需要将其清除在匹配区的数据
        if (userId.equals(gameService.getMatchUserId())){
            redisClient.del(RedisFiledConstant.DSQ_MATCH_USER_WAIT);
        }
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null){
            if (null == gameService.getGiveUp(userId)) {
                sendMessage(null, GAME_TYPE, againstId, SocketConstant.DSQ_MATCH_END, "offLine");
            } else {
                gameService.removeGiveUp(userId);
            }
        }
        gameService.removeAgainst(userId);
    }

    private void sendMessage(Object data, String group, Long userId, String msgCode) {
        sendMessage(data, group, userId, msgCode, null);
    }

    private void sendMessage(Object data, String group, Long userId, String msgCode, String msg) {
        SocketResponseMsg socketData = new SocketResponseMsg();
        socketData.setCode(msgCode);
        socketData.setSuccess(true);
        socketData.setData(data);
        socketData.setMsg(msg);
        webSocketSessionService.sendMessageToUser(group, userId.toString(), new TextMessage(JSONUtil.toJson(socketData)));
    }

    public static void main(String[] args) {
        GameData userRecord = new GameData();
        userRecord.setNickName("韩立");
        userRecord.setGender(0);
        userRecord.setHeadUrl("http://");
        userRecord.setUserId(1L);
        userRecord.setGameData(DoushouqiGameService.INSTANCE.genGameData(userRecord.getUserId(), 2L));
        userRecord.setAgainstGender(2);
        userRecord.setAgainstHeadUrl("http://sdfsf");
        userRecord.setAgainstId(2L);
        userRecord.setAgainstNickName("石牧");
        SocketResponseMsg msg = new SocketResponseMsg();
        msg.setCode(SocketConstant.DSQ_MATCH_START);
        msg.setSuccess(true);
        msg.setData(userRecord);
        System.out.println(JSON.toJSON(msg));
    }

}

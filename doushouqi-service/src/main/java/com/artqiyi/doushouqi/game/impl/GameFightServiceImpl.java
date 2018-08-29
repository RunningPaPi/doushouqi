package com.artqiyi.doushouqi.game.impl;

import com.alibaba.fastjson.JSON;
import com.artqiyi.doushouqi.common.constant.RedisFiledConstant;
import com.artqiyi.doushouqi.common.socket.SocketConstant;
import com.artqiyi.doushouqi.common.socket.SocketResponseMsg;
import com.artqiyi.doushouqi.common.util.DateUtil;
import com.artqiyi.doushouqi.common.util.JSONUtil;
import com.artqiyi.doushouqi.game.DoushouqiGameService;
import com.artqiyi.doushouqi.game.GameStatisticsService;
import com.artqiyi.doushouqi.game.IGameFightService;
import com.artqiyi.doushouqi.game.vo.FightRoom;
import com.artqiyi.doushouqi.game.vo.GameFightData;
import com.artqiyi.doushouqi.game.vo.PosData;
import com.artqiyi.doushouqi.redis.RedisClient;
import com.artqiyi.doushouqi.user.domain.User;
import com.artqiyi.doushouqi.user.service.IUserService;
import com.artqiyi.doushouqi.user.service.vo.UserVo;
import com.artqiyi.doushouqi.websocket.service.WebSocketSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.*;

/**
 * 好友对战模式
 */
@Service("gameFightService")
public class GameFightServiceImpl implements IGameFightService {
    private static Logger log = LoggerFactory.getLogger(GameFightServiceImpl.class);
    private static final String group = "FIGHT";

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private GameStatisticsService gameStatisticsService;

    private DoushouqiGameService gameService = DoushouqiGameService.INSTANCE;

    private WebSocketSessionService webSocketSessionService = WebSocketSessionService.INSTANCE;

    /**
     * 创建房间
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public String newRoom(Long userId) {
        //重复创建房间，销毁上一次创建的
        leave(userId);
        String lastRoomNo = gameService.getRoomNo(userId);
        if (lastRoomNo != null) {
            redisClient.hDel(RedisFiledConstant.DSQ_FIGHT_ROOM, lastRoomNo);
        }
        String roomNo = DateUtil.formatDate(new Date(), DateUtil.DATESEC_FORMAT_NO_DASH) + "_" + userId;//房间号
        GameFightData gameData = initPlayerInfo(userId);
        if (gameData == null) {
            return null;
        }
        gameData.setRoomNo(roomNo);
        gameData.setInviter(true);
        FightRoom room = new FightRoom();
        room.setMasterId(userId);
        room.setMasterInfo(gameData);
        redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
        gameService.addRoomNo(userId, roomNo);
        Map map = new HashMap(2);
        map.put("roomNo", roomNo);
        sendMessage(map, group, userId, SocketConstant.DSQ_FIGHT_CREATE_ROOM);
        log.info("【创建房间】userId={},roomNo为{}", userId, roomNo);
        return roomNo;
    }

    @Autowired
    IUserService userService;

    private GameFightData initPlayerInfo(Long userId) {
//        UserVo user = redisClient.hGet(RedisFiledConstant.USER, userId.toString(), UserVo.class);
        User user = userService.selectByUserId(userId);
        if (user == null) {
            return null;
        }
        GameFightData gameData = new GameFightData();
        gameData.setNickName(user.getNickName());
        gameData.setGender(user.getGender());
        gameData.setHeadUrl(user.getHeadPicUrl());
        gameData.setUserId(userId);
        return gameData;
    }

    /**
     * 加入房间
     *
     * @param userId 用户id
     * @param roomNo 房间编码
     */
    @Override
    public void enterRoom(Long userId, String roomNo) {
        log.info("【加入房间】userId={},roomNo={}", userId, roomNo);
        //并发控制
        try {
            if (!redisClient.setNx(RedisFiledConstant.DSQ_ENTER_ROOM + roomNo, "enterRoom")) {
                log.info("【加入房间】多人同时进入同一房间 roomNo={}", roomNo);
                sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_ENTER_ROOM_FAIL, "房间已满");
                return;
            }
            roomNo = roomNo.trim();
            //获取邀战玩家
            FightRoom room = getRoom(roomNo);
            if (room == null) {
                log.info("【加入房间】该游戏房间不存在！roomNo={}", roomNo);
                sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_ENTER_ROOM_FAIL, "房间已解散");
                return;
            }

            if (room.getMatchInfo() != null) {
                log.info("【加入房间】该房间已满！roomNo={}", roomNo);
                sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_ENTER_ROOM_FAIL, "房间已满");
                return;
            }

            GameFightData invitePlayer = room.getMasterInfo();
            //是否为同一玩家
            if (invitePlayer.getUserId().longValue() == userId.longValue()) {
                log.info("【加入房间】玩家进入自己的房间！userId={},roomNo={}", invitePlayer.getUserId(), roomNo);
                return;
            } else {
                log.info("【加入房间】该游戏当前赛场有玩家等待，进行匹配！roomNo={}", roomNo);
                GameFightData invitedPlayer = initPlayerInfo(userId);

                //向发起邀请的玩家推送消息
                invitePlayer.setAgainstId(userId);
                invitePlayer.setAgainstHeadUrl(invitedPlayer.getHeadUrl());
                invitePlayer.setAgainstNickName(invitedPlayer.getNickName());
                invitePlayer.setAgainstGender(invitedPlayer.getGender());
                invitePlayer.setInviter(true);
                String msgCode = SocketConstant.DSQ_FIGHT_ENTER_ROOM;
                sendMessage(invitePlayer, group, invitePlayer.getUserId(), msgCode);

                //向被邀请的玩家推送消息
                invitedPlayer.setRoomNo(invitePlayer.getRoomNo());
                invitedPlayer.setAgainstId(invitePlayer.getUserId());
                invitedPlayer.setAgainstHeadUrl(invitePlayer.getHeadUrl());
                invitedPlayer.setAgainstNickName(invitePlayer.getNickName());
                invitedPlayer.setAgainstGender(invitePlayer.getGender());
                room.setMatchInfo(invitedPlayer);

                sendMessage(invitedPlayer, group, userId, msgCode);
                //玩家对应的房间号
                gameService.addRoomNo(userId, roomNo);
                gameService.addAgainst(userId, invitePlayer.getUserId());
                gameService.addAgainst(invitePlayer.getUserId(), userId);
                redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
            }
        } finally {
            redisClient.del(RedisFiledConstant.DSQ_ENTER_ROOM + roomNo);
        }
    }

    public static void main(String[] args) {
        GameFightData data = new GameFightData();
        List<PosData> posData = DoushouqiGameService.INSTANCE.genGameData(1L, 2L);
        data.setUserId(1L);
        data.setHeadUrl("http://");
        data.setNickName("韩立");
        data.setGender(2);
        data.setAgainstId(2L);
        data.setAgainstHeadUrl("http://");
        data.setAgainstNickName("石牧");
        data.setAgainstGender(0);
        data.setInviter(true);
        data.setGameData(posData);
        String msgCode = SocketConstant.DSQ_FIGHT_ENTER_ROOM;
        SocketResponseMsg msg = new SocketResponseMsg();
        msg.setCode(msgCode);
        msg.setSuccess(true);
        msg.setData(data);
        System.out.println(JSON.toJSONString(msg));
    }

    /**
     * 开始游戏
     *
     * @param userId
     */
    @Override
    public void start(Long userId) {
        log.info("【开始游戏】");
        //获取房间号
        String roomNo = gameService.getRoomNo(userId);
        //获取当前用户对战记录
        FightRoom room = getRoom(roomNo);
        if (room == null) {
            return;
        }
        if (!room.getMasterId().equals(userId)){
            log.error("【开始游戏】不是房主不能开始游戏!");
            return;
        }
        GameFightData master = room.getMasterInfo();
        GameFightData match = room.getMatchInfo();
        Long againstId = match.getUserId();
        //初始化游戏数据
        List<PosData> posData = gameService.genGameData(userId, againstId);
        master.setGameData(posData);
        Random random = new Random();
        boolean isTurn = random.nextBoolean();
        master.setTurn(isTurn);

        //游戏已经开始
        master.setGameStarted(true);
        sendMessage(master, group, userId, SocketConstant.DSQ_FIGHT_START);
        redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_USER_RECORD, userId.toString(), master);

        match.setGameData(posData);
        match.setTurn(!isTurn);
        match.setGameStarted(true);
        sendMessage(match, group, againstId, SocketConstant.DSQ_FIGHT_START);
        room.setGameStarted(true);
        master.setGameData(null);
        match.setGameData(null);
        redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
        //游戏统计
        gameStatisticsService.statistics(userId, group);
        gameStatisticsService.statistics(againstId, group);
    }

    /**
     * 数据传输
     *
     * @param userId
     * @param message
     */
    @Override
    public void dataTrans(Long userId, TextMessage message) {
        log.info("【数据传输】 data={}", message);
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            webSocketSessionService.sendMessageToUser(group, againstId.toString(), message);
            return;
        }
        sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_DATA_TRANS_FAIL, "对手掉线或逃跑");
        log.error("【数据传输】 数据未发送，对手数据不存在！");
    }

    /**
     * 逃跑
     *
     * @param userId 用户id
     */
    @Override
    public void giveUp(Long userId) {
        log.info("【逃跑】");
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            sendMessage(null, group, againstId, SocketConstant.DSQ_FIGHT_GIVE_UP, "对手逃跑了");
            String roomNo = gameService.getRoomNo(userId);
            FightRoom room = getRoom(roomNo);
            if (room != null) {
                room.setMatchInfo(null);
                room.setGameStarted(false);
                redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
            }
            return;
        }
        sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_DATA_TRANS_FAIL, "对手掉线或逃跑");
        log.error("【逃跑】 数据未发送，对手数据不存在！");
    }

    /**
     * 发送表情
     *
     * @param userId
     * @param message
     */
    @Override
    public void emo(Long userId, TextMessage message) {
        log.info("【发送表情】 data={}", message);
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            webSocketSessionService.sendMessageToUser(group, againstId.toString(), message);
            return;
        }
        sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_DATA_TRANS_FAIL, "对手掉线或逃跑");
        log.error("【发送表情】 数据未发送，对手数据不存在！");
    }

    /**
     * 再来一局
     *
     * @param userId
     */
    @Override
    public void again(Long userId) {
        log.info("【再来一局】");
        Long againstId = gameService.getAgainstId(userId);
        String roomNo = gameService.getRoomNo(userId);
        FightRoom room = getRoom(roomNo);
        log.info("room={}", JSON.toJSONString(room));
        if (room != null) {
            if (againstId != null) {
                if (userId.equals(room.getMasterId())) {
                    sendMessage(room.getMatchInfo(), group, againstId, SocketConstant.DSQ_FIGHT_AGAIN);
                } else {
                    sendMessage(room.getMasterInfo(), group, againstId, SocketConstant.DSQ_FIGHT_AGAIN);
                }
            }
        } else {
            sendMessage(null, group, userId, SocketConstant.DSQ_FIGHT_AGAIN_FAIL, "房间已解散");
        }
    }

    /**
     * 离开房间
     *
     * @param userId
     */
    @Override
    public void leave(Long userId) {
        log.info("【离开房间】userId={}", userId);
        String roomNo = gameService.getRoomNo(userId);
        gameService.removeAgainst(userId);
        FightRoom room = getRoom(roomNo);
        if (room != null) {
            gameService.removeRoomNo(userId);
            //是否是房主
            if (room.getMasterId().intValue() == userId) {
                redisClient.hDel(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo);
                if (room.getMatchInfo() != null && room.getMatchInfo().getUserId() != null) {
                    gameService.removeRoomNo(room.getMatchInfo().getUserId());
                }
                GameFightData match = room.getMatchInfo();
                if (match != null) {
                    sendMessage(null, group, match.getUserId(), SocketConstant.DSQ_FIGHT_LEAVE, "房主离开");
                }
            } else {
                room.setGameStarted(false);
                room.setMatchInfo(null);
                redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
                sendMessage(null, group, room.getMasterId(), SocketConstant.DSQ_FIGHT_LEAVE, "对手离开");
            }
        }
    }

    /**
     * 掉线
     *
     * @param userId
     */
    @Override
    public void offline(Long userId) {
        String roomNo = gameService.getRoomNo(userId);
        FightRoom room = getRoom(roomNo);
        if (room != null && !room.isGameStarted()) {
            leave(userId);
            return;
        }
        log.info("【掉线】 userId={}", userId);
        Long againstId = gameService.getAgainstId(userId);
        if (againstId != null) {
            //逃跑的不再发送掉线消息
            if (null == gameService.getGiveUp(userId)) {
                sendMessage(null, group, againstId, SocketConstant.DSQ_FIGHT_END, "offLine");
            } else {
                gameService.removeGiveUp(userId);
            }
        }

        if (room != null) {
            room.setMatchInfo(null);
            room.setGameStarted(false);
            redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
        }
    }

    /**
     * 保存数据
     *
     * @param o
     */
    @Override
    public void saveData(Object o) {

    }

    /**
     * 游戏结束
     *
     * @param userId
     */
    @Override
    public void gameOver(Long userId) {
        log.info("【游戏结束】");
        resetGameData(userId);
    }

    /**
     * 重置游戏数据
     *
     * @param userId
     */
    private void resetGameData(Long userId) {
        String roomNo = gameService.getRoomNo(userId);
        FightRoom room = getRoom(roomNo);
        if (room != null) {
            room.setGameStarted(false);
            redisClient.hSet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, room);
        }
    }

    private FightRoom getRoom(String roomNo) {
        if (roomNo != null) {
            return redisClient.hGet(RedisFiledConstant.DSQ_FIGHT_ROOM, roomNo, FightRoom.class);
        }
        return null;
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
}

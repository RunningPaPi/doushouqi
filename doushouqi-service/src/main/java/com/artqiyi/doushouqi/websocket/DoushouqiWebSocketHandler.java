package com.artqiyi.doushouqi.websocket;

import com.alibaba.fastjson.JSON;
import com.artqiyi.doushouqi.common.socket.SocketConstant;
import com.artqiyi.doushouqi.game.IGameFightService;
import com.artqiyi.doushouqi.game.IMatchGameService;
import com.artqiyi.doushouqi.websocket.service.WebSocketSessionService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;

@Component
public class DoushouqiWebSocketHandler extends TextWebSocketHandler {
    private Logger log = LoggerFactory.getLogger(DoushouqiWebSocketHandler.class);
    @Autowired
    private IMatchGameService matchGameService;
    @Autowired
    private IGameFightService gameFightService;

    private WebSocketSessionService sessionService = WebSocketSessionService.INSTANCE;

    /**
	 * 处理前端发送的文本信息 js调用websocket.send时候, 会调用该方法
	 *
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = getUserId(session);
        Map paramsMap = (Map)JSON.parse(message.getPayload());
        log.info("【socket参数】 userId={} paramsMap={}", userId, paramsMap);
        String code = MapUtils.getString(paramsMap,"code");
        Map data = MapUtils.getMap(paramsMap,"data");

        if (code != null) {
            switch (code) {
                /*=============================斗兽棋随机匹配socket相关操作=========================================*/
                case SocketConstant.DSQ_MATCH_START: //游戏开始
                    matchGameService.startGame(userId);
                    break;
                case SocketConstant.DSQ_MATCH_DATA_TRANS: //数据传输;
                    matchGameService.transData(userId, message);
                    break;
                case SocketConstant.DSQ_MATCH_EMO: //表情
                    matchGameService.emo(userId, message);
                    break;
                case SocketConstant.DSQ_MATCH_END: //游戏结束
                    matchGameService.gameOver(userId);
                    break;
                case SocketConstant.DSQ_MATCH_GIVE_UP: //逃跑
                    matchGameService.giveUp(userId);
                    break;
                /*=============================斗兽棋好友对战socket相关操作=========================================*/
                case SocketConstant.DSQ_FIGHT_CREATE_ROOM: //创建房间
                    gameFightService.newRoom(userId);
                    break;
                case SocketConstant.DSQ_FIGHT_ENTER_ROOM: //进入房间
                    gameFightService.enterRoom(userId, MapUtils.getString(data,"roomNo"));
                    break;
                case SocketConstant.DSQ_FIGHT_START: //开始
                    gameFightService.start(userId);
                    break;
                case SocketConstant.DSQ_FIGHT_DATA_TRANS: //数据传输
                    gameFightService.dataTrans(userId, message);
                    break;
                case SocketConstant.DSQ_FIGHT_EMO: //表情
                    gameFightService.emo(userId, message);
                    break;
                case SocketConstant.DSQ_FIGHT_END: //游戏结束
                    gameFightService.gameOver(userId);
                    break;
                case SocketConstant.DSQ_FIGHT_GIVE_UP: //逃跑
                    gameFightService.giveUp(userId);
                    break;
                case SocketConstant.DSQ_FIGHT_AGAIN: //再来一局
                    gameFightService.again(userId);
                    break;
                case SocketConstant.DSQ_FIGHT_LEAVE: //再来一局
                    gameFightService.leave(userId);
                    break;
                default:
                    log.info("【心跳检测】pong={}", message.getPayload());
                    break;
            }
        } else {
            log.info("message.getPayload() = {}", message.getPayload());
        }
	}

	/**
	 * 当新连接建立的时候, 连接成功时候,会触发页面上onOpen方法
	 *
	 * @param session
	 * @throws Exception
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!sessionService.getAndSetStarted(true)){
            new Thread(()->{
                while (true){
                    TextMessage msg = new TextMessage("{\"code\":\"ping\",\"msg\":\"areyouok\"}");
                    sessionService.sendMessageToUsers(msg);
                    try {
                        Thread.currentThread().sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        UserGroup userGroup = getUserIdAndGroupName(session);
        //如果建立过连接,关闭并删除旧的
        WebSocketSession oldSession = sessionService.getUser(userGroup.getUserName());
        if (oldSession != null && oldSession.isOpen()){
            oldSession.close();
        }
        sessionService.addUser(userGroup.getUserName(), session);
        sessionService.addGroupUser(userGroup.getGroupName(), userGroup.getUserName(), session);
        log.info("在线用户：{}", sessionService.getOnlineCount());
	}

	private UserGroup getUserIdAndGroupName(WebSocketSession session){
        URI uri = session.getUri();
        String[] uriSeg = uri.getPath().replace("/websocket/socketServer/","").split("/");
        return new UserGroup(uriSeg[0], uriSeg[1]);
    }

    private Long getUserId(WebSocketSession session){
        return Long.valueOf(getUserIdAndGroupName(session).getUserName());
    }


	/**
	 * 当连接关闭时被调用
	 *
	 * @param session
	 * @param status
	 * @throws Exception
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserGroup userGroup = getUserIdAndGroupName(session);
        sessionService.removeUser(userGroup.getUserName());
        sessionService.removeGroupUser(userGroup.getGroupName(), userGroup.getUserName());

        if ("MATCH".equals(userGroup.getGroupName())) {
            matchGameService.offline(getUserId(session));
        }else if ("FIGHT".equals(userGroup.getGroupName())) {
            gameFightService.offline(getUserId(session));
        }
        log.info("用户 group={}, userId={}, Connection closed. Status: {}", userGroup.getGroupName(), userGroup.getUserName(),status);
        log.info("在线用户：{}", sessionService.getOnlineCount());
    }

	/**
	 * 传输错误时调用
	 *
	 * @param session
	 * @param exception
	 * @throws Exception
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        UserGroup userGroup = getUserIdAndGroupName(session);
		if (session.isOpen()) {
			session.close();
		}
		log.debug("用户: " + userGroup.getUserName() + " websocket connection closed......");
        sessionService.removeUser(userGroup.getUserName());
        sessionService.removeGroupUser(userGroup.getGroupName(), userGroup.getUserName());
	}

    public class UserGroup{
        private String userName;
        private String groupName;

        public UserGroup(String groupName, String userName){
            this.groupName = groupName;
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }
        public String getGroupName() {
            return groupName;
        }
    }

}

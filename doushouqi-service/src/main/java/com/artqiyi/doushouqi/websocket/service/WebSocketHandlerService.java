package com.artqiyi.doushouqi.websocket.service;


import com.artqiyi.doushouqi.common.socket.SocketRequestMsg;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * webSocket业务服务类
 */
@Component
public class WebSocketHandlerService extends TextWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandlerService.class);

    private WebSocketSessionService sessionService = WebSocketSessionService.INSTANCE;

    /**
     * 拦截客户端每次请求进行业务处理
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        // 获取提交过来的消息详情
        logger.info("收到用户 " + username + "的消息:" + message.getPayload());

        //业务处理
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            SocketRequestMsg requestMsg = new SocketRequestMsg(payload);
            String code = requestMsg.getCode();
            Map paramMap = requestMsg.getParamMap();
            switch (code) {
                default:
                    break;
            }
        }
    }

    /**
     * 当新连接建立的时候, 连接成功时候,会触发页面上onOpen方法(第一次调用的时候才调用这个方法)
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = String.valueOf(session.getAttributes().get("WEBSOCKET_USERNAME"));
        String groupName = String.valueOf(session.getAttributes().get("WEBSOCKET_GROUP"));
        logger.info("session.getId()={}",session.getId());
        //如果建立过连接,关闭并删除旧的
        WebSocketSession oldSession = sessionService.getUser(username);
        if (oldSession != null && oldSession.isOpen()){
            oldSession.close();
        }
        //用户会话session信息保存
        sessionService.addUser(username, session);
        sessionService.addGroupUser(groupName, username, session);
        logger.info("用户 " + username + " Connection Established");
        logger.info("在线人数：{}", sessionService.getOnlineCount());
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
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        String groupName = (String) session.getAttributes().get("WEBSOCKET_GROUP");
        logger.info("socket用户:" + username + " Connection closed. Status: " + status);
        sessionService.removeUser(username);
        sessionService.removeGroupUser(groupName, username);
        logger.info("在线人数：{}", sessionService.getOnlineCount());
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
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        if (session.isOpen()) {
            session.close();
        }
        logger.debug("socket用户: " + username + "连接关闭成功......");
    }


}

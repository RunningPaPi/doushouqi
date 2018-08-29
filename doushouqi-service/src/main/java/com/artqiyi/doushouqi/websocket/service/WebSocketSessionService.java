package com.artqiyi.doushouqi.websocket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * webSocket用户会话session信息服务(线程安全单例模式)
 */
public enum  WebSocketSessionService {
    INSTANCE;

    private static Logger log = LoggerFactory.getLogger(WebSocketSessionService.class);

    private static AtomicBoolean started = new AtomicBoolean();

    // 已建立连接的用户
    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    //分组用户
    private Map<String, Map<String, WebSocketSession>> groupUsers = new ConcurrentHashMap<>();

    public boolean getAndSetStarted(boolean flag){
        return started.getAndSet(flag);
    }

    public void addUser(String userName, WebSocketSession session){
        users.put(userName, session);
    }

    public void addGroupUser(String groupName, String userName, WebSocketSession session) {
        if (groupUsers.get(groupName) == null){
            Map<String, WebSocketSession> group = new ConcurrentHashMap<>();
            group.put(userName, session);
            groupUsers.put(groupName, group);
            return ;
        }
        groupUsers.get(groupName).put(userName, session);
    }

    public WebSocketSession getGroupUser(String groupName, String userName){
        if (groupUsers.get(groupName) == null){
            return null;
        }
        return groupUsers.get(groupName).get(userName);
    }

    public void removeGroupUser(String groupName, String userName){
        if (groupUsers.get(groupName) != null) {
            groupUsers.get(groupName).remove(userName);
            if (groupUsers.get(groupName).size() == 0) {
                groupUsers.remove(groupName);
            }
        }
    }

    public Map<String, WebSocketSession> getGroupUsers(String groupName){
        return groupUsers.get(groupName);
    }

    public Map<String, WebSocketSession> getAllUsers(){
        return users;
    }

    public WebSocketSession getUser(String userName){
        return users.get(userName);
    }

    public WebSocketSession removeUser(String userName){
        return users.remove(userName);
    }

    public Integer getOnlineCount() {
        return users.size();
    }

    /**
     * 给某个分组用户发信息
     *
     * @param message
     */
    public void sendMessageToGroupUsers(String groupName, TextMessage message) {
        Map<String, WebSocketSession> userMap = getGroupUsers(groupName);
        if (userMap == null) {
            return;
        }
        for (Map.Entry<String, WebSocketSession> entry : userMap.entrySet()) {
            WebSocketSession userSession = entry.getValue();
            try {
                if (null != userSession && userSession.isOpen()) {
                    userSession.sendMessage(message);
                    log.info("分组: " + groupName + " 信息发送，用户：" + entry.getKey() + "连接正常，消息发送成功：" + message.getPayload());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        Map<String, WebSocketSession> userMap = getAllUsers();
        if (userMap == null) {
            return;
        }
        for (Map.Entry<String, WebSocketSession> entry : userMap.entrySet()) {
            WebSocketSession userSession = entry.getValue();
            try {
                if (null != userSession && userSession.isOpen()) {
                    userSession.sendMessage(message);
                    log.info("全部在线用户信息发送，用户：" + entry.getKey() + "连接正常，消息发送成功：" + message.getPayload());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        WebSocketSession userSession = getUser(userName);
        try {
            if (null != userSession && userSession.isOpen()) {
                userSession.sendMessage(message);
                log.info("socket用户: " + userName + " 连接正常，消息发送成功：" + message.getPayload());
            } else {
                log.info("socket用户: " + userName + " 连接已断开，消息发送失败：" + message.getPayload());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给某个分组某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String groupName, String userName, TextMessage message) {
        WebSocketSession userSession = getGroupUser(groupName, userName);
        try {
            if (null != userSession && userSession.isOpen()) {
                userSession.sendMessage(message);
                log.info("分组:" + groupName + " socket用户: " + userName + " 连接正常，消息发送成功：" + message.getPayload());
            } else {
                log.info("分组:" + groupName + "socket用户: " + userName + " 连接已断开，消息发送失败：" + message.getPayload());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开某个用户链接
     *
     * @param userName
     * @throws Exception
     */
    public void close(String userName) throws Exception {
        WebSocketSession userSession = getUser(userName);
        if (userSession == null) {
            return;
        }
        if (userSession.isOpen()) {
            userSession.close();
        }
    }

}


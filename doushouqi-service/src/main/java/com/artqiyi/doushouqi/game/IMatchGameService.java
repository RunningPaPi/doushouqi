package com.artqiyi.doushouqi.game;

import org.springframework.web.socket.TextMessage;

/**
 * 闯关游戏service接口
 */
public interface IMatchGameService {
    /**
     * 开始游戏
     * @param userId 用户ID
     */
    void startGame(Long userId);

    void transData(Long userId, TextMessage message);

    void emo(Long userId, TextMessage message);

    void giveUp(long userId);

    void gameOver(Long userId);

    void gameDataSave();

    void offline(Long userId);
}


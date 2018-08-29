package com.artqiyi.doushouqi.game;

import org.springframework.web.socket.TextMessage;

/**
 * 好友对战模式
 */
public interface IGameFightService {
    /**
     * 用户创建房间
     *
     * @param userId 用户ID
     */
    String newRoom(Long userId);

    /**
     * 进入房间匹配对战
     *
     * @param userId 用户id
     * @param roomNo 房间编码
     */
    void enterRoom(Long userId, String roomNo);

    /**
     * 开始游戏
     *
     * @param userId
     */
    void start(Long userId);

    /**
     * 数据传输
     * @param userId
     * @param data
     */
    void dataTrans(Long userId, TextMessage data);

    /**
     * 逃跑
     *
     * @param userId 用户id
     */
    void giveUp(Long userId);

    /**
     * 表情
     *
     * @param userId
     */
    void emo(Long userId, TextMessage data);

    /**
     * 再来一局
     *
     * @param userId
     */
    void again(Long userId);

    /**
     * 离开房间
     *
     * @param userId
     */
    void leave(Long userId);
    /**
     * 掉线
     * @param userId
     */
    void offline(Long userId);

    /**
     * 保存数据
     * @param o
     */
    void saveData(Object o);

    /**
     * 游戏结束
     * @param userId
     */
    void gameOver(Long userId);
}

package com.artqiyi.doushouqi.game;

/**
 * 游戏相关任务
 */
public interface IGameJobService {
    void dealGameData();

    void removeGameJob(String groupName, String JobName);
}

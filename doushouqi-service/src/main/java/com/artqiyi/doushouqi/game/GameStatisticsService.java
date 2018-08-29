package com.artqiyi.doushouqi.game;

import com.artqiyi.doushouqi.game.vo.GameUserRecord;

public interface GameStatisticsService {

    void statistics(Long userId, String type);

    GameUserRecord getUserRecord(Long userId);
}

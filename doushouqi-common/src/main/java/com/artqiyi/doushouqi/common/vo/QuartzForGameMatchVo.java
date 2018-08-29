package com.artqiyi.doushouqi.common.vo;

/**
 *  quartz传参值对象——匹配游戏
 */
public class QuartzForGameMatchVo {
    private String gameNo;

    private Short nextRound;

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public Short getNextRound() {
        return nextRound;
    }

    public void setNextRound(Short nextRound) {
        this.nextRound = nextRound;
    }

}

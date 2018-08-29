package com.artqiyi.doushouqi.game.vo;

import java.util.Date;

public class GameRecord {
    private String gameType;

    private Integer contestNum;

    private Integer playTimes;

    private Date createTime;

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Integer getContestNum() {
        return contestNum;
    }

    public void setContestNum(Integer contestNum) {
        this.contestNum = contestNum;
    }

    public Integer getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(Integer playTimes) {
        this.playTimes = playTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

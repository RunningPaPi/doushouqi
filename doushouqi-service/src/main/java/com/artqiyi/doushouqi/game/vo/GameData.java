package com.artqiyi.doushouqi.game.vo;

import java.util.List;

/**
 * 用户游戏记录
 */
public class GameData {
    private int gender;//性别

    private int againstGender;

    private Long userId;

    private Long againstId;

    private String headUrl;

    private String againstHeadUrl;

    private String nickName;

    private String againstNickName;

    private boolean isTurn;//是否轮到当前

    private List<PosData> gameData;//

    public String getAgainstNickName() {
        return againstNickName;
    }

    public void setAgainstNickName(String againstNickName) {
        this.againstNickName = againstNickName;
    }

    public int getAgainstGender() {
        return againstGender;
    }

    public void setAgainstGender(int againstGender) {
        this.againstGender = againstGender;
    }

    public Long getAgainstId() {
        return againstId;
    }

    public void setAgainstId(Long againstId) {
        this.againstId = againstId;
    }

    public String getAgainstHeadUrl() {
        return againstHeadUrl;
    }

    public void setAgainstHeadUrl(String againstHeadUrl) {
        this.againstHeadUrl = againstHeadUrl;
    }



    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    public List<PosData> getGameData() {
        return gameData;
    }

    public void setGameData(List<PosData> gameData) {
        this.gameData = gameData;
    }
}

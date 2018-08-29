package com.artqiyi.doushouqi.game.vo;


public class FightRoom {
    private Long masterId;//房主id

    private GameFightData masterInfo;

    private GameFightData matchInfo;

    private boolean gameStarted;

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public GameFightData getMasterInfo() {
        return masterInfo;
    }

    public void setMasterInfo(GameFightData masterInfo) {
        this.masterInfo = masterInfo;
    }

    public GameFightData getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(GameFightData matchInfo) {
        this.matchInfo = matchInfo;
    }

    public Long getMathId(){
        if (null!=matchInfo){
            return matchInfo.getUserId();
        }
        return null;
    }
}

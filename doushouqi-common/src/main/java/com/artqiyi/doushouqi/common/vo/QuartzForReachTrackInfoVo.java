package com.artqiyi.doushouqi.common.vo;

public class QuartzForReachTrackInfoVo {
    private String username;
    private String message;
    private Long robotPlayerId;
    private Long realPlayerId;
    private Long gameFieldId;
    private String gameNo;

    public String getGameNo() {
        return gameNo;
    }

    public Long getRobotPlayerId() {
        return robotPlayerId;
    }

    public void setRobotPlayerId(Long robotPlayerId) {
        this.robotPlayerId = robotPlayerId;
    }

    public Long getRealPlayerId() {
        return realPlayerId;
    }

    public void setRealPlayerId(Long realPlayerId) {
        this.realPlayerId = realPlayerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public void setGameFieldId(Long gameFieldId) {
        this.gameFieldId = gameFieldId;
    }

    public Long getGameFieldId() {
        return gameFieldId;
    }
}

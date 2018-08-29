package com.artqiyi.doushouqi.game.vo;

/**
 * 斗兽棋坐标数据结构
 */
public class PosData{
    private Long userId;
    private int type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", type=" + type +
                '}';
    }
}
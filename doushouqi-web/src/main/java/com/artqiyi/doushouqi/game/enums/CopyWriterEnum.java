package com.artqiyi.doushouqi.game.enums;

public enum CopyWriterEnum {
    COME_ON_FIGHT(1, "房间开好了，来盘斗兽棋吧！", "https://imgqdw.artqiyi.com/doushouqishare1.png"),
    COME_ON_FIGHT_WITH_RED_PACK(2, "来战！输的发红包！", "https://imgqdw.artqiyi.com/doushouqishare2.png");

    private int code;

    private String text;

    private String url;

    CopyWriterEnum(int code, String text, String url) {
        this.code = code;
        this.text = text;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}

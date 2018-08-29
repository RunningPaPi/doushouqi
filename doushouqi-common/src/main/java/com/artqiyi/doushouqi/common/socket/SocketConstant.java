package com.artqiyi.doushouqi.common.socket;

/**
 * sockect常量
 */
public class SocketConstant {

    //匹配赛socket相关操作
    public static final String DSQ_MATCH_START = "dsq_match_start"; //游戏开始
    public static final String DSQ_MATCH_DATA_TRANS = "dsq_match_data_trans"; //数据传输
    public static final String DSQ_MATCH_DATA_TRANS_FAIL = "dsq_match_data_trans_fail"; //数据传输
    public static final String DSQ_MATCH_EMO = "dsq_match_emo"; //表情
    public static final String DSQ_MATCH_GIVE_UP = "dsq_match_give_up"; //放弃比赛
    public static final String DSQ_MATCH_OFFLINE = "dsq_match_offline"; //掉线
    public static final String DSQ_MATCH_END = "dsq_match_end"; //比赛结束


    //好友对战比赛socket相关操作
    public static final String DSQ_FIGHT_CREATE_ROOM = "dsq_fight_create_room"; //加入房间
    public static final String DSQ_FIGHT_ENTER_ROOM = "dsq_fight_enter_room"; //加入房间
    public static final String DSQ_FIGHT_ENTER_ROOM_FAIL = "dsq_fight_enter_room_fail"; //加入房间失败
    public static final String DSQ_FIGHT_START = "dsq_fight_start"; //比赛开始
    public static final String DSQ_FIGHT_EMO = "dsq_fight_emo"; //表情
    public static final String DSQ_FIGHT_GIVE_UP = "dsq_fight_give_up"; //放弃比赛
    public static final String DSQ_FIGHT_DATA_TRANS = "dsq_fight_data_trans"; //比赛数据
    public static final String DSQ_FIGHT_DATA_TRANS_FAIL = "dsq_fight_data_trans_fail"; //比赛数据传输失败
    public static final String DSQ_FIGHT_AGAIN = "dsq_fight_again"; //再来一局
    public static final String DSQ_FIGHT_AGAIN_FAIL = "dsq_fight_again_fail"; //再来一局失败
    public static final String DSQ_FIGHT_LEAVE = "dsq_fight_leave"; //离开
    public static final String DSQ_FIGHT_END = "dsq_fight_end"; //比赛结束
    public static final String DSQ_FIGHT_OFFLINE = "dsq_fight_offline"; //掉线

}

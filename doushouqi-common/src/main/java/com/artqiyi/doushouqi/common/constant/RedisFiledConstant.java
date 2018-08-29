package com.artqiyi.doushouqi.common.constant;
/**
 * redis缓存命名空间常量
 */
public class RedisFiledConstant {

    public static String FILED_USER = "dsq_user_info";      //用户信息
    public static String USER = "dsq_user";
    public static final String USER_OPEN_ID = "dsq_user_open_id";//用户openid

    public static String DSQ_MATCH_USER_WAIT = "dsq_match_user_wait";//用户消息推送

    public static String DSQ_MATCH_AGAINST = "dsq_match_against";//闯关比赛当前赛场记录

    public static String DSQ_GAME_MATCH_RECORD = "dsq_game_match_record";//对战数据
    public static String DSQ_GAME_FIGHT_RECORD = "dsq_game_fight_record";//对战数据
    public static String DSQ_GAME_MATCH_USER_RECORD = "dsq_game_match_user_record";//用户数据
    public static String DSQ_GAME_FIGHT_USER_RECORD = "dsq_game_fight_user_record";//用户数据

    //斗兽棋对战模式
    public static String DSQ_FIGHT_ROOM = "dsq_fight_room";//房间
    public static String DSQ_FIGHT_USER_RECORD = "dsq_fight_user_record";//比赛数据
    public static String DSQ_FIGHT_USER_ROOM = "dsq_user_room";//用户对应的房间号
    public static String DSQ_ENTER_ROOM = "dsq_fight_enter_room:";//房间

    /**
     * 登录相关
     */
    public static final String USER_SESSION_KEY = "dsq_user_session_key:";//session_key

}

package com.artqiyi.doushouqi.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service
public class WechatService {
    private static Logger log = LoggerFactory.getLogger(WechatService.class);

    /**
     * 闯关通关获奖微信模板消息推送
     */
    public void sendModelMsg() {
        log.info("【微信模板消息推送】: 闯关通关获奖微信模板消息推送");

    }

//    public String getAccessToken() {
//        String s = UrlUtil.get(wechatConfig.getAccessTokenUrl().replace("APPID", wechatConfig.getAppid()).replace("APPSECRET", wechatConfig.getAppsecret()));
//        Map getResult = (Map) JSON.parse(s);
//        return (String) getResult.get("access_token");
//    }

}

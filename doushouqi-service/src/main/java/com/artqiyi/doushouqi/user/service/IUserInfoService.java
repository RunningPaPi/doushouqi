package com.artqiyi.doushouqi.user.service;

import com.artqiyi.doushouqi.base.service.IBaseService;
import com.artqiyi.doushouqi.user.domain.UserInfo;
import com.artqiyi.doushouqi.user.domain.UserInfoExample;

public interface IUserInfoService extends IBaseService<UserInfo,UserInfoExample>{
    UserInfo selectByUserId(long userId);
    //邀请码是否已存在
    boolean isInviteCodeExist(String inviteCode);

    /**
     * 绑定支付宝
     * @param userId
     * @param alipayAccount
     * @param alipayRealname
     */
    void bindAlipayAccount(Long userId, String alipayAccount, String alipayRealname);

}

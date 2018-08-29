package com.artqiyi.doushouqi.user.service;

import com.artqiyi.doushouqi.base.service.IBaseService;
import com.artqiyi.doushouqi.user.domain.User;
import com.artqiyi.doushouqi.user.domain.UserExample;

public interface IUserService extends IBaseService<User,UserExample>{
    //是否登录
    boolean hasLogin(String token);
    //根据手机获取用户
    User getByPhone(String phone);

    User selectByUserId(Long userId);
}

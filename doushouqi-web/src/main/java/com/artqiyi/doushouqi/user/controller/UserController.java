package com.artqiyi.doushouqi.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.artqiyi.doushouqi.aspect.AuthPassport;
import com.artqiyi.doushouqi.common.constant.MsgConstant;
import com.artqiyi.doushouqi.common.constant.RedisFiledConstant;
import com.artqiyi.doushouqi.common.constant.ResponseCodeConstant;
import com.artqiyi.doushouqi.common.constant.SystemConstant;
import com.artqiyi.doushouqi.common.util.RandomUtil;
import com.artqiyi.doushouqi.redis.RedisClient;
import com.artqiyi.doushouqi.response.UserResponse;
import com.artqiyi.doushouqi.user.domain.User;
import com.artqiyi.doushouqi.user.domain.UserExample;
import com.artqiyi.doushouqi.user.domain.UserInfo;
import com.artqiyi.doushouqi.user.form.UserForm;
import com.artqiyi.doushouqi.user.service.IUserInfoService;
import com.artqiyi.doushouqi.user.service.IUserService;
import com.artqiyi.doushouqi.user.service.WXAppletUserInfoService;
import com.artqiyi.doushouqi.user.service.vo.UserVo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 平台用户接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private WXAppletUserInfoService wxAppletUserInfoService;
    @Autowired
    private RedisClient redisClient;

    /**
     * 微信小程序获取用户信息
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
//    @AuthPassport(checkSign = true, checkLogin = false)
    @GetMapping("/wxapplet/login")
    public UserResponse wxAppletUserInfo(@RequestParam("encryptedData") String encryptedData,
                                         @RequestParam("sessionKey") String sessionKey,
                                         @RequestParam("iv") String iv) {
        UserResponse rsp = new UserResponse();
//        log.info("encryptedData={}",encryptedData);
//        log.info("sessionKey={}",sessionKey);
//        log.info("iv={}",iv);
        try {
            String realSessionKey = redisClient.get(RedisFiledConstant.USER_SESSION_KEY + sessionKey, String.class);
            redisClient.del(RedisFiledConstant.USER_SESSION_KEY + sessionKey);
            rsp.setResult(wxAppletUserInfoService.getUserInfo(encryptedData, realSessionKey, iv));
            JSONObject wxuserInfo = wxAppletUserInfoService.getUserInfo(encryptedData, realSessionKey, iv);
            if (wxuserInfo != null) {
                log.info("wxuserInfo={}", wxuserInfo);
                String openId = wxuserInfo.getString("openId");
                String nickName = wxuserInfo.getString("nickName");
                int gender = wxuserInfo.getIntValue("gender");
                String city = wxuserInfo.getString("city");
                String province = wxuserInfo.getString("province");
                String country = wxuserInfo.getString("country");
                String avatarUrl = wxuserInfo.getString("avatarUrl");
                String unionId = wxuserInfo.getString("unionId");
                UserExample example = new UserExample();
                UserVo userVo = new UserVo();
                example.or().andOpenidEqualTo(openId);
                List<User> users = userService.selectByExample(example);
                String token = UUID.randomUUID().toString();
                if (null != users && users.size() > 0) { //非初次微信授权登录
                    User user = users.get(0);
                    String oldToken = user.getToken();
                    user.setToken(token);
                    user.setOpenid(openId);
                    userService.saveOrUpdate(user); //更新用户token
                    UserInfo userInfo = userInfoService.selectByUserId(user.getUserId());
                    if (null != userInfo) {
                        userInfo.setLastLoginTime(new Date());
                        userInfoService.saveOrUpdate(userInfo); //更新用户最后登录时间
                    }
                    user.setPassword("");
                    BeanUtils.copyProperties(user, userVo);
                    BeanUtils.copyProperties(userInfo, userVo);
                    redisClient.hDel(RedisFiledConstant.FILED_USER, oldToken); //移除上一次用户缓存信息
                    redisClient.hSet(RedisFiledConstant.FILED_USER, token, userVo);//保存用户信息至redis
                    redisClient.hSet(RedisFiledConstant.USER, user.getUserId().toString(), userVo);//保存用户信息至redis
                } else { //初次授权登录相当于新用户注册
                    User user = new User();
                    user.setToken(token);
                    user.setHeadPicUrl(avatarUrl);
                    user.setGender((short) gender);
                    user.setNickName(nickName);
                    user.setOpenid(openId);
                    user.setUnionId(unionId);
                    user.setCityCode(city);
                    user.setAreaCode(country);
                    user.setProvinceCode(province);
                    user.setStatus(SystemConstant.VALID);
                    user.setRegisterType(SystemConstant.REGISTE_WEIXIN);
                    user.setIsRobot(false);
                    Long id = userService.saveOrUpdate(user); //保存用户信息
                    int len = 6;//默认6位
                    //如果大于6位的极限自动转8位
                    if (id >= 900000L){
                        len = 8;
                    }
                    Long userId = RandomUtil.genUserId(id, len);
                    user.setUserId(userId);
                    //保存用户id
                    userService.saveOrUpdate(user);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(userId);
                    userInfo.setLastLoginTime(new Date());
                    userInfo.setUpdateTime(new Date());
                    userInfo.setLevel("0");
                    userInfo.setCoin(0);
                    //邀请码
                    userInfo.setInviteCode(RandomUtil.genInviteCode(id, 6));
                    userInfo.setPoint(0);
                    userInfo.setBalance(0L);//首次登录，赠送红包
                    userInfo.setBalanceWithdrawable(0L);
                    userInfo.setBalanceFreezed(0L);
                    userInfo.setAlipayAccountValidated(false);
                    userInfo.setPhoneValidated(false);
                    long userInfoId = userInfoService.saveOrUpdate(userInfo); //保存用户基本信息
                    userInfo.setId(userInfoId);

                    user.setPassword("");
                    BeanUtils.copyProperties(user, userVo);
                    BeanUtils.copyProperties(userInfo, userVo, "userId");
                    redisClient.hSet(RedisFiledConstant.FILED_USER, token, userVo);//保存用户信息至redis
                    redisClient.hSet(RedisFiledConstant.USER, user.getUserId().toString(), userVo);//保存用户信息至redis
                }

                rsp.setResult(userVo);
                rsp.setCode(ResponseCodeConstant.SUCCESS);
                rsp.setMsg(MsgConstant.SUCCESS_LOGIN);
                return rsp;
            }

        } catch (Exception e) {
            rsp.setCode(ResponseCodeConstant.SERVICE_FAIL);
            rsp.setMsg("注册/登录失败");
            log.error("【注册/登录】 注册登录异常Exception={}", e);
        }
        return rsp;
    }


    /**
     * 微信小程序登录
     *
     * @param code
     * @return
     */
//    @AuthPassport(checkSign = true, checkLogin = false)
    @GetMapping("/wxapplet/auth")
    public UserResponse wxAppletLogin(@RequestParam("code") String code) {
        UserResponse rsp = new UserResponse();
        try {
            String fakeSessionKey = UUID.randomUUID().toString();
            JSONObject jsonObject = wxAppletUserInfoService.getSessionKeyOrOpenid(code);
            log.info("jsonObject={}", jsonObject);
            String sessionKey = MapUtils.getString(jsonObject, "session_key");
            if (sessionKey != null){
                redisClient.setWithExpire(RedisFiledConstant.USER_SESSION_KEY + fakeSessionKey, sessionKey, 5 * 60);
                jsonObject.put("session_key", fakeSessionKey);
                rsp.setResult(jsonObject);
                rsp.setCode(ResponseCodeConstant.SUCCESS);
            }else {
                rsp.setMsg(jsonObject.toJSONString());
                rsp.setCode(ResponseCodeConstant.SERVICE_FAIL);
            }
        } catch (Exception e) {
            log.info("【微信小程序】 登录出错Exception={}", e.getMessage());
            rsp.setCode(ResponseCodeConstant.SERVICE_FAIL);
            rsp.setResult(null);
            return rsp;
        }
        return rsp;
    }

    /**
     * 微信快捷登录
     *
     * @param userForm
     * @return
     */
    @AuthPassport(checkSign = true, checkLogin = false)
    @PostMapping("/loginByWechat")
    public UserResponse loginByWechat(@RequestBody UserForm userForm) {
        UserResponse rsp = new UserResponse();

        try {
            Map<String, Object> data = new HashMap<String, Object>();//返回数据
            UserExample example = new UserExample();

            example.or().andUnionIdEqualTo(userForm.getUnionId());
            List<User> users = userService.selectByExample(example);
            String token = UUID.randomUUID().toString();
            if (null != users && users.size() > 0) { //非初次微信授权登录
                User user = users.get(0);
                String oldToken = user.getToken();
                user.setToken(token);
                user.setOpenid(userForm.getOpenId());
                userService.saveOrUpdate(user); //更新用户token
                UserInfo userInfo = userInfoService.selectByUserId(user.getId());
                if (null != userInfo) {
                    userInfo.setLastLoginTime(new Date());
                    userInfoService.saveOrUpdate(userInfo); //更新用户最后登录时间
                }
                user.setPassword("");
                data.put("user", user);
                data.put("userInfo", userInfo);
                //返回是否首次注册登录
                data.put("isFirstTimeLogin", false);
                redisClient.hDel(RedisFiledConstant.FILED_USER, oldToken); //移除上一次用户缓存信息
                redisClient.hSet(RedisFiledConstant.FILED_USER, token, data);//保存用户信息至redis
            } else { //初次授权登录相当于新用户注册
                User user = new User();
                user.setToken(token);
                user.setHeadPicUrl(userForm.getAvatarUrl());
                user.setGender((short) userForm.getGender().intValue());
                user.setNickName(userForm.getNickName());
                user.setOpenid(userForm.getOpenId());
                user.setUnionId(userForm.getUnionId());
                user.setCityCode(userForm.getCity());
                user.setAreaCode(userForm.getCountry());
                user.setProvinceCode(userForm.getProvince());
                user.setStatus(SystemConstant.VALID);
                user.setRegisterType(SystemConstant.REGISTE_WEIXIN);
                user.setIsRobot(false);
                Long id = userService.saveOrUpdate(user); //保存用户信息
                int len = 6;//默认6位
                //如果大于6位的极限自动转8位
                if (id >= 900000L){
                    len = 8;
                }
                Long userId = RandomUtil.genUserId(id, len);
                user.setUserId(userId);
                //保存用户id
                userService.saveOrUpdate(user);
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(userId);
                userInfo.setLastLoginTime(new Date());
                userInfo.setUpdateTime(new Date());
                userInfo.setLevel("0");
                userInfo.setCoin(0);
                //邀请码
                userInfo.setInviteCode(RandomUtil.genInviteCode(id, 6));
                userInfo.setPoint(0);
                userInfo.setBalance(0L);//首次登录，赠送红包
                userInfo.setBalanceWithdrawable(0L);
                userInfo.setBalanceFreezed(0L);
                userInfo.setAlipayAccountValidated(false);
                userInfo.setPhoneValidated(false);
                long userInfoId = userInfoService.saveOrUpdate(userInfo); //保存用户基本信息
                userInfo.setId(userInfoId);

                user.setPassword("");
                data.put("user", user);
                data.put("userInfo", userInfo);
                //返回是否首次注册登录
                data.put("isFirstTimeLogin", true);
                redisClient.hSet(RedisFiledConstant.FILED_USER, token, data);//保存用户信息至redis
            }

            rsp.setResult(data);
            rsp.setCode(ResponseCodeConstant.SUCCESS);
            rsp.setMsg(MsgConstant.SUCCESS_LOGIN);
            return rsp;
        } catch (Exception e) {
            rsp.setCode(ResponseCodeConstant.SERVICE_FAIL);
            rsp.setMsg("注册/登录失败");
            log.error("【注册/登录】 注册登录异常Exception={}", e.getMessage());
            return rsp;
        }
    }

}

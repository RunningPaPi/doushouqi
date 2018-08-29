package com.artqiyi.doushouqi.user.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class UserVo {
    private Long userId;

    private String nickName;

    private Short gender;

    private String headPicUrl;
    @JsonIgnore
    private String openid;
    @JsonIgnore
    private String unionId;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String phone;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private String city;
    @JsonIgnore
    private String province;
    @JsonIgnore
    private String area;
    @JsonIgnore
    private String tokenId;
    @JsonIgnore
    private Short registerType;
    @JsonIgnore
    private Short status;

    @JsonIgnore
    private Boolean isRobot;
    @JsonIgnore
    private String birthday;
    @JsonIgnore
    private String inviteCode;
    @JsonIgnore
    private String level;
    @JsonIgnore
    private Integer diamond;
    @JsonIgnore
    private Integer coin;
    @JsonIgnore
    private Integer point;
    @JsonIgnore
    private Long balance;
    @JsonIgnore
    private Long balanceWithdrawable;
    @JsonIgnore
    private Long balanceFreezed;

    @JsonIgnore
    private String alipayAccount;
    @JsonIgnore
    private String alipayRealname;
    @JsonIgnore
    private Boolean phoneValidated;
    @JsonIgnore
    private Boolean realnameValidated;
    @JsonIgnore
    private Boolean alipayAccountValidated;
    @JsonIgnore
    private Date lastLoginTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Short getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Short registerType) {
        this.registerType = registerType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Boolean getRobot() {
        return isRobot;
    }

    public void setRobot(Boolean robot) {
        isRobot = robot;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getBalanceWithdrawable() {
        return balanceWithdrawable;
    }

    public void setBalanceWithdrawable(Long balanceWithdrawable) {
        this.balanceWithdrawable = balanceWithdrawable;
    }

    public Long getBalanceFreezed() {
        return balanceFreezed;
    }

    public void setBalanceFreezed(Long balanceFreezed) {
        this.balanceFreezed = balanceFreezed;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayRealname() {
        return alipayRealname;
    }

    public void setAlipayRealname(String alipayRealname) {
        this.alipayRealname = alipayRealname;
    }

    public Boolean getPhoneValidated() {
        return phoneValidated;
    }

    public void setPhoneValidated(Boolean phoneValidated) {
        this.phoneValidated = phoneValidated;
    }

    public Boolean getRealnameValidated() {
        return realnameValidated;
    }

    public void setRealnameValidated(Boolean realnameValidated) {
        this.realnameValidated = realnameValidated;
    }

    public Boolean getAlipayAccountValidated() {
        return alipayAccountValidated;
    }

    public void setAlipayAccountValidated(Boolean alipayAccountValidated) {
        this.alipayAccountValidated = alipayAccountValidated;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}

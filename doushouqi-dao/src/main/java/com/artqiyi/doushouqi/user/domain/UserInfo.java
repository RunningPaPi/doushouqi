package com.artqiyi.doushouqi.user.domain;

import java.util.Date;

public class UserInfo {
    private Long id;

    private Long userId;

    private String birthday;

    private String inviteCode;

    private String level;

    private Integer diamond;

    private Integer coin;

    private Integer point;

    private Long balance;

    private Long balanceWithdrawable;

    private Long balanceFreezed;

    private String alipayAccount;

    private String alipayRealname;

    private Boolean phoneValidated;

    private Boolean realnameValidated;

    private Boolean alipayAccountValidated;

    private Date lastLoginTime;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode == null ? null : inviteCode.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
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
        this.alipayAccount = alipayAccount == null ? null : alipayAccount.trim();
    }

    public String getAlipayRealname() {
        return alipayRealname;
    }

    public void setAlipayRealname(String alipayRealname) {
        this.alipayRealname = alipayRealname == null ? null : alipayRealname.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
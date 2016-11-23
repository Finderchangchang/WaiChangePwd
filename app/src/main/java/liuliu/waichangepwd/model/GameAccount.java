package liuliu.waichangepwd.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/16.
 */

public class GameAccount extends BmobObject {
    private String phone;//手机号
    private String password;//密码
    private String AccountNumber;//游戏账号
    private String OpenId;//绑定OPENID
    private String state;//状态
    private Integer VipGrade;//vip等级
    private Integer AmountCharge;//充值金额
    private Integer BatteryGrade;//炮台等级
    private Integer Bomb;//核弹
    private Integer Bronze;//青铜
    private Integer Diamondes;//钻石
    private Integer Frozen;//冰冻
    private Integer Gold;//黄金
    private Integer Horn;//号角
    private Integer Locking;//锁定
    private Integer MaterialScience;//材料
    private Integer Platinum;//白金
    private Integer Rage;//狂暴
    private String ReliefFund;//救济金
    private Integer Silver;//白银

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean isCheced = false;

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public Integer getVipGrade() {
        return VipGrade;
    }

    public void setVipGrade(Integer vipGrade) {
        VipGrade = vipGrade;
    }

    public Integer getAmountCharge() {
        return AmountCharge;
    }

    public void setAmountCharge(Integer amountCharge) {
        AmountCharge = amountCharge;
    }

    public Integer getBatteryGrade() {
        return BatteryGrade;
    }

    public void setBatteryGrade(Integer batteryGrade) {
        BatteryGrade = batteryGrade;
    }

    public Integer getBomb() {
        return Bomb;
    }

    public void setBomb(Integer bomb) {
        Bomb = bomb;
    }

    public Integer getBronze() {
        return Bronze;
    }

    public void setBronze(Integer bronze) {
        Bronze = bronze;
    }

    public Integer getDiamondes() {
        return Diamondes;
    }

    public void setDiamondes(Integer diamondes) {
        Diamondes = diamondes;
    }

    public Integer getFrozen() {
        return Frozen;
    }

    public void setFrozen(Integer frozen) {
        Frozen = frozen;
    }

    public Integer getGold() {
        return Gold;
    }

    public void setGold(Integer gold) {
        Gold = gold;
    }

    public Integer getHorn() {
        return Horn;
    }

    public void setHorn(Integer horn) {
        Horn = horn;
    }

    public Integer getLocking() {
        return Locking;
    }

    public void setLocking(Integer locking) {
        Locking = locking;
    }

    public Integer getMaterialScience() {
        return MaterialScience;
    }

    public void setMaterialScience(Integer materialScience) {
        MaterialScience = materialScience;
    }

    public Integer getPlatinum() {
        return Platinum;
    }

    public void setPlatinum(Integer platinum) {
        Platinum = platinum;
    }

    public Integer getRage() {
        return Rage;
    }

    public void setRage(Integer rage) {
        Rage = rage;
    }

    public String getReliefFund() {
        return ReliefFund;
    }

    public void setReliefFund(String reliefFund) {
        ReliefFund = reliefFund;
    }

    public Integer getSilver() {
        return Silver;
    }

    public void setSilver(Integer silver) {
        Silver = silver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public Boolean getCheced() {
        return isCheced;
    }

    public void setCheced(Boolean checed) {
        isCheced = checed;
    }
}

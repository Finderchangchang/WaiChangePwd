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
private int VipGrade;//vip等级
    private int AmountCharge;//充值金额
    private int BatteryGrade;//炮台等级
    private int Bomb;//核弹
    private int Bronze;//青铜
    private int Diamondes;//钻石
    private int Frozen;//冰冻
    private int Gold;//黄金
    private int Horn;//号角
    private int Locking;//锁定
    private int MaterialScience;//材料
    private int Platinum;//白金
    private int Rage;//狂暴
    private int ReliefFund;//救济金
    private int Silver;//白银

    public boolean isCheced = false;

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public int getVipGrade() {
        return VipGrade;
    }

    public void setVipGrade(int vipGrade) {
        VipGrade = vipGrade;
    }

    public int getAmountCharge() {
        return AmountCharge;
    }

    public void setAmountCharge(int amountCharge) {
        AmountCharge = amountCharge;
    }

    public int getBatteryGrade() {
        return BatteryGrade;
    }

    public void setBatteryGrade(int batteryGrade) {
        BatteryGrade = batteryGrade;
    }

    public int getBomb() {
        return Bomb;
    }

    public void setBomb(int bomb) {
        Bomb = bomb;
    }

    public int getBronze() {
        return Bronze;
    }

    public void setBronze(int bronze) {
        Bronze = bronze;
    }

    public int getDiamondes() {
        return Diamondes;
    }

    public void setDiamondes(int diamondes) {
        Diamondes = diamondes;
    }

    public int getFrozen() {
        return Frozen;
    }

    public void setFrozen(int frozen) {
        Frozen = frozen;
    }

    public int getGold() {
        return Gold;
    }

    public void setGold(int gold) {
        Gold = gold;
    }

    public int getHorn() {
        return Horn;
    }

    public void setHorn(int horn) {
        Horn = horn;
    }

    public int getLocking() {
        return Locking;
    }

    public void setLocking(int locking) {
        Locking = locking;
    }

    public int getMaterialScience() {
        return MaterialScience;
    }

    public void setMaterialScience(int materialScience) {
        MaterialScience = materialScience;
    }

    public int getPlatinum() {
        return Platinum;
    }

    public void setPlatinum(int platinum) {
        Platinum = platinum;
    }

    public int getRage() {
        return Rage;
    }

    public void setRage(int rage) {
        Rage = rage;
    }

    public int getReliefFund() {
        return ReliefFund;
    }

    public void setReliefFund(int reliefFund) {
        ReliefFund = reliefFund;
    }

    public int getSilver() {
        return Silver;
    }

    public void setSilver(int silver) {
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


}

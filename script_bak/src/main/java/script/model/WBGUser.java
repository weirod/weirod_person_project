package script.model;


import java.math.BigDecimal;

public class WBGUser {

    private long userid;

    /**
     *推荐人id
     */
    private long parentid;

    private String username;

    private String password;

    /**
     * wt资产
     */
    private BigDecimal wt;

    /**
     * 推荐人
     */
    private String referral;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getWt() {
        return wt;
    }

    public void setWt(BigDecimal wt) {
        this.wt = wt;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    @Override
    public String toString() {
        return "WBGUser{" +
                "userid=" + userid +
                ", parentid=" + parentid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", wt=" + wt +
                ", referral='" + referral + '\'' +
                '}';
    }
}

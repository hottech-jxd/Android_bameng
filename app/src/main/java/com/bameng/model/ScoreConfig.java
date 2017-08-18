package com.bameng.model;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ScoreConfig {
    /**
     * 创建订单奖励
     */
    private int CreateOrderScore;

    /// <summary>
    /// 盟友提交客户信息并审核通过，盟友可获得积分
    /// </summary>
    private int SubmitCustomerToAllyScore;

    /// <summary>
    /// 盟友提交客户信息并审核通过，盟主可获得积分
    /// </summary>
    private int SubmitCustomerToMainScore1;

    /// <summary>
    /// 盟主提交客户信息，盟主可获得积分
    /// </summary>
    public int SubmitCustomerToMainScore2;

    /// <summary>
    /// 盟主邀请盟友奖励积分
    /// </summary>
    private int InviteScore;

    public int getCreateOrderScore() {
        return CreateOrderScore;
    }

    public void setCreateOrderScore(int createOrderScore) {
        CreateOrderScore = createOrderScore;
    }

    public int getSubmitCustomerToAllyScore() {
        return SubmitCustomerToAllyScore;
    }

    public void setSubmitCustomerToAllyScore(int submitCustomerToAllyScore) {
        SubmitCustomerToAllyScore = submitCustomerToAllyScore;
    }

    public int getSubmitCustomerToMainScore1() {
        return SubmitCustomerToMainScore1;
    }

    public void setSubmitCustomerToMainScore1(int submitCustomerToMainScore1) {
        SubmitCustomerToMainScore1 = submitCustomerToMainScore1;
    }

    public int getSubmitCustomerToMainScore2() {
        return SubmitCustomerToMainScore2;
    }

    public void setSubmitCustomerToMainScore2(int submitCustomerToMainScore2) {
        SubmitCustomerToMainScore2 = submitCustomerToMainScore2;
    }

    public int getInviteScore() {
        return InviteScore;
    }

    public void setInviteScore(int inviteScore) {
        InviteScore = inviteScore;
    }
}

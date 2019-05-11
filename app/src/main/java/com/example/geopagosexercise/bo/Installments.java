package com.example.geopagosexercise.bo;

import com.orm.SugarRecord;

public class Installments extends SugarRecord {

    private int iCount;
    private double iRate;
    private double iAmount;
    private double iTotalAmount;
    private String iAmountMsg;
    private String iRateMsg;

    public Installments() {
    }

    public Installments(int iCount, double iRate, double iAmount,double iTotalAmount, String iAmountMsg, String iRateMsg) {
        this.iCount = iCount;
        this.iRate = iRate;
        this.iAmount = iAmount;
        this.iTotalAmount = iTotalAmount;
        this.iAmountMsg = iAmountMsg;
        this.iRateMsg = iRateMsg;
    }

    public void setiCount(int iCount) {
        this.iCount = iCount;
    }

    public int getiCount() {
        return iCount;
    }

    public void setiRate(double iRate) {
        this.iRate = iRate;
    }

    public double getiRate() {
        return iRate;
    }

    public void setiAmount(double iAmount) {
        this.iAmount = iAmount;
    }

    public double getiAmount() {
        return iAmount;
    }

    public void setiTotalAmount(double iTotalAmount) {
        this.iTotalAmount = iTotalAmount;
    }

    public double getiTotalAmount() {
        return iTotalAmount;
    }

    public void setiAmountMsg(String iAmountMsg) {
        this.iAmountMsg = iAmountMsg;
    }

    public String getiAmountMsg() {
        return iAmountMsg;
    }

    public void setiRateMsg(String iRateMsg) {
        this.iRateMsg = iRateMsg;
    }

    public String getiRateMsg() {
        return iRateMsg;
    }
}

package com.example.geopagosexercise.bo;

import com.orm.SugarRecord;

public class Payment extends SugarRecord{
    private String paymentMethodId;
    private String paymentMethodName;
    private String paymentType;
    private int cardIssuerId;
    private String cardIssuerName;
    private int installmentsCount;
    private double installmentsRate;
    private double installmentsAmount;
    private double originalAmount;
    private double finalAmount;
    private String installmentsAmountMsg;
    private String currencySymbol;
    private String currencyCountry;

    public Payment() {
    }

    public Payment(String paymentMethodId, String paymentMethodName, String paymentType, int cardIssuerId, String cardIssuerName, int installmentsCount, double installmentsRate,
                   double installmentsAmount, double originalAmount,double finalAmount, String installmentsAmountMsg, String currencySymbol, String currencyCountry) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.paymentType = paymentType;
        this.cardIssuerId = cardIssuerId;
        this.cardIssuerName = cardIssuerName;
        this.installmentsCount = installmentsCount;
        this.installmentsRate = installmentsRate;
        this.installmentsAmount = installmentsAmount;
        this.originalAmount = originalAmount;
        this.finalAmount = finalAmount;
        this.installmentsAmountMsg = installmentsAmountMsg;
        this.currencySymbol = currencySymbol;
        this.currencyCountry = currencyCountry;

    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getCardIssuerId() {
        return cardIssuerId;
    }

    public void setCardIssuerId(int cardIssuerId) {
        this.cardIssuerId = cardIssuerId;
    }

    public String getCardIssuerName() {
        return cardIssuerName;
    }

    public void setCardIssuerName(String cardIssuerName) {
        this.cardIssuerName = cardIssuerName;
    }

    public double getInstallmentsAmount() {
        return installmentsAmount;
    }

    public void setInstallmentsAmount(double installmentsAmount) {
        this.installmentsAmount = installmentsAmount;
    }

    public double getInstallmentsRate() {
        return installmentsRate;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public void setInstallmentsAmountMsg(String installmentsAmountMsg) {
        this.installmentsAmountMsg = installmentsAmountMsg;
    }

    public int getInstallmentsCount() {
        return installmentsCount;
    }

    public void setInstallmentsCount(int installmentsCount) {
        this.installmentsCount = installmentsCount;
    }

    public String getInstallmentsAmountMsg() {
        return installmentsAmountMsg;
    }

    public void setInstallmentsRate(double installmentsRate) {
        this.installmentsRate = installmentsRate;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCurrencyCountry() {
        return currencyCountry;
    }

    public void setCurrencyCountry(String currencyCountry) {
        this.currencyCountry = currencyCountry;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}

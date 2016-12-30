package com.bameng.model;

/**
 * Created by 47483 on 2016.11.07.
 */

public class BaseData {
    private int userStatus;
    private String aboutUrl;
    private String agreementUrl;
    private int enableSignIn;
    private String registerUrl;
    private String reportUrl;


    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getAboutUrl() {
        return aboutUrl;
    }

    public void setAboutUrl(String aboutUrl) {
        this.aboutUrl = aboutUrl;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public int getEnableSignIn() {
        return enableSignIn;
    }

    public void setEnableSignIn(int enableSignIn) {
        this.enableSignIn = enableSignIn;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}

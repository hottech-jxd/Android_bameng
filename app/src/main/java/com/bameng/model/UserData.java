package com.bameng.model;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 47483 on 2016.11.03.
 */
public class UserData  implements Serializable {


        String BelongOne;
    String BelongOneUserName;
        int UserId;
        int UserIdentity;
        String MerchantID;
        String ShopId;
        String IsActive;
        int Score;
        String ScoreLocked;
        int MengBeans;
        int MengBeansLocked;
        String CreateTime;
        String LoginName;
        String RealName;
        String NickName;
        String UserMobile;
        String UserHeadImg;
        String ShopName;
        String ShopProv;
        String ShopCity;
        String LevelName;
        String OrderSuccessAmount;
        String CustomerAmount;
        String token;
    String UserCity;


    /***
     * 待结算盟豆
     */
    int TempMengBeans;

    boolean selected=false;
    /**
     * 性别 男M 女F 未知
     */
    String UserGender;
    /***
     * 所属门店类型 1：总店，2：分店
     */
    int ShopType;


    /***
     * 我的二维码地址属性
     */
    private String myqrcodeUrl;

    public String getMyShareQrcodeUrl() {
        return myShareQrcodeUrl;
    }

    public void setMyShareQrcodeUrl(String myShareQrcodeUrl) {
        this.myShareQrcodeUrl = myShareQrcodeUrl;
    }

    public String getMyqrcodeUrl() {
        return myqrcodeUrl;
    }

    public void setMyqrcodeUrl(String myqrcodeUrl) {
        this.myqrcodeUrl = myqrcodeUrl;
    }

    /***
     * 分享二维码地址
     */
    private String myShareQrcodeUrl;

    public String getBelongOne() {
        return BelongOne;
    }

    public void setBelongOne(String belongOne) {
        BelongOne = belongOne;
    }

    public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int userId) {
            UserId = userId;
        }

        public int getUserIdentity() {
            return UserIdentity;
        }

        public void setUserIdentity(int userIdentity) {
            UserIdentity = userIdentity;
        }

        public String getMerchantID() {
            return MerchantID;
        }

        public void setMerchantID(String merchantID) {
            MerchantID = merchantID;
        }

        public String getShopId() {
            return ShopId;
        }

        public void setShopId(String shopId) {
            ShopId = shopId;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String isActive) {
            IsActive = isActive;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int score) {
            Score = score;
        }

        public String getScoreLocked() {
            return ScoreLocked;
        }

        public void setScoreLocked(String scoreLocked) {
            ScoreLocked = scoreLocked;
        }

        public int getMengBeans() {
            return MengBeans;
        }

        public void setMengBeans(int mengBeans) {
            MengBeans = mengBeans;
        }

        public int getMengBeansLocked() {
            return MengBeansLocked;
        }

        public void setMengBeansLocked(int mengBeansLocked) {
            MengBeansLocked = mengBeansLocked;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String loginName) {
            LoginName = loginName;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getUserMobile() {
            return UserMobile;
        }

        public void setUserMobile(String userMobile) {
            UserMobile = userMobile;
        }

        public String getUserHeadImg() {
            return UserHeadImg;
        }

        public void setUserHeadImg(String userHeadImg) {
            UserHeadImg = userHeadImg;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getShopProv() {
            return ShopProv;
        }

        public void setShopProv(String shopProv) {
            ShopProv = shopProv;
        }

        public String getShopCity() {
            return ShopCity;
        }

        public void setShopCity(String shopCity) {
            ShopCity = shopCity;
        }

        public String getLevelName() {
            return LevelName;
        }

        public void setLevelName(String levelName) {
            LevelName = levelName;
        }

        public String getOrderSuccessAmount() {
            return OrderSuccessAmount;
        }

        public void setOrderSuccessAmount(String orderSuccessAmount) {
            OrderSuccessAmount = orderSuccessAmount;
        }

        public String getCustomerAmount() {
            return CustomerAmount;
        }

        public void setCustomerAmount(String customerAmount) {
            CustomerAmount = customerAmount;
        }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String userCity) {
        UserCity = userCity;
    }

    public int getTempMengBeans() {
        return TempMengBeans;
    }

    public void setTempMengBeans(int tempMengBeans) {
        TempMengBeans = tempMengBeans;
    }


    public int getShopType() {
        return ShopType;
    }

    public void setShopType(int shopType) {
        ShopType = shopType;
    }

    public String getBelongOneUserName() {
        return BelongOneUserName;
    }

    public void setBelongOneUserName(String belongOneUserName) {
        BelongOneUserName = belongOneUserName;
    }
}

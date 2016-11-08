package com.bameng.model;

/**
 * 初始化http请求接受model
 */
public class InitOutputsModel extends BaseModel {

    private InitInnerModel data;

    public InitInnerModel getData() {
        return data;
    }

    public void setData(InitInnerModel data) {
        this.data = data;
    }

    public class InitInnerModel
    {
        private BaseData baseData;
        private VersionData versionData;
        private UserData userData;

        public VersionData getVersionData() {
            return versionData;
        }

        public void setVersionData(VersionData versionData) {
            this.versionData = versionData;
        }

        public BaseData getBaseData() {
            return baseData;
        }

        public void setBaseData(BaseData baseData) {
            this.baseData = baseData;
        }

        public UserData getUserData() {
            return userData;
        }

        public void setUserData(UserData userData) {
            this.userData = userData;
        }


    }
}

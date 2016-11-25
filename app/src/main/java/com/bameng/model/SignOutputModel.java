package com.bameng.model;

import java.security.PublicKey;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/11/22.
 */

public class SignOutputModel extends BaseModel{
    private SignModel data;

    public SignModel getData() {
        return data;
    }

    public void setData(SignModel data) {
        this.data = data;
    }

    public class SignModel{
        private int score;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}

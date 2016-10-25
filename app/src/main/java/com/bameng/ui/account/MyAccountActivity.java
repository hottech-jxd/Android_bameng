package com.bameng.ui.account;

import android.os.Bundle;
import android.os.Message;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;

public class MyAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}

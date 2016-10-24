package com.bameng.ui.account;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;

import butterknife.Bind;

public class MDouCountActivity extends BaseActivity {

    @Bind(R.id.mdou_count)
    TextView mdou_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdou_count);
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

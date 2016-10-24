package com.bameng.widgets;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.utils.SystemTools;


public class CountDownTimerButton extends CountDownTimer
{
    public interface CountDownFinishListener{
        void timeFinish();
        void timeProgress();
    }
    
    TextView view;
    String txt;
    String formatTxt;
    CountDownFinishListener finishListener=null;
    long progressTime = -1;
    long totalTime;
    
    public CountDownTimerButton(TextView view , String formatTxt , String txt ,
                                long millisInFuture , CountDownFinishListener listener , long progressTime) {
        super(millisInFuture, 1000 ); 
        this.view= view;
        this.formatTxt = formatTxt;
        this.txt = txt;
        this.view.setText(txt);
        this.view.setClickable(false);
        //this.view.setBackgroundResource(R.drawable.btn_mark_gray);
        this.view.setBackgroundColor(Color.parseColor("#D1D1D1"));
        finishListener = listener;
        this.totalTime = millisInFuture;
        this.progressTime = progressTime;
    }

    @Override
    public void onTick(long millisUntilFinished)
    {
        long t1 = (totalTime - millisUntilFinished) /1000;
        long t2 = progressTime / 1000;
        String content = String.format( formatTxt , millisUntilFinished / 1000 );
        view.setText( content );
        if( t1 >= t2 ){
            if( finishListener!=null ){
                finishListener.timeProgress();
            }
        }
    }

    @Override
    public void onFinish()
    {
        view.setClickable(true);
        view.setText(txt);
        //view.setBackgroundResource(R.drawable.btn_red_sel);
        if( finishListener!=null){
            finishListener.timeFinish();
        }
    }
    
    public void Stop(){
        this.cancel();
    }

}

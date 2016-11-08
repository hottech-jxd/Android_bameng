package com.bameng.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bameng.ui.base.BaseFragment;


/**
 * fragment界面切换管理类
 */
public class FragManager {



    public enum FragType{
        HOME, NEWS, BUSINESS, PROFILE,ALLYHOME,RICHES
    }
    private int viewId;
    private FragmentManager fragManager;
    private FragType preFragType;
    private FragType curFragType;
    private static FragManager fragIns;

    public static FragManager getIns(FragmentActivity context, int viewId){
        if(null != fragIns)
            fragIns = null;
        return fragIns = new FragManager(context, viewId);
    }
    private FragManager(FragmentActivity context, int viewId){
        this.viewId = viewId;
        this.fragManager = context.getSupportFragmentManager();

    }
    public FragType getCurrentFragType(){
        return this.curFragType;
    }
    public BaseFragment getCurrentFrag(){
        return getFragmentByType(preFragType);
    }

    public void setCurrentFrag(FragType type){
        if(type == preFragType)
            return;
        curFragType = type;
        FragmentTransaction ft = fragManager.beginTransaction();
        String fragTag = makeFragmentName(viewId, type);
        BaseFragment frag =  (BaseFragment) fragManager.findFragmentByTag(fragTag);
        if(frag == null){
            switch (type) {
                case HOME:
                    frag = new HomeFragment();
                    break;
                case NEWS:
                    frag = new NewsFragment();
                    break;
                case BUSINESS:
                    frag = new BusinessFragment();
                    break;
                case PROFILE:
                    frag = new UserFragment();
                    break;
                case ALLYHOME:
                    frag = new AllyHomeFrag();
                    break;
                case RICHES:
                    frag = new RichesFrag();
                    break;
//                case STORE:
//                    frag = new StoreFrag();
//                    break;
//                case ALLY:
//                    frag = new AllyFrag();
//                    break;
//                default:
//                    frag = new HomeFragment();
//                    break;
            }

            ft.add(viewId, frag, fragTag);
        }else{
            frag.onReshow();
        }


        ft.show(frag);
        if(preFragType != null){
            BaseFragment preFrag = getFragmentByType(preFragType);
//            preFrag.onPause();
            ft.hide(preFrag);
        }

        ft.commitAllowingStateLoss();
        preFragType = type;

    }
    public BaseFragment getFragmentByType(FragType type){
        return (BaseFragment) fragManager.findFragmentByTag(makeFragmentName(viewId, type));
    }

    private String makeFragmentName(int viewId, FragType type) {
        return "android:switcher:" + viewId + ":" + type;
    }


    public void setPreFragType(FragType type ){
        preFragType=type;
    }
}

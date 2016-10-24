package com.bameng.utils;


import android.util.Log;

import com.facebook.common.util.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil
{

    private static class Holder
    {
        private static final EncryptUtil instance = new EncryptUtil();
    }

    private EncryptUtil()
    {

    }

    public static final EncryptUtil getInstance()
    {
        return Holder.instance;
    }

//    public String encryptMd532(String source)
//    {
//        if (null == source || "".equals(source.trim()))
//        {
//            return null;
//        } else
//        {
//            return new String(Hex.encodeHex(DigestUtils.md5(source)));
//        }
//    }
    public String encryptMd532(String source)
    {
        if (null == source || "".equals(source.trim())){
            return null;
        } else{
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(source.getBytes("utf-8"));
                byte[] s1 = messageDigest.digest();
                String tem = new String( Hex.encodeHex(s1,false) );
                Log.i("test>>>>>>>>", tem);
                return tem;
            }catch (UnsupportedEncodingException ex){
                return "";
            }catch (NoSuchAlgorithmException ex2){
                return "";
            }
        }
    }

}

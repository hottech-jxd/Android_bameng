package com.bameng.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.bameng.BaseApplication;
import com.bameng.config.Constants;
import com.bameng.model.AccountModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 授权参数构建类
 */
public class AuthParamUtils {

    public AuthParamUtils()
    {


    }








    private Map removeNull(Map map)
    {
        Map<String, String> lowerMap = new HashMap< String, String >(  );
        Iterator lowerIt = map.entrySet ().iterator ();
        while ( lowerIt.hasNext () )
        {

            Map.Entry entry = ( Map.Entry ) lowerIt.next ();
            Object value = entry.getValue ( );
            if( null != value )
            {
                lowerMap.put ( String.valueOf ( entry.getKey () ).toLowerCase (), String.valueOf ( value ) );
            }
        }

        return lowerMap;
    }




    public String getSign(Map map){
        return getSign(map , Constants.APP_SECRET);
    }

    public String getSign(Map map , String secret )
    {
        String values = this.doSort(map , secret );
        Log.i ( "sign", values );
        // values = URLEncoder.encode(values);
        //String signHex =DigestUtils.md5DigestAsHex(values.toString().getBytes("UTF-8")).toLowerCase();
        String signHex = EncryptUtil.getInstance().encryptMd532(values).toLowerCase();
        Log.i("signHex", signHex);
        return signHex;
    }

    /**
     *
     * @方法描述：获取sign码第二步：参数排序
     * @方法名：doSort
     * @参数：@param map
     * @参数：@return
     * @返回：String
     * @exception
     * @since
     */
    private String doSort(Map<String, String> map , String secret )
    {
        //将MAP中的key转成小写
        Map<String, String> lowerMap = new HashMap< String, String >(  );
        Iterator lowerIt = map.entrySet ().iterator ();
        while ( lowerIt.hasNext () )
        {
            Map.Entry entry = ( Map.Entry ) lowerIt.next ();
            Object value = entry.getValue ( );
            if( ! TextUtils.isEmpty ( String.valueOf ( value ) ) )
            {
                lowerMap.put ( String.valueOf ( entry.getKey () ).toLowerCase (), String.valueOf ( value ) );
            }
        }

        TreeMap<String, String> treeMap = new TreeMap< String, String >( lowerMap );
        StringBuffer buffer = new StringBuffer();
        Iterator it = treeMap.entrySet ().iterator ();
        while(it.hasNext ())
        {
            Map.Entry entry =(Map.Entry) it.next();
            buffer.append ( entry.getKey ()+"=" );
            buffer.append ( entry.getValue ()+"&" );
        }
        String suffix = buffer.substring ( 0, buffer.length ()-1 )+ secret;//Constants.getAPP_SECRET();
        return suffix;
    }



}

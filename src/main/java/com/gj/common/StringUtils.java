package com.gj.common;

import java.security.MessageDigest;
import java.util.Map;

public class StringUtils {

    /**
     * map转拼接数据格式并且进行MD5加密
     * 例如: 1=123&2=123&
     * @param map
     * @return
     */
    public static String StringSignMd5(Map<String,String> map,String key){
        String substring = map.toString().substring(1, map.toString().length() - 1);
        String replace = substring.replace(", ", "&");
        return MD5Utils.getPwd(replace+"&key="+key).toUpperCase();
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(Map<String,Object> data) throws Exception {
        String substring = data.toString().substring(1, data.toString().length() - 1);
        String replace = substring.replace(", ", "&");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(replace.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
    public static String StringPayXML(Map<String,String> map){
        String substring = map.toString().substring(1, map.toString().length() - 1);
        String[] split = substring.split(", ");
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>");
        for (String strings:split){
            String[] builderSplit = strings.split("=");
            builder.append("<"+builderSplit[0]+">").append(builderSplit[1]).append("</"+builderSplit[0]+">");
        }
        builder.append("</xml>");
        return builder.toString();
    }
}

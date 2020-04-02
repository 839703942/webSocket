package com.gj.controller;

import com.gj.common.StringUtils;
import com.gj.common.WXPayUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gj.common.EnumResponse.*;
import static com.gj.common.HttpUrl.PAY_URL;

/**
 * 小程序支付
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_TYPE);

    @GetMapping(value = "/program")
    public Map<String,String> Pay(@RequestParam(name = "money")String money,
                                  HttpServletRequest request
                                  ) throws Exception {
        Map<String, String> map = new TreeMap<>();
        map.put("appid",APPID);//小程序ID
        map.put("attach",ATTACH);//商品描述
        map.put("body",BODY);//商品描述
        map.put("mch_id",MCH_ID);//商户号
        map.put("nonce_str",String.valueOf(System.currentTimeMillis() * 10));//随机串
        map.put("notify_url","待定");//通知地址
        map.put("openid","ohnXm5dWzZiVlcW68dd9N-iuGY1c");//openid
        map.put("out_trade_no",String.valueOf(System.currentTimeMillis()));//商户订单号（时间戳）
        map.put("spbill_create_ip",request.getRemoteAddr());//终端ip地址(调用微信支付用户ip)
        map.put("total_fee",money);//标价金额(默认为分)
        map.put("trade_type","JSAPI");//交易类型
        map.put("sign",StringUtils.StringSignMd5(map,KEY).toUpperCase());//签名

        //转xml格式
        String xml = StringUtils.StringPayXML(map);
        String content = WXPayUtil.httpRequest(PAY_URL,HTTP_POST,xml);
        //把返回的XML转map
        Map<String, String> xmlMap = WXPayUtil.xmlToMap(content);
        if(xmlMap.get("return_code").equals("SUCCESS")&&xmlMap.get("return_msg").equals("OK")){
            Map<String,String> signMap = new TreeMap<>();
            //拼接签名
            String nonceStr = String.valueOf(System.currentTimeMillis());
            long dataTime = System.currentTimeMillis();
            signMap.put("appId",MCH_ID);
            signMap.put("nonceStr",nonceStr);
            signMap.put("package",xmlMap.get("prepay_id"));
            signMap.put("timeStamp",String.valueOf(dataTime));
            String sign = StringUtils.StringSignMd5(signMap, KEY).toUpperCase();
            signMap.clear();//清空map

            signMap.put("sign",sign);
            signMap.put("timeStamp",String.valueOf(dataTime));
            signMap.put("nonceStr",nonceStr);
            signMap.put("package","prepay_id="+xmlMap.get("prepay_id"));
            return signMap;
        }
        return xmlMap;
    }

    @PostMapping(value = "/PayCallback")
    public void PayCallback(){
        System.out.println("回调成功");
    }

    public static void main(String[] args) {
        Map<String, Object> map = new TreeMap<>();
        map.put("d","123");
        map.put("c","123");
        map.put("e","123");
        String substring = map.toString().substring(1, map.toString().length() - 1);
        String[] split = substring.split(", ");
        StringBuilder builder = new StringBuilder();
        builder.append("<xml>");
        for (String strings:split){
            String[] builderSplit = strings.split("=");
            builder.append("<"+builderSplit[0]+">").append(builderSplit[1]).append("</"+builderSplit[0]+">");
        }
        builder.append("</xml>");
        System.out.println(builder.toString());
    }
}

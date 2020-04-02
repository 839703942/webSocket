package com.gj.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.*;

public class WXPayUtil {

    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    private static boolean isValidChar(char ch) {
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
            return true;
        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
            return true;// 简体中文汉字编码
        return false;
    }
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        // 创建SSLContext
        StringBuffer buffer = null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            //往服务器端写内容
            if(null !=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

                br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
	 /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }






	/*
    * IpUtils工具类方法
    * 获取真实的ip地址
    * @param request
    * @return
    */
   public static String getIpAddr(HttpServletRequest request) {
       String ip = request.getHeader("X-Forwarded-For");
       if( ip != null && ip.length() > 0 && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
           int index = ip.indexOf(",");
           if(index != -1){
               return ip.substring(0,index);
           }else{
               return ip;
           }
       }
       ip = request.getHeader("X-Real-IP");
       if(ip != null && ip.length() > 0 && !"unKnown".equalsIgnoreCase(ip)){
          return ip;
       }
       return request.getRemoteAddr();
   }

   /**
    * XML格式字符串转换为Map
    *
    * @param strXML XML字符串
    * @return XML数据转换后的Map
    * @throws Exception
    */
   public static Map<String, String> xmlToMap(String strXML) throws Exception {
       try {
           Map<String, String> data = new HashMap<String, String>();
           DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
           InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
           org.w3c.dom.Document doc = documentBuilder.parse(stream);
           doc.getDocumentElement().normalize();
           NodeList nodeList = doc.getDocumentElement().getChildNodes();
           for (int idx = 0; idx < nodeList.getLength(); ++idx) {
               Node node = nodeList.item(idx);
               if (node.getNodeType() == Node.ELEMENT_NODE) {
                   org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                   data.put(element.getNodeName(), element.getTextContent());
               }
           }
           try {
               stream.close();
           } catch (Exception ex) {
               // do nothing
           }
           return data;
       } catch (Exception ex) {
           WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
           throw ex;
       }

   }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }
    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 日志
     * @return
     */
    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


  /**
   * 除去数组中的空值和签名参数
   * @param sArray 签名参数组
   * @return 去掉空值与签名参数后的新签名参数组
   */
  public static Map<String, String> paraFilter(Map<String, String> sArray) {
    Map<String, String> result = new HashMap<String, String>();
    if (sArray == null || sArray.size() <= 0) {
      return result;
    }
    for (String key : sArray.keySet()) {
      String value = sArray.get(key);
      if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
        || key.equalsIgnoreCase("sign_type")) {
        continue;
      }
      result.put(key, value);
    }
    return result;
  }

  public static String doRefund(HttpServletRequest request, String url, String data) throws Exception{
    /**
     * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
     */

    KeyStore keyStore  = KeyStore.getInstance("PKCS12");
    String substring = request.getSession().getServletContext().getRealPath("/").substring(0, request.getSession().getServletContext().getRealPath("/").lastIndexOf("webapp\\"));
    FileInputStream instream = new FileInputStream(substring+"resources/refund_certificate/apiclient_cert.p12");//P12文件目录 证书路径
    System.out.println(substring);
    try {
      /**
       * 此处要改
       * */
      keyStore.load(instream,"1482533172".toCharArray());//这里写密码..默认是你的MCHID
    } finally {
      instream.close();
    }

    // Trust own CA and all self-signed certs
    /**
     * 此处要改
     * */
    SSLContext sslcontext = SSLContexts.custom()
      .loadKeyMaterial(keyStore, "1482533172".toCharArray())//这里也是写密码的
      .build();
    // Allow TLSv1 protocol only
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
      sslcontext,
      new String[] { "TLSv1" },
      null,
      SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    CloseableHttpClient httpclient = HttpClients.custom()
      .setSSLSocketFactory(sslsf)
      .build();
    try {
      HttpPost httpost = new HttpPost(url); // 设置响应头信息
      httpost.addHeader("Connection", "keep-alive");
      httpost.addHeader("Accept", "*/*");
      httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      httpost.addHeader("Host", "api.mch.weixin.qq.com");
      httpost.addHeader("X-Requested-With", "XMLHttpRequest");
      httpost.addHeader("Cache-Control", "max-age=0");
      httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
      httpost.setEntity(new StringEntity(data, "UTF-8"));
      CloseableHttpResponse response = httpclient.execute(httpost);
      try {
        HttpEntity entity = response.getEntity();

        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        return jsonStr;
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }
}

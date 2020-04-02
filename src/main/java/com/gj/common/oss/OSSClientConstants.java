package com.gj.common.oss;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
public class OSSClientConstants {
    //阿里云API的外网域名
    @Value("${oss.endpoint}")
    private  String endpoint;

    //阿里云API的密钥Access Key ID
    @Value("${oss.access-key-id}")
    private  String accessKeyId ;

    //阿里云API的密钥Access Key Secret
    @Value("${oss.access-key-secret}")
    private  String accessKeySecret ;

    //阿里云API的bucket名称
    @Value("${oss.backet-name}")
    private  String backetName;

    //阿里云API的文件夹名称
    @Value("${oss.folder}")
    private  String folder;

    //阿里雲api的外網Bucket域名
    @Value("${oss.aly-url}")
    private  String alyUrl;

    //需要刪除的圖片名稱
    @Value("${oss.backet-name}")
    private String delName;

    //圖片字節數
    @Value("${oss.imageSize}")
    private String ImageSize;

    public long GetImageSize(){
        //1048576是1M流量的字節數大小
        long imageSize = Long.parseLong(ImageSize)*1048576;
        return imageSize;
    }
}

package com.gj.common.oss;

import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 圖片操作
 */
@Slf4j
@Component
public class OperationImage{
    //log日志
    private static Logger logger = log;

    @Autowired
    private  OssExecutorInit ossExecutorInit ;
    /**
     * 根據相關數據刪除圖片
     * name="backetName" value="lizhuor"
     * name="folder" value="imag/"
     * name="delName" value="sdasd.jpg"
     * @return
     */
    public String DeleteImage(String BacketName,String Folder,String DelName){
        try{
            OSSClientConstants ossData = ossExecutorInit.OssExecutorInit();
            OSSClient ossClient=ossExecutorInit.OssExecutorInit(ossData);
            AliyunOSSClientUtil.deleteFile(ossClient,BacketName,Folder,DelName);
            return "刪除成功";
        }catch (Exception e){
            e.printStackTrace();
            return "刪除異常，請檢查配置項";
        }
    }


    /**
     * 阿里雲oss圖片上傳，上傳
     * @param file 圖片流
     * @param
     */
    public String  UploadImage(MultipartFile file) {
        OSSClientConstants ossData = ossExecutorInit.OssExecutorInit();
        logger.info("請求參數={}",ossData);
        if(file.getSize()>=ossData.GetImageSize()){
            logger.info("文件過大");
            return "文件過大，不能大于1M";
        }
        try {
            //初始化OSSClient
            OSSClient ossClient=OssExecutorInit(ossData);
            String url = AliyunOSSClientUtil.uploadObject2OSS(ossClient, file, ossData.getBacketName(), ossData.getFolder(),ossData.getAlyUrl());
            logger.info("上傳后返回的url:" + url);
            //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
            return url;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("上傳異常");
            return null;
        }
    }

    public  OSSClient OssExecutorInit(OSSClientConstants ossData){
        OSSClient ossClient = new OSSClient(ossData.getEndpoint(),ossData.getAccessKeyId(),ossData.getAccessKeySecret());
        return ossClient;
    }
}

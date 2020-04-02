package com.gj.common.oss;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class OssExecutorInit {
    @Bean
    public OSSClientConstants OssExecutorInit(){
        return new OSSClientConstants();
    }

    @Bean
    public  OSSClient OssExecutorInit(OSSClientConstants ossData){
        OSSClient ossClient = new OSSClient(ossData.getEndpoint(),ossData.getAccessKeyId(),ossData.getAccessKeySecret());
        return ossClient;
    }

}

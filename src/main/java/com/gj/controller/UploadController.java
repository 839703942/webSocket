package com.gj.controller;

import com.gj.common.ResponseMessage;
import com.gj.common.oss.OperationImage;
import com.gj.sendall.MyWebsocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/Upload")
public class UploadController{

    @Autowired
    private OperationImage operationImage;

    @Autowired
    private MyWebsocketServer myWebsocketServer;
    @PostMapping(value = "/ImageUpload")
    public ResponseMessage ImageUpload(MultipartFile file,String name,String userName){

        String image = operationImage.UploadImage(file);
        if(image.equals("文件過大，不能大于1M")){
            return ResponseMessage.Fail(image);
        }
        myWebsocketServer.ImageSend(name,image,userName);
        return ResponseMessage.Success(image);
    }
}

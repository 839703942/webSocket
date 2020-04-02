package com.gj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping(value = "/html")
public class HTMLController {
    /**
     * 链接websocket
     * @return
     */
    @GetMapping(value = "/websocket")
    public String websocket(){
        return "websocket";
    }

    /**
     * 链接websocket
     * @return
     */
    @GetMapping(value = "/registered")
    public String registered(){
        return "/html/user/registered.html";
    }

    /**
     * 链接websocket
     * @return
     */
    @GetMapping(value = "/login")
    public String login(){
        return "/html/user/login.html";
    }

    /**
     * 链接websocket
     * @return
     */
    @GetMapping(value = "/index")
    public String index(){
        return "/html/user/index.html";
    }
}

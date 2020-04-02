package com.gj.sendall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gjing
 **/
@ServerEndpoint("/test/{user}/{name}")
@Component
@Slf4j
@RestController
public class MyWebsocketServer{
    final int MAX_THREADS = 3; //定义线程数最大值

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Map<String, Session>> mapClients = new ConcurrentHashMap<>();
    private static int index = 0;//当前人数
    private static int room = mapClients.size();//当前聊天室数量
    private static final Map<String,Set<String>> map = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("user")String user) {
        log.info("有新的客户端连接了: {}", session.getId());
        //将新用户存入在线的组
        index++;
        Map<String, Session> clients = new ConcurrentHashMap<>();
        clients.put(session.getId(), session);
        if(mapClients.get(user)!=null){
            mapClients.get(user).put(session.getId(), session);
        }else {
            mapClients.put(user,clients);
        }
        log.info("当前人数:{},当前聊天室数量:{}",index,mapClients.size());
    }

    /**
     * 客户端关闭
     * @param session session
     */
    @OnClose
    public void onClose(Session session,@PathParam("user")String user,@PathParam("name")String name) {
        log.info("有用户断开了,聊天室为:{}, id为:{}", user,session.getId());
        //将掉线的用户移除在线的组里
        mapClients.get(user).remove(session.getId());
        //如果聊天室人员为0则删除聊天室
        index--;
        if(mapClients.get(user).size()==0){
            mapClients.remove(user);
            room--;
        }
        log.info("当前人数:{},当前聊天室数量:{}",index,mapClients.size());
        send("《"+name+"》气宇轩昂的走了",user);
    }

    /**
     * 发生错误
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }


    /**
     * 收到客户端发来消息
     * @param message  消息对象
     */
    @OnMessage
    public void onMessage(String message,@PathParam("user")String user) {
        log.info("服务端收到客户端发来的消息: {}", message);
        this.send(message,user);
    }

    /**
     * 群发消息
     * @param message 消息内容
     */
    private void send(String message,@PathParam("user")String user) {
        for (Map.Entry<String, Session> sessionEntry : mapClients.get(user).entrySet()) {
            sessionEntry.getValue().getAsyncRemote().sendText(message);
        }
    }

    public void ImageSend(String user,String url,String userName){
        ExecutorService es1 = Executors.newFixedThreadPool(MAX_THREADS);
        es1.submit(new Runnable() {
            @Override
            public void run() {
                //判断聊天室是否为空
                String massage=userName+"歷繨韃<li class='liName'>"+userName+"</li><li class='liContent'><img onclick='onclickImg(this.src)' src="+url+" class='imgLi' src='+url+'/></li>";
                send(massage,user);
            }
        });

    }
}





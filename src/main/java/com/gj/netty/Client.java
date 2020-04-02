package com.gj.netty;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

    private NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    private Bootstrap bootstrap;

    public static void main(String[] args) {
        Client  client = new Client();

        client.start();

        client.sendData();      
    }

    private void start() {
        bootstrap = new Bootstrap();        
        bootstrap.group(worker)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                // TODO Auto-generated method stub
                ChannelPipeline pipeline = ch.pipeline();
                   /*
                    readerIdleTimeSeconds: 读超时. 即当在指定的时间间隔内没有从 Channel 读取到数据时, 会触发一个 READER_IDLE 的 IdleStateEvent 事件.
                    writerIdleTimeSeconds: 写超时. 即当在指定的时间间隔内没有数据写入到 Channel 时, 会触发一个 WRITER_IDLE 的 IdleStateEvent 事件.
                     allIdleTimeSeconds: 读/写超时. 即当在指定的时间间隔内没有读或写操作时, 会触发一个 ALL_IDLE 的 IdleStateEvent 事件.*/
                pipeline.addLast(new IdleStateHandler(10,0,0));

                pipeline.addLast(new MsgPckDecode());

                pipeline.addLast(new MsgPckEncode());

                pipeline.addLast(new Client3Handler(Client.this));              
            }           
        }); 
        doConnect();
    }

    /**
     * 连接服务端 and 重连
     */
    protected void doConnect() {

        if (channel != null && channel.isActive()){
            return;
        }       
        ChannelFuture connect = bootstrap.connect("192.168.124.13", 8080);
        //实现监听通道连接的方法
        connect.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

                if(channelFuture.isSuccess()){
                    channel = channelFuture.channel();
                    System.out.println("连接成功");

                }else{
                    System.out.println("每隔2s重连....");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {

                        @Override
                        public void run() {
                            // TODO -generated method stub
                            doConnect();
                        }
                    },2,TimeUnit.SECONDS);
                }   
            }
        });     
    }   
    /**
     * 向服务端发送消息
     */
    private void sendData() {
        Scanner sc= new Scanner(System.in); 
        for (int i = 0; i < 1000; i++) {

            if(channel != null && channel.isActive()){              
                //获取一个键盘扫描器
                String nextLine = sc.nextLine();
                Model model = new Model();

                model.setType(TypeData.CUSTOMER);

                model.setBody(nextLine);

                channel.writeAndFlush(model);
            }
        }
    }
}
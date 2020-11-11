package com.yanyi.fire.im;

import com.happygame.facade.im.ChatMsg;
import com.happygame.facade.im.ConstantValue;
import com.happygame.facade.im.MsgType;
import com.yanyi.fire.util.NettyDecode;
import com.yanyi.fire.util.NettyEncode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

import org.jetbrains.annotations.NotNull;

/**
 * @author chenqiang
 * @date 2020-11-05 16:31
 */
public class NettyClient extends Thread {
    @SneakyThrows
    @Override
    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NotNull SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new NettyDecode())
                                .addLast("encoder", new NettyEncode())
                                .addLast(new ClientHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect("192.168.2.39", ConstantValue.NETTY_PORT).sync();
        System.out.println("客户端启动");
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setFromId(1L);
        chatMsg.setMsgType(MsgType.ONLINE);
        future.channel().writeAndFlush(chatMsg);
        ChatMsg msg = new ChatMsg();
        msg.setMsgType(MsgType.MSG);
        msg.setFromId(1L);
        msg.setDestId(1L);
        msg.setMsg("1 发向 自己 的消息");
        future.channel().writeAndFlush(msg);
    }

}


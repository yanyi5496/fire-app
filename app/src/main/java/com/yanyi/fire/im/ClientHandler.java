package com.yanyi.fire.im;




import com.happygame.facade.im.ChatMsg;
import com.happygame.facade.im.ConstantValue;
import com.happygame.facade.im.MsgType;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author chenqiang
 * @date 2020-11-05 16:33
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ChatMsg) {
            System.out.println("msg = " + msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.close();
    }


    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        ping(ctx.channel());
    }

    /**
     * PING服务端保持心跳
     *
     * @param channel channel
     */
    private void ping(Channel channel) {
        ScheduledFuture<?> future = channel.eventLoop().schedule(() -> {
            if (channel.isActive()) {
                ChatMsg heartMsg = new ChatMsg();
                heartMsg.setMsgType(MsgType.HEARTBEAT);
                channel.writeAndFlush(heartMsg);
            } else {
                channel.closeFuture();
                throw new RuntimeException();
            }
        }, ConstantValue.HEART_BEAT_CLIENT, TimeUnit.SECONDS);

        future.addListener(future1 -> {
            if (future1.isSuccess()) {
                ping(channel);
            } else {
                throw new RuntimeException();
            }
        });
    }

}


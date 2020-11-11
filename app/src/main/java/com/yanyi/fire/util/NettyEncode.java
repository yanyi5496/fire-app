package com.yanyi.fire.util;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义序列化方式
 *
 * @author chenqiang
 * @date 2020-11-05 17:04
 */
public class NettyEncode extends MessageToByteEncoder<Serializable> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        //将对象转换为byte
        byte[] body = ObjectAndByte.toByteArray(msg);
        //读取消息的长度
        int dataLength = body.length;
        //先将消息长度写入，也就是消息头
        out.writeInt(dataLength);
        //消息体中包含我们要发送的数据
        out.writeBytes(body);
    }
}

package com.yanyi.fire.util;


import com.happygame.facade.im.ConstantValue;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自定义序列化方式
 *
 * @author chenqiang
 * @date 2020-11-05 17:01
 */
public class NettyDecode extends ByteToMessageDecoder {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
        if (in.readableBytes() < ConstantValue.HEAD_LENGTH) {
            return;
        }
        //我们标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        int dataLength = in.readInt();
        // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < 0) {
            ctx.close();
        }

        ////读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        // 嗯，这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~~
        byte[] body = new byte[dataLength];
        in.readBytes(body);
        //将byte数据转化为我们需要的对象。伪代码，用什么序列化，自行选择
        Object o = ObjectAndByte.toObject(body);
        out.add(o);
    }
}

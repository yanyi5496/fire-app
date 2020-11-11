package com.happygame.facade.im;

/**
 * @author chenqiang
 * @date 2020-11-04 11:35
 */
public class ConstantValue {
    /** 消息ID的步进 */
    public static final long SEQ_STEP = 100;
    /** 消息ID的初始值*/
    public static final long SEQ_INIT = 0;
    /** 报文头长度*/
    public static final int HEAD_LENGTH = 4;
    /** Netty的端口 */
    public static final int NETTY_PORT = 8000;
    /** 服务端心跳时间间隔*/
    public static final int HEART_BEAT_SERVER = 2 * 60;
    /** 客户端心跳时间间隔 */
    public static final int HEART_BEAT_CLIENT = HEART_BEAT_SERVER - 5;
    /** 打印在线用户定时器间隔*/
    public static final String ONLINE_USER_LOG_TIME = "0/30 * * * * ?";
}

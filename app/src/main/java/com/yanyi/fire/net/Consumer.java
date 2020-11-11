package com.yanyi.fire.net;

/**
 * lambda 用, 为了向下兼容, 参考官方文档
 *
 * @author changzhu.zhao
 */
public interface Consumer<U> {
    /**
     * 接受1个参数, 无返回值
     */
    void accept(U t);
}
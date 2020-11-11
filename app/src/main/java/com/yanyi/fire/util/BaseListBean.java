package com.yanyi.fire.util;

import java.util.List;

/**
 * 用于分页的数据基类
 */
public abstract class BaseListBean<T> {
    /**
     * 是否还有更多数据
     */
    public boolean isMore;

    /**
     * 获取更多数据
     */
    public abstract List<T> getList();

    /**
     * 创建
     */
    public static <E> BaseListBean<E> create(final List<E> list) {
        return new BaseListBean<E>() {
            @Override
            public List<E> getList() {
                return list;
            }
        };
    }
}

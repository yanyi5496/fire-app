package com.yanyi.fire.util;

import android.text.TextUtils;

/**
 * 响应基类
 *
 * @author bonan 2018/5/18
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseResponse<T> {

    private Boolean status;
    private String errorCode;
    private String errorMsg;
    private T data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}

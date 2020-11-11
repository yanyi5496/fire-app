package com.yanyi.fire.util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;

/**
 * 网络请求结果解析
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsonCallback<T> extends AbsCallback<BaseResponse<T>> {

    protected boolean handleSuccess = true;
    protected boolean customHandleError = false; // 自己处理服务器返回的异常

    @Nullable
    private final RespLiveData<T> liveData;
    private Class<T> tClass;
    private boolean isMore;

    /**
     * 用于匿名类, 列表
     */
    protected JsonCallback(@Nullable RespLiveData<T> liveData) {
        super();
        this.liveData = liveData;
    }

    /**
     * 一种通用用法
     *
     * @param tClass 返回值类型
     */
    public JsonCallback(@Nullable RespLiveData<T> liveData, @NonNull Class<T> tClass) {
        this(liveData, tClass, false);
    }

    /**
     * 用于分页加载
     *
     * @param tClass 返回值类型
     * @param isMore 是否有更多数据
     */
    public JsonCallback(@Nullable RespLiveData<T> liveData, @NonNull Class<T> tClass, boolean isMore) {
        super();
        this.liveData = liveData;
        this.tClass = tClass;
        this.isMore = isMore;
    }

    /**
     * 便携方法, 不自定义 onSuccess 等, 回调后自动为 liveData 赋值.
     */
    public static <T> JsonCallback<T> newInstance(@NonNull RespLiveData<T> liveData, @NonNull Class<T> tClass) {
        return new JsonCallback<>(liveData, tClass);
    }

    /**
     * 便携方法, 分页
     */
    public static <T> JsonCallback<T> newInstance(@NonNull RespLiveData<T> liveData, @NonNull Class<T> tClass, boolean isMore) {
        return new JsonCallback<>(liveData, tClass, isMore);
    }

    /**
     * 便携方法, 自定义 onSuccess
     */
    public static <T> JsonCallback<T> newInstance(@NonNull RespLiveData<T> liveData, @NonNull Class<T> tClass,
                                                  @NonNull Consumer<T> onSuccess) {
        return newInstance(liveData, tClass, onSuccess, null);
    }

    /**
     * 便携方法, 自定义 onSuccess, onError
     */
    public static <T> JsonCallback<T> newInstance(@Nullable RespLiveData<T> liveData, @NonNull Class<T> tClass,
                                                  @Nullable final Consumer<T> onSuccess, @Nullable final Consumer<Throwable> onError) {
        return new JsonCallback<T>(liveData, tClass) {
            @Override
            public void onSuccess(@NonNull Response<BaseResponse<T>> response) {
                super.onSuccess(response);
                if (onSuccess != null) {
                    onSuccess.accept(response.body().getData());
                }
            }

            @Override
            public void onError(Response<BaseResponse<T>> response) {
                super.onError(response);
                if (onError != null) {
                    onError.accept(response.getException());
                }
            }
        };
    }

    @Override
    public void onSuccess(@NonNull Response<BaseResponse<T>> response) {
        if (liveData == null || !handleSuccess) {
            return;
        }
        BaseResponse<T> base = response.body();
        if (base != null) {
            T data = base.getData();
            if (isMore && data instanceof BaseListBean) {
                ((BaseListBean) data).isMore = true;
            }
            liveData.setValue(data);
        }
    }


    @Override
    public BaseResponse<T> convertResponse(okhttp3.Response response) throws Throwable {
        Type typeArg = tClass;
        if (typeArg == null) {
            Type genType = getClass().getGenericSuperclass();
            typeArg = ((ParameterizedType) genType).getActualTypeArguments()[0];
        }
        Type type = TypeToken.getParameterized(BaseResponse.class, typeArg).getType();

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else {
            throw new ParseException("网络请求泛型参数存在问题");
        }
    }

    /**
     * 自定义异常类型, 服务器异常
     */
    @SuppressWarnings("unused")
    private static class ServerFailException extends NetworkErrorException {

        private static final long serialVersionUID = -4235685863482688407L;
        private final String messageId;

        ServerFailException(String messageId, String message) {
            super(message);
            this.messageId = messageId;
        }
    }

    /**
     * 自定义异常类型, 解析异常
     */
    @SuppressWarnings("unused")
    private static class ParseException extends Exception {
        private static final long serialVersionUID = -4827722281714948509L;
        @SuppressWarnings("SameParameterValue")
        ParseException(String message) {
            super(message);
        }
    }


    @Nullable
    private BaseResponse<T> parseParameterizedType(@NonNull okhttp3.Response response, @Nullable ParameterizedType type) throws ServerFailException {
        if (type == null) {

            return null;
        }
        ResponseBody body = response.body();
        if (body == null) {

            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType(); // 泛型的实际类型
        // Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != BaseResponse.class) { // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)

            BaseResponse<T> b = Convert.fromJson(jsonReader, type);
            response.close();
            return b;
        } else { // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)或new JsonCallback<LzyResponse<Void>>(this)

            BaseResponse<T> dataResponse = null;
            try {
                dataResponse = Convert.fromJson(jsonReader, type);
            } catch (Exception e) {

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            }
            response.close();
            if (dataResponse.getStatus()) {
                return dataResponse;
            } else {
                if (dataResponse.getData() != null) {
                    return dataResponse;
                }
                throw new ServerFailException(dataResponse.getErrorMsg(),  ": " + dataResponse.getErrorMsg());
            }
        }
    }



    @Override
    public void onError(Response<BaseResponse<T>> response) {
        super.onError(response);
        Throwable e = response.getException();
        if (liveData != null && liveData.errorHandler != null) {
            liveData.errorHandler.onError(e);

            BaseResponse<T> base = response.body();
            if (base != null) {
                T data = base.getData();
                if (isMore && data instanceof BaseListBean) {
                    ((BaseListBean) data).isMore = true;
                }
                liveData.setValue(data);
            }
        }


        if (e instanceof ConnectException) {
            ToastUtils.showShort("R.string.net_error_connection_failed");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShort("R.string.net_error_connection_timeout");
        } else if (e instanceof ParseException || e instanceof JsonSyntaxException || e instanceof JsonIOException) {
            ToastUtils.showShort("R.string.net_error_parse_failed");
        } else if (e instanceof ServerFailException) {
            Context context = MyApplication.getAppContext();
            String messageId = ((ServerFailException) e).messageId;
            messageId = TextUtils.isEmpty(messageId) ? "0" : messageId;
            int id = context.getResources().getIdentifier(messageId, "string", context.getPackageName());
            if (id > 0) {
                if (!customHandleError) {
                    ToastUtils.showShort(context.getString(id));
                }
            } else {
                ToastUtils.showShort(e.getMessage());
            }
        } else {
            ToastUtils.showShort("R.string.net_error_other");
        }
    }

}

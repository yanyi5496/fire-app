package com.yanyi.fire.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.Locale;

import okhttp3.HttpUrl;

/**
 * 字符串相关
 *
 * @author zhaochangzhu 2018-10-09
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class StringUtils {

    public static final String EMPTY = "";
    public static final String NULL = "null";

    private StringUtils() {
    }

    /**
     * 根据资源名称获取String
     *
     * @param stringResName 资源名称
     * @param def           默认值
     */
    public static String getString(@Nullable String stringResName, String def) {
        String string = def;
        try {
            Context context = MyApplication.getAppContext();
            int id = context.getResources().getIdentifier(stringResName, "string", context.getPackageName());
            string = context.getString(id);
        } catch (Exception ignored) {
        }
        return string;
    }

    /**
     * 根据资源id获取String
     */
    public static String getString(@StringRes int resId) {
        return MyApplication.getAppContext().getString(resId);
    }

    /**
     * 根据资源id获取String
     */
    public static String getString(@StringRes int resId, Object... formatArgs) {
        return MyApplication.getAppContext().getString(resId, formatArgs);
    }

    /**
     * 解析String, 转换为int类型
     *
     * @param def 失败时的默认值
     */
    public static int toInt(String intString, int def) {
        int result = def;
        try {
            result = Integer.parseInt(intString);
        } catch (NumberFormatException ignored) {
        }

        return result;
    }

    /**
     * 设置列表结果数, 只是一个便捷方法.
     */
    public static void setListCount(@Nullable TextView view, int count) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        //view.setText(view.getResources().getQuantityString(R.plurals.searched_xxx_results, count, count));
    }

    /**
     * 获取TextView的text
     */
    public static String getText(TextView view) {
        return view.getText().toString().trim();
    }

    /**
     * 根据viewId获取TextView的text
     *
     * @param view root View
     */
    public static String getText(@NonNull View view, @IdRes int id) {
        return view.<TextView>findViewById(id).getText().toString().trim();
    }

    /**
     * 获取TextView的text, 并转换为int类型
     */
    public static int getInt(TextView view, int def) {
        int result = def;
        try {
            result = Integer.parseInt(getText(view));
        } catch (NumberFormatException ignored) {
        }

        return result;
    }

    /**
     * float 转换成字符串, 只保留整数部分
     */
    public static String floatToStr0(float num) {
        return String.format(Locale.getDefault(), "%.0f", num);
    }

    /**
     * float 转换成字符串, 只保留整数部分, 可设置是否为0时显示.
     */
    public static String floatToStr0(float num, boolean showZero) {
        if ((int) num == 0 && !showZero) {
            return "";
        } else {
            return String.format(Locale.getDefault(), "%.0f", num);
        }
    }

    /**
     * 转换为大写
     */
    public static String toUpper(@Nullable String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }

    /**
     * 将 url 格式化为 host:port 的形式，如果 port=80或443，不显示port
     */
    public static String formatUrl(String url) {
        String newUrl = "";
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = "https://" + url;
            }
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl != null) {
                int port = httpUrl.port();
                newUrl = httpUrl.host() + ((port == 443 || port == 80) ? "" : (":" + port));
            }
        }
        return newUrl;
    }

    /**
     * 相比于 TextUtils, 增加 “null” 的判断
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0 || text.equalsIgnoreCase(NULL);
    }

}

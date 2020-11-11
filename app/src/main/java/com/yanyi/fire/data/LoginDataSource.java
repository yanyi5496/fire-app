package com.yanyi.fire.data;

import com.lzy.okgo.OkGo;
import com.yanyi.fire.net.BaseResponse;
import com.yanyi.fire.net.JsonCallback;
import com.yanyi.fire.net.RespLiveData;
import com.yanyi.fire.ui.login.LoginResult;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginDataSource {
    private static final String TAG = "LoginDataResource";

    public void login(final String account, final String password, final RespLiveData<LoginResult> liveData) {

        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        JSONObject jsonObject = new JSONObject(params);
        OkGo.<BaseResponse<LoginResult>>post("http://192.168.2.39:2333/user/login")
                .upJson(jsonObject)
                .execute(new JsonCallback<>(liveData, LoginResult.class));
    }
}

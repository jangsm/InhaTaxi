package com.inhataxi.utils;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.inhataxi.IngaTaxiApp.LOGIN_TYPE;
import static com.inhataxi.IngaTaxiApp.sSharedPreferences;


public class LoginTypeInterceptor implements Interceptor {

    public LoginTypeInterceptor() {
    }

    @Override
    @NonNull
    public Response intercept(@NonNull final Interceptor.Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        final String loginType = sSharedPreferences.getString(LOGIN_TYPE, "");
        if (!loginType.isEmpty()) {
            builder.addHeader("LOGIN-TYPE", loginType);
        }
        return chain.proceed(builder.build());
    }
}

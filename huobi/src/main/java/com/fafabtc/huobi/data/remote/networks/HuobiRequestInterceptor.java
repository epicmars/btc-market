package com.fafabtc.huobi.data.remote.networks;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiRequestInterceptor implements Interceptor {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().header("User-Agent", USER_AGENT).build();
        return chain.proceed(request);
    }
}

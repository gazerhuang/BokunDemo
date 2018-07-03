package cn.sh.bokun.bokundemo.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {
    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);

//        String sessionId = null;
//        if (PreferenceHelper.getUserSessionId() != null){
//            sessionId = PreferenceHelper.getUserSessionId();
//        }
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(address)
//                .header("Cookie","JSESSIONID="+sessionId)
//                .build();
//        client.newCall(request).enqueue(callback);

    }
}

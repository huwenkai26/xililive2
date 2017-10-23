package com.example.xililive.fanguo;


import com.example.xililive.*;
import com.example.xililive.meme.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FgMainActivity {

    private StringBuilder stringBuilder;
    private static String url = "http://niukou.ysgfgj.com/mapi/index.php";
    private static OkHttpClient client;
    private static CountDownLatch cdl = null;


    public  String domain() {

        final StringBuilder stringBuilder = new StringBuilder();
        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();


        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            final IndexBean indexBean = new Gson().fromJson(response.body().string(), IndexBean.class);
            List<IndexBean.ListEntity> list = indexBean.list;
            if (list.size() != 0) {
                final CountDownLatch cdl2 = new CountDownLatch(indexBean.list.size());

                for (int i = 0; i < indexBean.list.size(); i++) {
                    final int finalI = i;

                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try { String roomid =indexBean.list.get(finalI).room_id ;
                                        String sign =  MD5Util.MD5("1400045740" +"200025"  + roomid);
                                        String re =  AESUtil.encrypt(
                                                "{\"screen_width\":720,\"screen_height\":1280,\"sdk_type\":\"android\",\"sdk_version_name\":\"2.5.2\",\"sdk_version\":2017102101,\"xpoint\":113.913439,\"ypoint\":22.548069,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":"+roomid+",\"is_vod\":0,\"sign\":\""+sign+"\"}","1400045740000000");

                                        System.out.println(URLEncoder.encode(re));
                                        String ss = "requestData=" + URLEncoder.encode(re) + "&i_type=1&ctl=video&act=get_video2&itype=&sdk_version_name=2.5.2";

                                        String result = HttpUtils.sendPost("http://niukou.ysgfgj.com/mapi/index.php",ss,false);

                                        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                                        Gson gson = gsonBuilder.create();

                                        System.out.println(result);
                                        if(!result.contains("output")){
                                            cdl2.countDown();
                                            return;
                                        }
                                        BaseEncryptModel baseEncryptModel = gson.fromJson(result, BaseEncryptModel.class);

                                        String decrypt = AESUtil.decrypt(baseEncryptModel.getOutput(), "1400045740000000");

                                        System.out.println(decrypt);
                                        roominfo roomBean =gson.fromJson(decrypt, roominfo.class);


                                        if (roomBean.play_flv!=null&&!roomBean.play_flv.isEmpty()) {
                                            indexBean.list.get(finalI).play_flv = (AESUtil.decrypt(roomBean.play_flv,"Z#Er3XLGrM00Shsh"));
                                        }
                                        if (roomBean.play_mp4!=null&&!roomBean.play_mp4.isEmpty()) {
                                            indexBean.list.get(finalI).play_mp4 = (AESUtil.decrypt(roomBean.play_mp4,"Z#Er3XLGrM00Shsh"));
                                        }
                                        if (roomBean.play_rtmp!=null&&!roomBean.play_rtmp.isEmpty()) {
                                            indexBean.list.get(finalI).play_rtmp =  URLDecoder.decode((AESUtil.decrypt(roomBean.play_rtmp,"Z#Er3XLGrM00Shsh")),"utf-8");
                                        }
                                        if (roomBean.play_url!=null&&!roomBean.play_url.isEmpty()) {
                                            indexBean.list.get(finalI).play_url = URLDecoder.decode( (AESUtil.decrypt(roomBean.play_url,"Z#Er3XLGrM00Shsh")),"utf-8");
                                        }
                                        if (roomBean.preview_play_url!=null&&!roomBean.preview_play_url.isEmpty()) {
                                            indexBean.list.get(finalI).play_url = URLDecoder.decode((AESUtil.decrypt(roomBean.preview_play_url,"Z#Er3XLGrM00Shsh")),"utf-8");
                                        }


                                    }catch (Exception e){
                                        cdl2.countDown();
                                    }

                                    cdl2.countDown();

                                }
                            }
                    ).start();


                    Call call2 = client.newCall(parseRequestParams(indexBean.list.get(i)));



                }
                try {
                    cdl2.await();
                    String replaceAll = getDecodeString(stringBuilder, indexBean);

                    return replaceAll;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getDecodeString(StringBuilder stringBuilder, IndexBean indexBean) {
        stringBuilder.append(new Gson().toJson(indexBean));
//
        String replaceAll = stringBuilder.toString().replaceAll("\\\\u003d", "=");
        replaceAll = replaceAll.toString().replaceAll("\\\\u0026", "&");
        System.out.println(replaceAll);
        return replaceAll;
    }


    public  Request parseRequestParams(IndexBean.ListEntity listbean) {

        String roomid  = listbean.room_id;
        String sign =  MD5Util.MD5("1400045740" +"200025"  + roomid);
        String re =  AESUtil.encrypt(
                "{\"screen_width\":720,\"screen_height\":1280,\"sdk_type\":\"android\",\"sdk_version_name\":\"2.5.2\",\"sdk_version\":2017102101,\"xpoint\":113.913439,\"ypoint\":22.548069,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":"+roomid+",\"is_vod\":0,\"sign\":\""+sign+"\"}","1400045740000000");
        FormBody.Builder builder = new FormBody.Builder();
        String encode = URLEncoder.encode(re);

        builder.add("requestData", encode);
         builder.add("i_type", "1");
         builder.add("ctl", "video");
         builder.add("act", "get_video2");
         builder.add("itype", "");
         builder.add("sdk_version_name", "2.5.2");

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .post(body)
                .addHeader("Cookie", "client_ip=116.24.65.34; nick_name=200025; user_id=200025; user_pwd=c5a5f0a616dbd92846c4551f03a26960; PHPSESSID2=vvh4hmr1tu15s6vtb3qod5gif5; PHPSESSID=6krrhlvj05cvpk4sroaa8b3g73")
                .build();


        return request;
    }

}

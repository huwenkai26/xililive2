package com.example.xililive.meme;


import com.example.xililive.AESUtil;

import com.example.xililive.MD5Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MemeMainActivity {


    private static String url = "http://live.kbdaiban.com/mapi/index.php";
    private static OkHttpClient client;

    private   int kk = 0;

    public  String domain() {

        final StringBuilder stringBuilder = new StringBuilder();
        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //读取缓存文件

//        Request request = new Request.Builder().addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36").url(url).build();


        String respon = HttpUtils.httpRequest(url);
        System.out.println(respon);
        String decrypt = "";
        if(!respon.isEmpty()){
            String substring = respon.substring(11, respon.lastIndexOf("\"}")).replaceAll("\\\\","");
                    decrypt = AESUtil.decrypt(substring, "1400045299000000");
        }
        final TjIndexbean indexBean = new Gson().fromJson(decrypt, TjIndexbean.class);
        List<TjIndexbean.ListBean> list = indexBean.list;
        if (list.size() != 0) {
            final CountDownLatch cdl2 = new CountDownLatch(indexBean.list.size());
            System.out.println("xiancae"+indexBean.list.size());
            for (int i = 0; i < indexBean.list.size(); i++) {
                final int finalI = i;

                if(indexBean.list.get(i).is_live_pay.equals("1")){
                    cdl2.countDown();
                    kk++;
                    continue;

                }
                Call call2 = client.newCall(parseRequestParams(indexBean.list.get(i)));

                call2.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call arg0, IOException arg1) {

                        kk++;
                        cdl2.countDown();

                    }

                    @Override
                    public void onResponse(Call arg0, Response response) throws IOException {

                        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                        Gson gson = gsonBuilder.create();
                        String result = response.body().string();
                        if(result.isEmpty()){
                            cdl2.countDown();
                            kk++;
                            return;
                        }
                        if(result.contains("Blocked")){

                        }else if(result.contains("error")){
                            BaseEncryptModel baseEncryptModel = gson.fromJson(result, BaseEncryptModel.class);
                            String decrypt = AESUtil.decrypt(baseEncryptModel.output, "1400045299000000");

                            System.out.println(decrypt);
                            MmRoominfo roomBean =gson.fromJson(decrypt, MmRoominfo.class);
                            indexBean.list.get(finalI).play_url = roomBean.preview_play_url;

                        }else {
                            BaseEncryptModel baseEncryptModel = gson.fromJson(result, BaseEncryptModel.class);
                            String decrypt = AESUtil.decrypt(baseEncryptModel.output, "1400045299000000");

                            System.out.println(decrypt);
                            MmRoominfo roomBean =gson.fromJson(decrypt, MmRoominfo.class);
                            indexBean.list.get(finalI).play_url = roomBean.play_url;
                        }



                        cdl2.countDown();
                        kk++;
                        System.out.println(kk);

                    }

                });


            }
            try {
                cdl2.await();
                String replaceAll = getDecodeString(stringBuilder, indexBean);
                System.out.println(replaceAll);

                return replaceAll;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    private String getDecodeString(StringBuilder stringBuilder, TjIndexbean indexBean) {
        stringBuilder.append(new Gson().toJson(indexBean));
        String replaceAll = stringBuilder.toString().replaceAll("\\\\u003d", "=");
        replaceAll = replaceAll.toString().replaceAll("\\\\u0026", "&");
        return replaceAll;
    }


    public  Request parseRequestParams(TjIndexbean.ListBean listbean) {

        //key ---> 1400043672000000 json -->{"screen_width":720,"screen_height":1280,"sdk_type":"android","sdk_version_name":"2.3.3","sdk_version":2017091501,"xpoint":113.908448,"ypoint":22.548354,"ctl":"video","act":"get_video2","room_id":39417,"is_vod":0,"sign":"9d56d6ec34ff6522a55a3b931b204b1c"}
        /*yctjFuwawImx/YGYk34okRxXo846xWttT5S+l07qQPlWp2Xb8GCezmH0y93dEhZdWnXtoy8lUf/E
        JvN2e5QOUDoctNApJS99WsJPhFLTuOSyeghjWcpnqB6ht4JI6lx+dGnpaM9ZLDzQKZB4uUj1zaDY
        RDZUkB/OV66lv2lAWd1kb4p3fh/wWQGeQVSI6kE2zZE3qAf1b2k84fLvGtfmtiBr+YDpG2sX5f8f
        OyTBa70UyLyVElEUm2Fp13v3vUMOIxqhpoEWMzCfwqeHntsEul3bDxfAlUEQGbaafIAXqO6XL3kL
        3WQexOjAsTjvPQ4Ig8vabgjLr/MSGPhkgVD13w== */
//        HashMap<String, String> params = new HashMap<>();
//        params.put("ctl","video");
//        params.put("act","get_video2");
//        params.put("room_id", roomid);
//        params.put("is_vod", "0");
//        params.put("private_key", private_key);
//        String tencent_app_id = AppRuntimeWorker.getSdkappid();
//        params.put(c.SIGN, MD5Util.MD5(tencent_app_id + AppRuntimeWorker.getLoginUserID() + room_id)); tencent_app_id = "1400043672"
//        user_id = "801561"
        String roomid  = listbean.room_id;
        String sign =  MD5Util.MD5("1400045299" +"129401"  + roomid);
        System.out.println(roomid);
        String encryptData = AESUtil.encrypt("{\"screen_width\":1440,\"screen_height\":2560,\"sdk_type\":\"android\",\"sdk_version_name\":\"2.4.3\",\"sdk_version\":"+"2017101201"+",\"xpoint\":114.026852,\"ypoint\":22.524203,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":"+roomid+",\"is_vod\":0,\"sign\":\""+sign+"\"}","1400045299000000");
        FormBody.Builder builder = new FormBody.Builder();
         builder.add("requestData", encryptData);
         builder.add("i_type", "1");
         builder.add("ctl", "video");
         builder.add("act", "get_video2 ");
         builder.add("itype", "");
        builder.add("sdk_version_name", "2.4.3");

/*

        FormBody.Builder builder = new FormBody.Builder();


        String roomID = listbean.room_id;

        builder.add("screen_width", "1440");
        builder.add("screen_height", "2560");
        builder.add("sdk_type", "android");
        builder.add("sdk_version_name", "2017091601");
        builder.add("xpoint", "113.908472");
        builder.add("ypoint", "113.908472");
        builder.add("ctl", "video");
        builder.add("act", "get_video2");
        builder.add("room_id", roomID);
        builder.add("is_vod", "0");
        builder.add("type", "0");
        builder.add("has_scroll", "1");
        builder.add("sex", "0");
        builder.add("cate_id", "0");
        //todo 这里的要等模拟器支持微信


        String tencent_app_id = "1400030866";

//58442 9277e01bae73290dd84bda49f8a439cf

        builder.add("sign", MD5Util.MD5(tencent_app_id + "316393" + roomID));

        RequestBody body = builder.build();
*/
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .post(body).addHeader("User-Agent","Dalvik/1.6.0 (Linux; U; Android 4.4.4; SM-G925F Build/KTU84P)")
                .addHeader("Cookie", "client_ip=183.131.184.59; nick_name=129401; user_id=129401; user_pwd=7a6b3120a218ca2cc41a87efb2ac453e; PHPSESSID2=actvbbf49qeq6vhqkc19hd8t36; yd_cookie=77792023-53c6-4e0a73c42b1ca7835941f39190b7f3394537; PHPSESSID=kegdo8hbuu2d4shbr9a4pj59e5")
                .build();


        return request;
    }

}

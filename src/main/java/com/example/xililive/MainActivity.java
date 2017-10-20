package com.example.xililive;


import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity {

    private StringBuilder stringBuilder;
    private static String url = "http://starlight.xgauto.net/mapi/index.php";
    private static OkHttpClient client;
    private static CountDownLatch cdl = null;

    public static String domain() {

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


                    Call call2 = client.newCall(parseRequestParams(indexBean.list.get(i)));
                    call2.enqueue(new Callback() {

                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                            // TODO Auto-generated method stub
                            cdl2.countDown();
                        }

                        @Override
                        public void onResponse(Call arg0, Response response) throws IOException {

                            String result = response.body().string();
                            BaseEncryptModel baseEncryptModel = new Gson().fromJson(result, BaseEncryptModel.class);

//key 1400043672000000
                            String decrypt = AESUtil.decrypt(baseEncryptModel.getOutput(), "1400043672000000");
//                            System.out.println(decrypt);
                            roominfo roomBean = new Gson().fromJson(decrypt, roominfo.class);


                            if (roomBean.play_flv!=null&&!roomBean.play_flv.isEmpty()) {
                                indexBean.list.get(finalI).play_flv = (AESUtil.decrypt(roomBean.play_flv,"Z#Er3XLGrM00Shsh"));
                            }
                            if (roomBean.play_mp4!=null&&!roomBean.play_mp4.isEmpty()) {
                                indexBean.list.get(finalI).play_mp4 = (AESUtil.decrypt(roomBean.play_mp4,"Z#Er3XLGrM00Shsh"));
                            }
                            if (roomBean.play_rtmp!=null&&!roomBean.play_rtmp.isEmpty()) {
                                indexBean.list.get(finalI).play_rtmp = (AESUtil.decrypt(roomBean.play_rtmp,"Z#Er3XLGrM00Shsh"));
                            }
                            if (roomBean.play_url!=null&&!roomBean.play_url.isEmpty()) {
                                indexBean.list.get(finalI).play_url = (AESUtil.decrypt(roomBean.play_url,"Z#Er3XLGrM00Shsh"));
                            }
                            if (roomBean.preview_play_url!=null&&!roomBean.preview_play_url.isEmpty()) {
                                indexBean.list.get(finalI).play_url = (AESUtil.decrypt(roomBean.preview_play_url,"Z#Er3XLGrM00Shsh"));
                            }




                            cdl2.countDown();

                        }

                    });


                }
                try {
                    cdl2.await();
                    stringBuilder.append(new Gson().toJson(indexBean.list));
//                    writerTolocal(stringBuilder.toString()); 
//                    byte[] utf_8 = .getBytes("UTF-8");
//                    String json= new String(utf_8, "UTF-8");
//                    String gbkStr = new String(stringBuilder.toString().getBytes("ISO8859-1"), "GBK"); 
                    String replaceAll = stringBuilder.toString().replaceAll("\\\\u003d", "=");
//                    System.out.println(replaceAll);
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

    private static void writerTolocal(String string) {
        FileOutputStream writerStream;
        try {
            writerStream = new FileOutputStream("json.txt");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(new Gson().toJson(string));
            writer.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    public static Request parseRequestParams(IndexBean.ListEntity listbean) {

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
        String sign =  MD5Util.MD5("1400043672" +"801561"  + roomid);
        String encryptData = AESUtil.encrypt("{\"screen_width\":720,\"screen_height\":1280,\"sdk_type\":\"android\",\"sdk_version_name\":\"2.3.3\",\"sdk_version\":2017091501,\"xpoint\":113.908448,\"ypoint\":22.548354,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":" + roomid + ",\"is_vod\":0,\"sign\":\"" + sign + "\"}", "1400043672000000");
        FormBody.Builder builder = new FormBody.Builder();
         builder.add("requestData", encryptData);
         builder.add("i_type", "1");
         builder.add("ctl", "video");
         builder.add("act", "get_video2 ");
         builder.add("itype", "");
         builder.add("sdk_version_name", "2.3.3");

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
                .post(body)
                .header("Cookie", "client_ip=183.15.177.141; nick_name=801561; user_id=801561; user_pwd=b28f0b2523d1ab29b3ce77619d818a88; PHPSESSID2=v754gl75lib0656vjah09kl814; PHPSESSID=kh11kus1b7p8i8cb37k9bc3j66")
                .build();


        return request;
    }

}

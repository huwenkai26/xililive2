package com.example.xililive;


import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private static String url = "http://huangfeng.xzjvip.com/mapi/index.php";
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
                            roominfo roomBean = new Gson().fromJson(result, roominfo.class);

//                            System.out.println("222222222" + decryptData(baen.output));
                            if (roomBean.play_flv!=null&&!roomBean.play_flv.isEmpty()) {
                                indexBean.list.get(finalI).play_flv = (AesEncryptionUtil.decrypt(roomBean.play_flv));
                            }
                            if (roomBean.play_mp4!=null&&!roomBean.play_mp4.isEmpty()) {
                                indexBean.list.get(finalI).play_mp4 = (AesEncryptionUtil.decrypt(roomBean.play_mp4));
                            }
                            if (roomBean.play_rtmp!=null&&!roomBean.play_rtmp.isEmpty()) {
                                indexBean.list.get(finalI).play_rtmp = (AesEncryptionUtil.decrypt(roomBean.play_rtmp));
                            }
                            if (roomBean.play_url!=null&&!roomBean.play_url.isEmpty()) {
                                indexBean.list.get(finalI).play_url = (AesEncryptionUtil.decrypt(roomBean.play_url));
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
                    System.out.println(replaceAll);
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

        Request request = new Request.Builder().url(url)
                .post(body)
                .header("Cookie", "client_ip=61.141.64.27; nick_name=Oh.%E6%BC%8F; user_id=316393; user_pwd=47f8592143b3eeb584a9af1ea33ae09e; PHPSESSID2=jfn25svbrbb10tt6n7aigfg3a2; PHPSESSID=m49k6j09sbfcbdt65a994dd1i2")
                .build();


        return request;
    }

}

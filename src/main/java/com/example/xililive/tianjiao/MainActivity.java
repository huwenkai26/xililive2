package com.example.xililive.tianjiao;


import com.example.xililive.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivity {

    private StringBuilder stringBuilder;
    private static String url = "http://renminren.com/mapi/index.php";
    private static OkHttpClient client;
    private  CountDownLatch cdl = null;
    private static  int kk = 0;

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
        final TjIndexbean indexBean = new Gson().fromJson(respon, TjIndexbean.class);
        List<TjIndexbean.ListBean> list = indexBean.list;
        if (list.size() != 0) {
            final CountDownLatch cdl2 = new CountDownLatch(indexBean.list.size());
            int j= 0;
            for (int i = 0; i < indexBean.list.size(); i++) {

                if(j%5==0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final int finalI = i;
                //判断缓存有没有
//                    if(roomids.contains(indexBean.list.get(i).room_id)){
//                        cdl2.countDown();
//                        lastbean
//                    }
                if(indexBean.list.get(i).title.isEmpty()){
                    cdl2.countDown();

                    continue;
                }

                Call call2 = client.newCall(parseRequestParams(indexBean.list.get(i)));
                j++;
                call2.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call arg0, IOException arg1) {
                        // TODO Auto-generated method stub

                        cdl2.countDown();
                    }

                    @Override
                    public void onResponse(Call arg0, Response response) throws IOException {

                        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
                        Gson gson = gsonBuilder.create();
                        String result = response.body().string();
                        System.out.println(result);
                        if(result.contains("Blocked")){


                        }else if(result.contains("output")){

                            BaseEncryptModel baseEncryptModel = gson.fromJson(result, BaseEncryptModel.class);
                            String decrypt = AESUtil.decrypt(baseEncryptModel.getOutput(), "1400040126000000");

                            if(decrypt.contains("status")){
                            kk++;
                                System.out.println(decrypt);
                                TjRoominfo roomBean =gson.fromJson(decrypt, TjRoominfo.class);
                                indexBean.list.get(finalI).play_url = roomBean.preview_play_url;
                                System.out.println(kk+  indexBean.list.get(finalI).play_url );
                                System.out.println(kk+  roomBean.preview_play_url );
                            }else{
                                kk++;
                                System.out.println(decrypt);
                                TjRoominfo roomBean =gson.fromJson(decrypt, TjRoominfo.class);
                                indexBean.list.get(finalI).play_url = roomBean.play_url;
                                System.out.println(kk+  indexBean.list.get(finalI).play_url );
                            }

                        }



//key 1400043672000000




                        cdl2.countDown();

                    }

                });


            }
            try {
                cdl2.await();
                stringBuilder.append(new Gson().toJson(indexBean));
//                    writerTolocal(stringBuilder.toString());
//                    byte[] utf_8 = .getBytes("UTF-8");
//                    String json= new String(utf_8, "UTF-8");
//                    String gbkStr = new String(stringBuilder.toString().getBytes("ISO8859-1"), "GBK");
                String replaceAll = stringBuilder.toString().replaceAll("\\\\u003d", "=");
                replaceAll = replaceAll.toString().replaceAll("\\\\u0026", "&");
                System.out.println(replaceAll);

                return replaceAll;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    private  void writerTolocal(String string) {
        FileOutputStream writerStream;
        try {
            writerStream = new FileOutputStream("json.txt");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(string);
            writer.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private  String readerTolocal() {
        FileInputStream readerStream;
        StringBuilder result = new StringBuilder();

        try {

            readerStream = new FileInputStream("json.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(readerStream, "UTF-8"));
            String s = null;
            while((s = reader.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }

            reader.close();
            return result.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    return "";
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
        String sign =  MD5Util.MD5("1400040126" +"192019"  + roomid);
        String encryptData = AESUtil.encrypt("{\"screen_width\":720,\"screen_height\":1280,\"sdk_type\":\"android\",\"sdk_version_name\":2017052201,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":"+roomid+",\"is_vod\":0,\"type\":0,\"has_scroll\":1,\"sex\":0,\"cate_id\":0,\"sign\":\""+sign+"\"}","1400040126000000");
        FormBody.Builder builder = new FormBody.Builder();
         builder.add("requestData", encryptData);
         builder.add("i_type", "1");
         builder.add("ctl", "video");
         builder.add("act", "get_video2 ");
         builder.add("itype", "");


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
                .addHeader("Cookie", "client_ip=113.119.11.138; nick_name=192019; user_id=192019; user_pwd=3f4b40c5fa69076d255af7a35e3da4f4; PHPSESSID2=3gjc871qg61pmrooqg6eli7bi7; yd_cookie=acef32c4-f52d-49127a1345029344c3980b99653738a1f7f8; PHPSESSID=2tvdtqn74cm2lru5uipfbmpre2; yunsuo_session_verify=44cf2b7658178a602ec097c7e6699d7c")
                .build();


        return request;
    }

}

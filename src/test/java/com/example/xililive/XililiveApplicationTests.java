package com.example.xililive;

import com.example.xililive.meme.HttpUtils;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@SpringBootTest(classes=XililiveApplication.class)
@RunWith(SpringRunner.class)

public class XililiveApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public void testShow() throws UnsupportedEncodingException {
//
//				.replaceAll("\\\\",""),
//				"1400045299000000");
	int roomid = 78191;
		String sign =  MD5Util.MD5("1400045740" +"200025"  + roomid);
		String re =  AESUtil.encrypt(
				"{\"screen_width\":720,\"screen_height\":1280,\"sdk_type\":\"android\",\"sdk_version_name\":\"2.5.2\",\"sdk_version\":2017102101,\"xpoint\":113.913439,\"ypoint\":22.548069,\"ctl\":\"video\",\"act\":\"get_video2\",\"room_id\":"+roomid+",\"is_vod\":0,\"sign\":\""+sign+"\"}","1400045740000000");

		System.out.println(URLEncoder.encode(re));
		String ss = "requestData=" + URLEncoder.encode(re) + "&i_type=1&ctl=video&act=get_video2&itype=&sdk_version_name=2.5.2";

		String decrypt = HttpUtils.sendPost("http://niukou.ysgfgj.com/mapi/index.php",ss,false);
		System.out.println(decrypt);
//		String sign =  MD5Util.MD5("1400045740" +"200025"  + 77882);
//		System.out.println(sign);
	}
}

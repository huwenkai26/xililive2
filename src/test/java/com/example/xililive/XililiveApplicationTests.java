package com.example.xililive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest(classes=XililiveApplication.class)
@RunWith(SpringRunner.class)

public class XililiveApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public void testShow() {
		String decrypt = AESUtil.decrypt("LD1PBy8p45o4Lc3sIXUg3UviEFEoMWCdMyKyURvQtUe0a6X0Xs8eTde2fmOEBVtxge2mRXaQGgiZRX/lCBCLOw6VqkYXWXkttL3l6BvSDxIiecMUuS/9Y6Lvav8tdGMc6/n8Zj2VPsp9YKEZA6vwn+FPSGnG3BCnBCHTyr8NOmzAxoY42BsSfhhyCQtmNerZL7626CfY22A6QjQ6mz5qbh7vA0wk1yZFl1NgXIpvPlNZsRtAwpdMuOc48Xm9YDfe1HPml+Xt52CmIlIUXGQZPIzodtk1xvBkh/3MtFBqjrepcY2NnrpSZeseANfTswrIDpKJtFpI8GfMA/BHTYTWE7kuialdfxP9Szl668z/awX6TZafPXu7DptzhNIiqWr1uqqieLronOPpEDfMj6c8YKE09AUjN0thrEukCRPFrcIgYAH4FFiSAV9DD5ia3rdlap/Howh1nNI7azGKxr1C2+M+1JibOejcSKC8h23iXWO+OeqTHa+6FQ1Zy3as9C8xVXpaYtjtrCHFDVXkHLA21E+hOjqWVxX+XP4ASZYsPBc=", "1400043672000000");
		System.out.println(decrypt);
	}
}

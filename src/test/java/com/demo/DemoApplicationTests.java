package com.demo;

import com.alibaba.fastjson.JSONObject;
import com.demo.entity.AccessToken;
import com.demo.utils.WeiXinUtil;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        try {
            AccessToken accessToken = WeiXinUtil.getAccessToken();
            System.out.println(accessToken.getToken());
            System.out.println(accessToken.getExpires_in());
            int result = WeiXinUtil.createMenu(accessToken.getToken(), JSONObject.toJSONString(WeiXinUtil.iniMenu()).toString());
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.demo.entity.AccessToken;
import com.demo.utils.WeiXinUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String token = "";
        try {
            AccessToken accessToken = WeiXinUtil.getAccessToken();

            if (accessToken != null && null != accessToken.getToken()){
                token = accessToken.getToken();
            }else {
                System.out.println("获取token失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int result = WeiXinUtil.createMenu(token, JSONObject.toJSONString(WeiXinUtil.iniMenu()).toString());
            if (result == 0){
                System.out.println("创建菜单成功");
            }else {
                System.out.println("创建菜单失败：" + result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

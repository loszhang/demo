package com.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.demo.entity.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WeiXinUtil {

    /*private static final String APPID = "wxa35d40b1636c4fe8";
    private static final String APPSECRET = "c7c6b6bcbd604841e2306482212b7527";*/

    private static final String APPID = "wxb63432a771e3538f";
    private static final String APPSECRET = "5b0ec45be57d9299028f04b24ea3d489";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String CREATE_MENU_URL ="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
     * 发送Get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetStr(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response =httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null){
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject;
        }
        return null;
    }

    /**
     * 发送Post请求
     *
     * @param url
     * @param outStr
     * @return
     * @throws IOException
     */
    public static JSONObject doPostStr(String url, String outStr) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(outStr, "UTF-8" ));
        HttpResponse response =httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null){
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject;
        }
        return null;
    }

    /**
     * 获取token
     * @return
     * @throws IOException
     */
    public static AccessToken getAccessToken() throws IOException {
        AccessToken accessToken = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID ).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if (jsonObject != null){
            if (!"".equals(jsonObject.getString("access_token")) || null != jsonObject.getString("access_token")){
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpires_in(jsonObject.getInteger("expires_in"));
            }else{
                System.out.println("获取token失败，错误：" + jsonObject.getString("errcode") +", " + jsonObject.getString("errmsg"));
            }

        }
        return accessToken;
    }

    public static Menu iniMenu(){
        ClickButton btn1 = new ClickButton();
        btn1.setName("主菜单");
        btn1.setType(MessageUtil.MESSAGE_CLICK);
        btn1.setKey("11");

        ViewButton btn2 = new ViewButton();
        btn2.setName("副菜单");
        btn2.setType("view");
        btn2.setUrl("http://www.baidu.com");

        ClickButton btn3 = new ClickButton();
        btn3.setName("扫码");
        btn3.setType("scancode_push");
        btn3.setKey("31");

        ClickButton btn4 = new ClickButton();
        btn4.setName("地理位置");
        btn4.setType("location_select");
        btn4.setKey("32");

        Button btn = new Button();
        btn.setName("菜单");
        btn.setSub_button(new Button[]{btn3, btn4});

        Menu menu = new Menu();
        menu.setButton(new Button[]{btn1, btn2, btn});

        return menu;
    }

    public static int createMenu(String token, String menu) throws IOException {
        int result =0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, menu);
        if (jsonObject != null){
            result = jsonObject.getIntValue("errcode");
        }

        return result;

    }
}

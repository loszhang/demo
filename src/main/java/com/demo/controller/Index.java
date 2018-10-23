package com.demo.controller;

import com.demo.entity.TextMessage;
import com.demo.utils.MessageUtil;
import io.swagger.annotations.*;
import org.dom4j.DocumentException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@Api(tags = "微信公众号验证")
@Controller
public class Index {

    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    @ApiOperation(value = "验证token", notes = "验证token")
    @ApiImplicitParams({@ApiImplicitParam(name = "signature", required = true,  dataType = "String"),
        @ApiImplicitParam(name = "timestamp", required = true, dataType = "String"),
            @ApiImplicitParam(name = "nonce",required = true, dataType = "String"),
            @ApiImplicitParam(name = "echostr",required = true, dataType = "String")})
    @RequestMapping("/")
    @ResponseBody
    public String index(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ParseException {
        System.out.println("signature: " + signature);
        System.out.println("timestamp: " + timestamp);
        System.out.println("nonce: " + nonce);
        System.out.println("echostr: " + echostr);
        if (null != echostr){
            return echostr;
        }
        System.out.println("path: " + request.getServletPath());


        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String string = null;
        try {
            //将request请求，传到Message工具类的转换方法中，返回接收到的Map对象
            Map<String, String> map = MessageUtil.xmlToMap(request);
            //从集合中，获取XML各个节点的内容
            String ToUserName = map.get("ToUserName");
            String FromUserName = map.get("FromUserName");
            String CreateTime = map.get("CreateTime");
            CreateTime = MessageUtil.formatTime(CreateTime);
            String MsgType = map.get("MsgType");
            String Content = map.get("Content");
            String MsgId = map.get("MsgId");
            if (MsgType.equals(MessageUtil.MESSAGE_TEXT)) {//判断消息类型是否是文本消息(text)
                if (Content.equals("1")){//获取简介
                        string = MessageUtil.initNewsMessage(ToUserName, FromUserName);
                }else {
                    TextMessage message = new TextMessage();
                    message.setFromUserName(ToUserName);//原来【接收消息用户】变为回复时【发送消息用户】
                    message.setToUserName(FromUserName);
                    message.setMsgType("text");
                    message.setCreateTime(new DateTime().toString(DATETIMEFORMAT));//创建当前时间为消息时间
                    message.setContent("您好，" + FromUserName + "\n我是：" + ToUserName
                            + "\n您发送的消息类型为：" + MsgType + "\n您发送的时间为" + CreateTime
                            + "\n我回复的时间为：" + message.getCreateTime() + "\n您发送的内容是:" + Content);
                    string = MessageUtil.objectToXml(message); //调用Message工具类，将对象转为XML字符串
                }
            }else if (MsgType.equals(MessageUtil.MESSAGE_EVENT)){
                String eventType = map.get("Event");
                if (eventType.equals(MessageUtil.MESSAGE_SUBSCRIBE)){
                    string = MessageUtil.intiSubText(ToUserName, FromUserName,MessageUtil.subscribeReplyText());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        out.print(string); //返回转换后的XML字符串
        out.close();

        return echostr;
    }

}

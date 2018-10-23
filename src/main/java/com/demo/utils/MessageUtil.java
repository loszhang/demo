package com.demo.utils;

import com.demo.entity.News;
import com.demo.entity.NewsMessage;
import com.demo.entity.TextMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageUtil {
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "click";
    public static final String MESSAGE_VIEW = "view";
    public static final String MESSAGE_SCAN = "scan";
    public static final String MESSAGE_NEWS ="news";

    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream inputStream = request.getInputStream();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> list = root.elements();
        for (Element element : list) {
            map.put(element.getName(), element.getText());
            System.out.println(element.getName() + ": " + element.getText());
        }
        inputStream.close();

        return map;
    }

    /**
     * 将文本消息对象转化成XML格式
     *
     * @param message 文本消息对象
     * @return 返回转换后的XML格式
     */

    public static String objectToXml(TextMessage message) {
        XStream xs = new XStream();
        //由于转换后xml根节点默认为class类，需转化为<xml>
        xs.alias("xml", message.getClass());
        return xs.toXML(message);
    }


    /**
     * 将图文消息对象转成XML
     * @param
     * @return
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xstream = new XStream();
        //将xml的根节点替换成<xml>  默认为NewsMessage的包名
        xstream.alias("xml", newsMessage.getClass());
        //同理，将每条图文消息News类的报名，替换为<item>标签
        xstream.alias("item", new News().getClass());
        return xstream.toXML(newsMessage);
    }


    /**
     * 初始化图文消息
     */
    public static String initNewsMessage(String toUSerName,String fromUserName){
        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        //组建一条图文↓ ↓ ↓
        News newsItem = new News();
        newsItem.setTitle("欢迎来到loszhang公众号");
        newsItem.setDescription("loszhang公众号，为您提供更多实用小工具！");
        newsItem.setPicUrl("http://47.104.238.162/images/index/polarbear.jpg");
        newsItem.setUrl("www.jredu100.com");
        newsList.add(newsItem);

        //组装图文消息相关信息
        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUSerName);
        newsMessage.setCreateTime(new Date().getTime()/1000 + "");
        newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        //调用newsMessageToXml将图文消息转化为XML结构并返回
        return MessageUtil.newsMessageToXml(newsMessage);
    }

    /**
     * 将公众号返回的CreateTime转换正常时间
     * @param time
     * @return
     */
    public static String formatTime(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long msgCreateTime = Long.parseLong(time) * 1000L;
        Date date = new Date(msgCreateTime);
        return simpleDateFormat.format(date);
    }

    /**
     * 初始化回复消息
     * @param toUSerName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String intiSubText(String toUSerName,String fromUserName,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUSerName);
        text.setToUserName(fromUserName);
        text.setMsgType(MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime()+"");
        text.setContent(content);
        return MessageUtil.objectToXml(text);
    }

    /**
     * 关注回复内容
     * @return
     */
    public static String subscribeReplyText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎光临loszhang公众号，请选择序号：\n");
        sb.append("1、简介\n");
        sb.append("2、功能\n");
        sb.append("回复 ？ 调出主菜单\n");
        return sb.toString();
    }

}

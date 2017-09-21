package com.gether.research.wsclient.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
 



 
/**
 *  
 * @author BYSocket
 * Jaxb2.0 处理Xml与Object转换
 *
 */
public class JaxbObjectAndXmlUtil
{ 
       
    /**
     * @param xmlStr 字符串
     * @param c 对象Class类型
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(String xmlStr,Class<T> c)
    { 
        try
        { 
            JAXBContext context = JAXBContext.newInstance(c); 
            Unmarshaller unmarshaller = context.createUnmarshaller(); 
             
            T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr)); 
             
            return t; 
             
        } catch (JAXBException e) {  e.printStackTrace();  return null; } 
         
    } 
       
    /**
     * @param object 对象
     * @return 返回xmlStr
     */
    public static String object2Xml(Object object)
    { 
        try
        {   
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass()); 
            Marshaller  marshal = context.createMarshaller();
             
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化输出 
            marshal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式,默认为utf-8 
            marshal.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息 
            marshal.setProperty("jaxb.encoding", "utf-8"); 
            marshal.marshal(object,writer);
             
            return new String(writer.getBuffer());
             
        } catch (Exception e) { e.printStackTrace(); return null;}    
         
    } 
     
   /* public static void main(String[] args)
    {
         *//** 构造测试报文头对象 *//*
        HeadBean head = new HeadBean();
        head.setCode("test123");
        head.setSid("000000");
        head.setServiceId("teat0123456");
        head.setTimestamp(System.currentTimeMillis());
        
        BodyBean body = new BodyBean();
        body.setAppacctid("smb123456789");
        //body.setMainacctid("0123456");
       // body.setRsp("00000");
        body.setToken("9876543210");
        
        UserRspBean rsp = new UserRspBean();
        rsp.setBody(body);
        rsp.setHead(head);
        
        String xmlStr = JaxbObjectAndXmlUtil.object2Xml(rsp);//构造报文 XML 格式的字符串
        System.out.println("对象转xml报文： \n"+xmlStr);
         
        UserRspBean result = JaxbObjectAndXmlUtil.xml2Object(xmlStr, UserRspBean.class);
        System.out.println("报文转xml转： \n"+JSON.toJSONString(result));
    }*/
}

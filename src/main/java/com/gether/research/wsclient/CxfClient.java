//package com.gether.research.wsclient;
//
//import com.arcsoft.closeli.smb.util.PropertyUtil;
//import com.gether.research.wsclient.pojo.BodyBean;
//import com.gether.research.wsclient.pojo.HeadBean;
//import com.gether.research.wsclient.pojo.UserReqBean;
//import com.gether.research.wsclient.pojo.UserRspBean;
//import com.gether.research.wsclient.util.JaxbObjectAndXmlUtil;
//import org.apache.cxf.endpoint.Client;
//import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
//import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//public class CxfClient {
//	private final static Logger log = LoggerFactory.getLogger(CxfClient.class);
//	private static final String webserviceFor4A= PropertyUtil.getConfig("webserviceFor4A")+"?wsdl";
//
//
//
//	public static String CheckAiuapTokenSoap(String serviceId,String appAccId,String token) {
//		HeadBean head = new HeadBean();
//		String timestamp = new SimpleDateFormat("YYYYMMDDHHmmss").format(new Date());
//		head.setCODE(timestamp);
//		head.setSID(timestamp);
//		head.setSERVICEID(serviceId);
//		head.setTIMESTAMP(timestamp);
//
//		BodyBean body = new BodyBean();
//		body.setAPPACCTID(appAccId);
//		body.setTOKEN(token);
//
//		UserReqBean rsp = new UserReqBean();
//		rsp.setBODY(body);
//		rsp.setHEAD(head);
//
//		String requestXml = JaxbObjectAndXmlUtil.object2Xml(rsp);// 构造报文 XML 格式的字符串
//		log.info("对象转xml报文： \n" + requestXml);
//		return JaxWsDynamicClient(requestXml);
//		//return DynamicClient(requestXml);
//	}
//	public static String JaxWsDynamicClient(String requestXml) {
//		log.info("------------------JaxWsDynamicClient--------------------");
//		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
//		try {
//			log.info("ws server {}",webserviceFor4A);
//			Client client = factory.createClient(webserviceFor4A);
//			Object[] results = client.invoke("RequestInfo", requestXml);
//			String result = (String) results[0];
//
//			log.info("获取到的返回报文：\n" + result);
//			UserRspBean resultBean = JaxbObjectAndXmlUtil.xml2Object(result, UserRspBean.class);
//			String account = resultBean.getBODY().getAPPACCTID();
//			log.info("返回的子帐号：\n" + account);
//			return account;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static String DynamicClient(String requestXml){
//		log.info("------------------DynamicClient--------------------");
//		DynamicClientFactory factory = DynamicClientFactory.newInstance();
//	    try {
//			log.info("ws server {}",webserviceFor4A);
//	    	Client client = factory.createClient(webserviceFor4A);
//			Object[] results = client.invoke("RequestInfo", requestXml);
//			String result = (String) results[0];
//
//			log.info("获取到的返回报文：\n" + result);
//			UserRspBean resultBean = JaxbObjectAndXmlUtil.xml2Object(result, UserRspBean.class);
//			String account = resultBean.getBODY().getAPPACCTID();
//			log.info("返回的子帐号：\n" + resultBean.getBODY().getAPPACCTID());
//			return account;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	    return null;
//    }
//}

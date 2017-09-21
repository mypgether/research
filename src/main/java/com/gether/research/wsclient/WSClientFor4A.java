/*
 * 创建日期 Mar 6, 2015
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.gether.research.wsclient;

import com.gether.research.wsclient.pojo.BodyBean;
import com.gether.research.wsclient.pojo.HeadBean;
import com.gether.research.wsclient.pojo.UserReqBean;
import com.gether.research.wsclient.pojo.UserRspBean;
import com.gether.research.wsclient.util.JaxbObjectAndXmlUtil;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author Ar.M(Yu)
 *
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class WSClientFor4A {
	private final static Logger log = LoggerFactory.getLogger(WSClientFor4A.class);
	private static final String webserviceFor4A = "";
	//PropertyUtil.getConfig("webserviceFor4A");


	// ServicesURL:ServicesURL
	// SerVicesName:服务名称
	// ReqName：请求参数名称
	// ReqValue：请求参数值
	// RspName：响应参数名
	private static String CallCommonServices(String ServicesURL, String SerVicesName, String ReqName, String ReqValue,
											 String RspName) throws Exception {
		try {
			Map output;
			Call call = (Call) new Service().createCall();

			call.setTimeout(180 * 1000);// 3分钟

			String _ServicesURL = ServicesURL;
			call.setTargetEndpointAddress(new URL(_ServicesURL));
			call.setOperationName(new QName(SerVicesName, SerVicesName));
			call.addParameter(ReqName, org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			call.setEncodingStyle("UTF-8");

			Object responseWS_role = call.invoke(new Object[]{ReqValue});
			log.info("=====Receiving response=====: " + (String) responseWS_role);
			output = call.getOutputParams();
			String RspValue;
			try {
				RspValue = (String) output.get(new QName("", RspName));
			} catch (Exception _exception) {
				RspValue = (String) org.apache.axis.utils.JavaUtils.convert(output.get(new QName("", RspName)), String.class);
			}

			if (RspValue == null)
				RspValue = (String) responseWS_role;

			log.info("=====RspValue=====: " + RspValue);
			return RspValue;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public static String CheckAiuapTokenSoap(String serviceId, String appAccId, String token) throws Exception {
		HeadBean head = new HeadBean();
		String timestamp = new SimpleDateFormat("YYYYMMDDHHmmss").format(new Date());
		head.setCODE(timestamp);
		head.setSID(timestamp);
		head.setSERVICEID(serviceId);
		head.setTIMESTAMP(timestamp);

		BodyBean body = new BodyBean();
		body.setAPPACCTID(appAccId);
		body.setTOKEN(token);

		UserReqBean req = new UserReqBean();
		req.setBODY(body);
		req.setHEAD(head);

		String requestXml = JaxbObjectAndXmlUtil.object2Xml(req);// 构造报文 XML
		// 格式的字符串
		log.info("对象转xml报文： \n" + requestXml);

		String responseXml = CallCommonServices(webserviceFor4A, "CheckAiuapTokenSoap", "RequestInfo", requestXml, "ResponseInfo");
		log.info("=====responseXml=====: " + responseXml);
		UserRspBean resp = JaxbObjectAndXmlUtil.xml2Object(responseXml, UserRspBean.class);
		String rsp = resp.getBODY().getRSP();
		if (StringUtils.equals("0", rsp)) {
			return resp.getBODY().getAPPACCTID();
		}
		return null;
	}
}
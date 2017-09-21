/*
 * 创建日期 Mar 6, 2015
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.gether.research.wsclient;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;



/**
 * @author Ar.M(Yu)
 *
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class testSmpPermission {

		public static void main(String[] args) throws Exception, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
			String ServicesURL="http://10.191.143.74:9080/uac20/services/CheckAiuapTokenSoap";
			//http://10.189.96.222:8082/ngcrm/aaaa/mutilWorkerView.action?appAcctId=2000498023&token=32|107|34|61|122|57|70|-51|122|4|-50|-123|-126|-112|-106|-80|-53|-54|76|-113|-116|-3|124|-10|-97|22|-49|50|-1|-5|86|-64|89&flag=1&ipAddress=10.191.130.37&macAddress=00-1E-EC-B6-70-B9,50-50-54-50-30-30,33-50-6F-45-30-30,3A-F3-20-52-41-53&cpuSerial=BFF00016A070253154,BFF00016A07F540033,BFF00016A07C6B90B7,BFF00016A074050033&hostName=PC-20150727SJZU&hostAccount=Administrator&mac=00-1E-EC-B6-70-B9,50-50-54-50-30-30,33-50-6F-45-30-30,3A-F3-20-52-41-53&ip=10.191.130.37&client=PC-20150727SJZU&username=Administrator&businessAddress=10.191.130.37
			String SerVicesName="CheckAiuapTokenSoap";
			String ReqName="RequestInfo";
			String ReqValue="<?xml version='1.0' encoding='UTF-8'?><USERREQ>" +
					"<HEAD><CODE></CODE><SID></SID><TIMESTAMP>20170216143155</TIMESTAMP>" +
					"<SERVICEID>CQNGBOSS</SERVICEID></HEAD>" +
					"<BODY><APPACCTID>2000498023</APPACCTID>" +
					"<TOKEN>32|107|34|61|122|57|70|-51|122|4|-50|-123|-126|-112|-106|-80|-53|-54|76|-113|-116|-3|124|-10|-97|22|-49|50|-1|-5|86|-64|89</TOKEN>" +
					"</BODY></USERREQ>";
			String RspName="ResponseInfo";
			System.out.println(ReqValue);
		// String s=WebServicesClientUtil.CallCommonServices(ServicesURL, SerVicesName, ReqName, ReqValue, RspName);
		  String s=CallCommonServices(ServicesURL, SerVicesName, ReqName, ReqValue, RspName);

			System.out.println(s);
		}



		// ServicesURL:ServicesURL
		// SerVicesName:服务名称
		// ReqName：请求参数名称
		// ReqValue：请求参数值
		// RspName：响应参数名
		public static String CallCommonServices(String ServicesURL,
				String SerVicesName, String ReqName, String ReqValue, String RspName)
				throws Exception {
			try {
				Map output;
				Call call = (Call) new Service().createCall();

				call.setTimeout(180 * 1000);// 3分钟

				String _ServicesURL = ServicesURL;
				call.setTargetEndpointAddress(new URL(_ServicesURL));
				call.setOperationName(new QName(SerVicesName, SerVicesName));
				call.addParameter(ReqName, org.apache.axis.Constants.XSD_STRING,
						javax.xml.rpc.ParameterMode.IN);
				call.setReturnType(org.apache.axis.Constants.XSD_STRING);
				call.setEncodingStyle("UTF-8");

				Object responseWS_role = call.invoke(new Object[] { ReqValue });
				// Debug.log("=====Receiving response=====: " + (String)
				// responseWS_role);
				output = call.getOutputParams();
				String RspValue;
				try {
					RspValue = (String) output.get(new QName("", RspName));
				} catch (Exception _exception) {
					RspValue = (String) org.apache.axis.utils.JavaUtils.convert(
							output.get(new QName("", RspName)),
							String.class);
				}

				if (RspValue == null)
					RspValue = (String) responseWS_role;

				// Debug.log("=====RspValue=====: " + RspValue);
				return RspValue;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
}






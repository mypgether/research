package com.gether.research.wsclient.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "BODY")
public class BodyBean implements Serializable {

	private String RSP;
	private String MAINACCTID;
	private String APPACCTID;
	private String TOKEN;
	public String getRSP() {
		return RSP;
	}
	public void setRSP(String rSP) {
		RSP = rSP;
	}
	public String getMAINACCTID() {
		return MAINACCTID;
	}
	public void setMAINACCTID(String mAINACCTID) {
		MAINACCTID = mAINACCTID;
	}
	public String getAPPACCTID() {
		return APPACCTID;
	}
	public void setAPPACCTID(String aPPACCTID) {
		APPACCTID = aPPACCTID;
	}
	public String getTOKEN() {
		return TOKEN;
	}
	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}


}

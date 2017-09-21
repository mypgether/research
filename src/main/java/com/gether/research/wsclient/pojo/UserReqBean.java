package com.gether.research.wsclient.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "USERREQ")
public class UserReqBean implements Serializable {

	private static final long serialVersionUID = -538838957843824254L;
	private HeadBean HEAD;
	private BodyBean BODY;

	public HeadBean getHEAD() {
		return HEAD;
	}

	@XmlElement(name = "HEAD")
	public void setHEAD(HeadBean head) {
		this.HEAD = head;
	}

	public BodyBean getBODY() {
		return BODY;
	}

	@XmlElement(name = "BODY")
	public void setBODY(BodyBean body) {
		this.BODY = body;
	}
}

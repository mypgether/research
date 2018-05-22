package com.gether.research.test.kafka.streams.case1.bean.statistic.dto;

import com.gether.research.test.kafka.streams.case1.bean.statistic.KAppAction;
import com.gether.research.test.kafka.streams.case1.bean.statistic.KUser;

/**
 * Created by myp on 2017/10/10.
 */
public class KAppActionUser {

    private String userno;
    private String deviceno;
    private long time;
    private int flowtype;
    private long datasize;
    private Double runlength;

    private String userCountry;
    private String userProvince;
    private String userCity;

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getFlowtype() {
        return flowtype;
    }

    public void setFlowtype(int flowtype) {
        this.flowtype = flowtype;
    }

    public long getDatasize() {
        return datasize;
    }

    public void setDatasize(long datasize) {
        this.datasize = datasize;
    }

    public Double getRunlength() {
        return runlength;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public void setRunlength(Double runlength) {
        this.runlength = runlength;
    }

    public static KAppActionUser fromAppAction(KAppAction appAction) {
        KAppActionUser appActionUser = new KAppActionUser();
        if (appAction == null) {
            return appActionUser;
        }
        appActionUser.setUserno(appAction.getUserno());
        appActionUser.setDeviceno(appAction.getDeviceno());
        appActionUser.setTime(appAction.getTime());
        appActionUser.setFlowtype(appAction.getFlowtype());
        appActionUser.setDatasize(appAction.getDatasize());
        appActionUser.setRunlength(appAction.getRunlength());
        return appActionUser;
    }

    public static KAppActionUser fromAppActionUser(KAppAction appAction, KUser user) {
        KAppActionUser appActionUser = fromAppAction(appAction);
        if (user == null) {
            return appActionUser;
        }
        appActionUser.setUserCountry(user.getCountry());
        appActionUser.setUserCity(user.getCity());
        appActionUser.setUserProvince(user.getProvince());
        return appActionUser;
    }
}
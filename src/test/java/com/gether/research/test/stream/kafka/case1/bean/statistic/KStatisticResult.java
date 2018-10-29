package com.gether.research.test.stream.kafka.case1.bean.statistic;

/**
 * Created by myp on 2017/10/10.
 */
public class KStatisticResult {

    private String userno;
    private String userCountry;
    private String userProvince;
    private String userCity;
    private int deviceCount;
    private String serviceTime;
    private String dvrdays;

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
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

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getDvrdays() {
        return dvrdays;
    }

    public void setDvrdays(String dvrdays) {
        this.dvrdays = dvrdays;
    }
}
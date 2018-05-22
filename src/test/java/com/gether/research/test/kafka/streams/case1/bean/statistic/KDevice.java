package com.gether.research.test.kafka.streams.case1.bean.statistic;

/**
 * Created by myp on 2017/10/9.
 */
public class KDevice {

    private String deviceno;
    private String userno;
    private String serviceTime;
    private String dvrdays;

    public KDevice() {
    }

    public KDevice(String deviceno, String userno, String serviceTime, String dvrdays) {
        this.deviceno = deviceno;
        this.userno = userno;
        this.serviceTime = serviceTime;
        this.dvrdays = dvrdays;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
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
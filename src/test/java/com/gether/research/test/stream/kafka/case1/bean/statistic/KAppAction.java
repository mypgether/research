package com.gether.research.test.stream.kafka.case1.bean.statistic;

/**
 * Created by myp on 2017/10/9.
 */
public class KAppAction {

    private String userno;
    private String deviceno;
    private long time;

    private int flowtype;
    private long datasize;
    private Double runlength;

    public KAppAction() {
    }

    public KAppAction(String userno, String deviceno, long time, int flowtype, long datasize, Double runlength) {
        this.userno = userno;
        this.deviceno = deviceno;
        this.time = time;
        this.flowtype = flowtype;
        this.datasize = datasize;
        this.runlength = runlength;
    }

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

    public void setRunlength(Double runlength) {
        this.runlength = runlength;
    }
}
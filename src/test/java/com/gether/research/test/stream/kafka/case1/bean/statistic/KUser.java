package com.gether.research.test.stream.kafka.case1.bean.statistic;

/**
 * Created by myp on 2017/10/9.
 */
public class KUser {

    private String userno;
    private String country;
    private String province;
    private String city;

    public KUser() {
    }

    public KUser(String userno, String country, String province, String city) {
        this.userno = userno;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

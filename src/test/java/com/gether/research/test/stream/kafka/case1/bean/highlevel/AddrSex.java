package com.gether.research.test.stream.kafka.case1.bean.highlevel;

/**
 * Created by myp on 2017/8/22.
 */
public class AddrSex {
    private String address;
    private String sex;

    public AddrSex() {
    }

    public AddrSex(String address, String sex) {
        this.address = address;
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "AddrSex{" +
                "address='" + address + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}

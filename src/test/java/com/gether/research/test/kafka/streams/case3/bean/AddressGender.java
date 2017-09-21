package com.gether.research.test.kafka.streams.case3.bean;

/**
 * Created by myp on 2017/8/31.
 */
public class AddressGender {
    private String address;
    private String gender;

    public AddressGender() {
    }

    public AddressGender(String address, String gender) {
        this.address = address;
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

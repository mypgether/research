package com.gether.research.test.stream.kafka.case1.bean.statistic;

/**
 * Created by myp on 2017/10/9.
 */
public class KDataWrapper {

    private KAppAction kAppAction;
    private KDevice kDevice;
    private KUser kUser;

    public KDataWrapper() {
    }

    public KDataWrapper(KAppAction kAppAction, KDevice kDevice, KUser kUser) {
        this.kAppAction = kAppAction;
        this.kDevice = kDevice;
        this.kUser = kUser;
    }

    public KAppAction getkAppAction() {
        return kAppAction;
    }

    public void setkAppAction(KAppAction kAppAction) {
        this.kAppAction = kAppAction;
    }

    public KDevice getkDevice() {
        return kDevice;
    }

    public void setkDevice(KDevice kDevice) {
        this.kDevice = kDevice;
    }

    public KUser getkUser() {
        return kUser;
    }

    public void setkUser(KUser kUser) {
        this.kUser = kUser;
    }
}

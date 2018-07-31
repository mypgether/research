package com.gether.research.test.protobuff;

import com.gether.research.protobuff.AppActionLog;
import com.gether.research.protobuff.UserProto;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Arrays;
import org.junit.Test;

/**
 * Created by myp on 2017/8/13.
 */
public class ProtocolBufferTest {

  @Test
  public void user() {
    UserProto.User u = UserProto.User.newBuilder().setId(1).setName("gether").setRemark("remark")
        .setSex(UserProto.User.SEX.MALE).build();
    System.out.println(u.toString());
    System.out.println(u.toByteArray());
    System.out.println(Arrays.toString(u.toByteArray()));

    try {
      UserProto.User newUser = UserProto.User.parseFrom(u.toByteArray());
      System.out.println(newUser.toString());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void repeateUser() {
    UserProto.User u1 = UserProto.User.newBuilder().setId(1).setName("gether").setRemark("remark")
        .setSex(UserProto.User.SEX.MALE).build();
    UserProto.User u2 = UserProto.User.newBuilder().setId(2).setName("tom").setRemark("remark")
        .setSex(UserProto.User.SEX.FEMALE).addFriends(u1).build();
    UserProto.User u3 = UserProto.User.newBuilder().setId(3).setName("jack").setRemark("remark")
        .setSex(UserProto.User.SEX.MALE).addFriends(u1).addFriends(u2).build();

    u1 = UserProto.User.newBuilder(u1).addFriends(u2).addFriends(u3).build();
    System.out.println(u1.toString());
    System.out.println(u2.toString());
    System.out.println(u3.toString());

    u1.getFriendsList().forEach((u) -> System.out.println(u.toString()));
  }

  @Test
  public void appActionLog() {
    AppActionLog.AppLog.Builder builder = AppActionLog.AppLog.newBuilder();
    builder.setRequestid("requestId");
    builder.setDeviceno("02910192EF");
    builder.addAppactions(AppActionLog.AppAction.newBuilder()
        .setActiontype(AppActionLog.AppAction.ACTIONTYPE.APP_OPEN).addProperties(
            AppActionLog.MapFieldEntry.newBuilder().setValue("0901").setKey("deviceno"))
        .addProperties(AppActionLog.MapFieldEntry.newBuilder().setValue("1000").setKey("length"))
        .addProperties(
            AppActionLog.MapFieldEntry.newBuilder().setValue("1024").setKey("flowsize")));
    builder.setDeviceinfo(
        AppActionLog.DeviceInfo.newBuilder().setDevicetype("iPhone 6S").setVersion("1.0.0.3.9")
            .setImei("110101010101").setOstype("ios").setOsversion("9.1"));
    builder.setNetwork(AppActionLog.AppLog.NETTYPE.WIFI);
    builder.setTime(System.currentTimeMillis());
    AppActionLog.AppLog appLog = builder.build();
    System.out.println(appLog.toString());
  }
}
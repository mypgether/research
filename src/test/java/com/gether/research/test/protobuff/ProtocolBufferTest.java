package com.gether.research.test.protobuff;

import com.gether.research.protobuff.UserProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by myp on 2017/8/13.
 */
public class ProtocolBufferTest {

    @Test
    public void user() {
        UserProto.User u = UserProto.User.newBuilder().setId(1).setName("gether").setRemark("remark").setSex(UserProto.User.SEX.MALE).build();
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
        UserProto.User u1 = UserProto.User.newBuilder().setId(1).setName("gether").setRemark("remark").setSex(UserProto.User.SEX.MALE).build();
        UserProto.User u2 = UserProto.User.newBuilder().setId(2).setName("tom").setRemark("remark").setSex(UserProto.User.SEX.FEMALE).addFriends(u1).build();
        UserProto.User u3 = UserProto.User.newBuilder().setId(3).setName("jack").setRemark("remark").setSex(UserProto.User.SEX.MALE).addFriends(u1).addFriends(u2).build();

        u1 = UserProto.User.newBuilder(u1).addFriends(u2).addFriends(u3).build();
        System.out.println(u1.toString());
        System.out.println(u2.toString());
        System.out.println(u3.toString());

        u1.getFriendsList().forEach((u) -> System.out.println(u.toString()));
    }
}
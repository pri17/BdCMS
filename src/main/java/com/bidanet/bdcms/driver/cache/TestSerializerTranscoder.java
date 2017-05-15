package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * . <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class TestSerializerTranscoder implements Serializable {

    private static final long serialVersionUID = -1941046831377985500L;

    public TestSerializerTranscoder() {

    }

    public static void main(String[] args){
        testObject();
        testList();

    }

    public static void testObject() {
        List<TestUser> lists = buildTestData();

        TestUser userA = lists.get(0);

        ObjectsTranscoder<TestUser> objTranscoder =  new ObjectsTranscoder<>();

        byte[] result1 = objTranscoder.serialize(userA);

        TestUser userA_userA = objTranscoder.deserialize(result1);

        System.out.println(userA_userA.getName() + "\t" + userA_userA.getAge());
    }


    public static void testList() {
        List<TestUser> lists = buildTestData();


        ListTranscoder<TestUser> listTranscoder = new ListTranscoder<>();

        byte[] result1 = listTranscoder.serialize(lists);

        List<TestUser> results = listTranscoder.deserialize(result1);

        for (TestUser user : results) {
            System.out.println(user.getName() + "\t" + user.getAge());
        }

    }

    private static List<TestUser> buildTestData() {
        TestSerializerTranscoder tst = new TestSerializerTranscoder();
        TestUser userA =  tst.new TestUser();
        userA.setName("lily");
        userA.setAge(25);


        TestUser userB = tst.new TestUser();


        userB.setName("Josh Wang");
        userB.setAge(28);

        List<TestUser> list = new ArrayList<TestUser>();
        list.add(userA);
        list.add(userB);

        return list;
    }

    class TestUser implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;

        private int age;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }


}
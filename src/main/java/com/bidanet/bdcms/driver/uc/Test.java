package com.bidanet.bdcms.driver.uc;

import com.bidanet.bdcms.driver.cache.*;
import org.apache.commons.fileupload.util.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
@Service("testIn")

public class Test implements TestIn{

    @Autowired
    Cache cache;

    protected static Logger logger= LoggerFactory.getLogger(RedisCache.class);

    @Autowired
    private  JedisPool jedisPool;

//    @Autowired
//    private ListTranscoder listTranscoder;
//
//    @Autowired
//    private ObjectTranscoder objectTranscoder;

    /**
     * Test Data
     * @return
     */
    public  List<User> buildTestData(){

        List<User> list = new ArrayList<User>();
        for(int i=0;i<1000;i++){

            User a = new User();

            a.setName("name"+i);

            list.add(a);
        }

        return list;
    }

    public  void testSetElements(){
        List<User> testData = buildTestData();

        Jedis jedis = jedisPool.getResource();

        String key = "testSetElements" + new Random(1000).nextInt();

        ListTranscoder<User> listTranscoder = new ListTranscoder<User>();

        byte[] result1 = listTranscoder.serialize(testData);

        jedis.set(key.getBytes(), result1);




        //验证
        byte[] in = jedis.get(key.getBytes());
        List<User> list = listTranscoder.deserialize(in);
        for(User user : list){
            System.out.println("testSetElements user name is:" + user.getName());
        }
//        String keyValue = jedis.get(key);

//        System.out.println("keyValue="+keyValue);
    }

    public  void testSetEnsemble(){
//        List<User> testData = buildTestData();
//        Jedis jedis = jedisPool.getResource();
//        String key = "testSetEnsemble" + new Random(1000).nextInt();
//        jedis.set(key.getBytes(), listTranscoder.serialize(testData));
//
//        //验证
//        byte[] in = jedis.get(key.getBytes());
//        List<User> list = (List<User>)listTranscoder.deserialize(in);
//        for(User user : list){
//            System.out.println("testSetEnsemble user name is:" + user.getName());
//        }
    }



    public  void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.info("Unable to close %s", closeable, e);
            }
        }
    }

    @Override
    public void testInAction() {

        testSetElements();
        testSetEnsemble();
    }


    public void exec(RedisCache.RedisExec exec){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            exec.exec(jedis);
        }catch (Exception e){


        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }



    }

    public interface RedisExec{
        void exec(Jedis jedis);
    }



}

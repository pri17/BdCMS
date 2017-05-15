package com.bidanet.bdcms.driver.message;

/**
 * Created by Administrator on 2016/7/2.
 */
public interface Message {

    boolean send(String msg, String... to);
}

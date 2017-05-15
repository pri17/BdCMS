package com.bidanet.bdcms.common;

import com.bidanet.bdcms.driver.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分布式缓存工具
 */

public class CacheTool {
    private Map<String,Object> cacheMap=new HashMap<String,Object>();

    @Autowired
    Cache cache;

    public void set(String key,Object val){
        cache.set(key, val);
    }

    /**
     * 设置带有效期的缓存
     * @param key  key
     * @param val  值
     * @param seconds  有效期（秒）
     */
    public void set(String key,Object val,int seconds){
        cache.set(key, val,seconds);
    }
    public String get(String key){
        return cache.get(key);
    }
    public <T> T get(String key,Class<T> tClass){
        return cache.get(key,tClass);
    }
    public <T> List<T> getArray(String key,Class<T> tClass){
        return cache.getArray(key, tClass);
    }


}

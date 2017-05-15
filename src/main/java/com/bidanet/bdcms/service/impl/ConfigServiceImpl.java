package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.ConfigDao;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.entity.Config;
import com.bidanet.bdcms.service.ConfigService;

import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*配置管理
*/
@Service("configService")
public class ConfigServiceImpl extends BaseServiceImpl<Config> implements ConfigService {
    protected static final String cacheTag="config.";
    @Autowired
    Cache cache;




    @Autowired
    private ConfigDao configDao;
    @Override
    protected Dao getDao() {
        return configDao;
    }


   /* @Override
    public void getConfigList(Config config, Page<Config> page) {
        List<Config> list = configDao.findByExample(config, page.getPageCurrent(), page.getPageSize());
        long count = configDao.countByExample(config);
        page.setList(list);
        page.setTotal(count);
    }*/

    /**
     *
     * 更新配置的方法
     */
    @Override
    public void updateConfigT(Config config) {

        Config  updateConfig = configDao.get(config.getId());
        checkNull(updateConfig,"没有找到此配置！");
        updateConfig.setKey(config.getKey());
        updateConfig.setVal(config.getVal());
        updateConfig.setDescribe(config.getDescribe());
        updateConfig.setGroup(config.getGroup());
        updateConfig.setExt(config.getExt());
        updateConfig.setValType(config.getValType());
        updateConfig.setValExt(config.getValExt());




        configDao.update(updateConfig);

        cache.delete(cacheTag + updateConfig.getKey());

    }


    /**
     *
     *用户修改
     */
    @Override
    public void updateConfigUserT(long id,String val) {
        Config  updateConfig = configDao.get(id);
        updateConfig.setVal(val);
        configDao.update(updateConfig);


        cache.delete(cacheTag + updateConfig.getKey());
    }

    /**
     *
     *增加配置
     */
    @Override
    public void addConfigT(Config config) {


        checkString(config.getKey(),"key不能为空");
        checkString(config.getVal(),"val不能为空");
        checkString(config.getDescribe(),"描述不能为空");
        checkString(config.getGroup(),"分组不能为空");
       // checkString(config.getExt(),"ext不能为空");
        checkString(config.getValType(),"valType不能为空");
        checkString(config.getValExt(),"valExt不能为空");
        configDao.save(config);

    }

    /**
     *
     *删除配置
     */
    @Override
    public void deleteT(Long id) {
        configDao.delete(id);

    }

    /**
     *
     *配置列表里的分组查询
     */
    @Override
    public List<Object> getGroup() {
       List list= configDao.getGroup();
        return list;
    }


    @Override
    public String getConfig(String key){
        String s = cache.get(cacheTag + key);
        if (s==null){
            Config config = new Config();
            config.setKey(key);
            List<Config> list = configDao.findByExample(config);
            if (list.size()>0){
                s=list.get(0).getVal();
                cache.set(cacheTag+key,s);
            }else{
                return null;
            }
        }
        return s;
    }
    @Override
    public Float getConfigToFloat(String key){
        String config = getConfig(key);
        return Float.parseFloat(config);
    }
    @Override
    public Integer getConfigToInt(String key){
        String config = getConfig(key);
        return Integer.parseInt(config);
    }



}

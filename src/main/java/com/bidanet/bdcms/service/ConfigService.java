package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.Config;
import com.bidanet.bdcms.vo.Page;

import java.util.List;

/**
*
*/
public interface ConfigService extends Service<Config> {

    /*void getConfigList(Config config, Page<Config> page);*/

    void updateConfigT(Config config);

    /**
     *
     *用户修改参数（id，valExt）
     */
    void updateConfigUserT(long id,String valExt);

    void addConfigT(Config config);

    void deleteT(Long id);

    List<Object> getGroup();

    String getConfig(String key);

    Float getConfigToFloat(String key);

    Integer getConfigToInt(String key);
}

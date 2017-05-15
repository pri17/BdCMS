package com.bidanet.bdcms.driver.cache;

import javax.persistence.Transient;
import java.io.Serializable;

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

public class User implements Serializable{

    private String name;

    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

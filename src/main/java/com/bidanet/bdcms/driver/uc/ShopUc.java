package com.bidanet.bdcms.driver.uc;

import com.bidanet.bdcms.common.SpringWebTool;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/7/29.
 */
@Service("uc.shop")
public class ShopUc extends SessionUc {
    public ShopUc() {
        loginTag="shop_login";
    }
}

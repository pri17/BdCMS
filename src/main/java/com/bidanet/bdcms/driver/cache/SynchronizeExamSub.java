package com.bidanet.bdcms.driver.cache;

import java.io.Serializable;

/**
 * 省平台体检信息详细. <br>
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
public class SynchronizeExamSub implements Serializable {
    //主键
    private String uuid;
    //体检项目编号
    private String itemCode;
    //计量单位
    private String msrunt;
    //参考值
    private String itemStdvalue;
    //是否缺项
    private String isMiss;
    //体检项目结果
    private String bhkRst;
    //检查日期
    private String chkdat;
    //检查医生
    private String chkdoct;
    //是否合格
    private String rgltag;
}

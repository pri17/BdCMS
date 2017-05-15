package com.bidanet.bdcms.common;

/**
 * Created by jizhaoqun on 16/10/11.
 */
public interface SystemContent {

    /**
     * 数据库用户名称
     */
    String DB_USERNAME = ParsProperFile.getConfigProp("jdbc.username");

    /**
     * 数据库密码
     */
    String DB_PASSWORD = ParsProperFile.getConfigProp("jdbc.password");

    /**
     * mysql安装路径
     */
    String DB_PATH = ParsProperFile.getConfigProp("mysql.path");

    String MYSQL_URL = ParsProperFile.getConfigProp("mysql.url");

    /**
     * 需要备份的数据库的名称
     */
    String DB_NAME = ParsProperFile.getConfigProp("db.name");

    /**
     * 备份数据库存储路径
     */
    String DB_STOREPATH = ParsProperFile.getConfigProp("store.path");

    /**
     * 甲肝
     */
    String BLOD_HAV = "甲肝";

    /**
     * 戊肝
     */
    String BLOD_HEV = "戊肝";

    /**
     * 谷丙转氨酶
     */
    String BLOD_ALT = "谷丙转氨酶";

    /**
     * 体检项目 戊肝
     */
    Long PROJECT_HEV = 5L;

    /**
     * 体检项目 肠道带菌检查
     */
    Long PROJECT_CHANGDAO = 6L;

    /**
     * 体检项目 谷丙氨酶
     */
    Long PROJECT_ALT = 7L;

    /**
     * 体检项目 甲肝
     */
    Long PROJECT_HAV = 8L;

    /**
     * 体检项目 内科检查
     */
    Long PROJECT_MEDICAL = 9L;

    /**
     * 体检项目 胸部X线检查
     */
    Long PROJECT_X = 10L;

    /**
     * 体检项目类型 戊肝
     */
    Integer PROJECT_TYPE_HEV = 5;

    /**
     * 体检项目类型 肠道带菌检查
     */
    Integer PROJECT_TYPE_CHANGDAO = 6;

    /**
     * 体检项目类型 谷丙氨酶
     */
    Integer PROJECT_TYPE_ALT = 7;

    /**
     * 体检项目类型 甲肝
     */
    Integer PROJECT_TYPE_HAV = 8;

    /**
     * 体检项目类型 内科检查
     */
    Integer PROJECT_TYPE_MEDICAL = 9;

    /**
     * 体检项目类型 胸部X线检查
     */
    Integer PROJECT_TYPE_X = 10;

    /**
     * 当前体检项目合格与否 未判断
     */
    Integer PROJECT_ISQUALIFIED_WEIPANDUAN = 0;

    /**
     * 当前体检项目合格与否 合格
     */
    Integer PROJECT_ISQUALIFIED_YES = 1;

    /**
     * 当前体检项目合格与否 不合格
     */
    Integer PROJECT_ISQUALIFIED_NO = 2;

    /**
     * 当前体检项目合格与否 已采集
     */
    Integer PROJECT_ISQUALIFIED_CAIJI = 3;

    /**
     * 当前体检项目复检与否 否
     */
    Integer PROJECT_ISRECHECK_NO= 0;

    /**
     * 当前体检项目复检与否 是
     */
    Integer PROJECT_ISRECHECK_YES = 1;

    /**
     * 体检合格与否 未判断
     */
    Integer EXAM_ISQUALIFIED_WEIPANDUAN = 0;

    /**
     * 体检合格与否 合格
     */
    Integer EXAM_ISQUALIFIED_YES = 1;

    /**
     * 体检合格与否 不合格
     */
    Integer EXAM_ISQUALIFIED_NO = 2;

    /**
     * 描述用语类型 合格
     */
    Integer RESULT_TYPE_YES = 1;

    /**
     * 描述用语类型 不合格
     */
    Integer RESULT_TYPE_NO = 2;

    /**
     * 体检项目备注内容
     */
    String PROJECT_REMARK_DEFAULT = "必检项目";

    String QUALIFIED_SUCCESS = "合格";

    String QUALIFIED_FAIL = "不合格";

    String QUALIFIED_NOJUDGE = "未判断";

    String QUALIFIED_GATHER = "已采集";

    Integer JIKONGZHONGXIN = 132;

    Long JIKONGZHONGXINID = (long)31;

    String PROJECT_INTESTINAL = "肠检";

    String PROJECT_BLOD = "血检";

    String PROJECT_NK = "内科";

    String PROJECT_XXIAN = "胸部X线检查";

    String PROJECT_BEFORE_WORD = "您在预防性健康检查中发现";/*经检查,您在预防性健康检查中发现*/

    String PROJECT_AFTER_WORD = "不符合健康证明要求,请带好本人身份证及复检单来本中心接受复检,如在接到本告知书10个工作日内未答复,视为无异议.";

    Integer PROJECT_NUMBER  = 30;
    Integer REVIEW_PROJECT_NUMBER=25;

    //省平台错误类型
    //00	成功
    //01	机构编码、注册码不允许为空
    //02	身份验证失败
    //03	其他异常错误


}

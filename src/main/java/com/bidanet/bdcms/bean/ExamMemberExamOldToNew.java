package com.bidanet.bdcms.bean;

import java.math.BigDecimal;

/**
 * Created by xuejike-pc on 2017/2/20.
 */
public class ExamMemberExamOldToNew {

    private Long memberId;

    private Integer  age;

    private Long payId;

    private Long agenciesId;

    private Long examTime;

    /**
     * 体检日期格式化：yyyy-MM-dd HH:mm:ss
     */
    private String examTimeStr;

    /**
     * 体检日期格式化：yyyy-MM-dd
     */
    private String examTimeShort;

    private Long areaId;

    private Long examNumberId;

    private String examNumber;

    private Integer isRecheck;

    private String IHA;

    private String DDIA;

    private String COPT;

    private String STOOL;

    private Long eCardId;

    private Integer verifyState;

    private Long verifyId;

    private String verifyName;

    private Long verifyTime;

    private Integer verifyConclusion;

    private Long categoryId;

    private Long auditTime;

    private Long packageId;

    /**
     * 血检流水号对应id
     */
    private  Long examBlodId;

    /**
     * 肠检流水号对应id
     */
    private Long examIntestinalId;
    /**
     * 体检号条形码地址
     */
    private String examNumberIcon;

    /**
     * 录入状态 0 未录入完成 1 已录入完
     */
    private Integer entryState;

    //复检标志：0 1 2 3 只要有复检项目需要收费就逐级递增    取最新的一条数据
    private Integer recheckTag;

    private String recheckTagStr;

    //是否最新标识：0、否 1、是  查询数据的时候按照最新的那条数据去查
    private Integer isNew;

    //复检打印 0 未打印  1 已打印
    private Integer isRecheckPrint;

    private String isRecheckPrintStr;

    //用于显示上一次体检的时间
    private String upExamTimeStr;

    //用于复检管理 如果该字段为0 则不需要查询  如果该字段为1 并且is_new =1 时 需要判断
    private Integer isShowRecheck;

    private Integer isCancel;//是否作废

    //是否打印体检单或者复检单 0 否 1 是 进行更新
    private Integer isPhysicalPrint;

    private Long lastJudgeTime;

    private Integer isPush;

    //注册渠道:1-管理后台  2-微信  3-自助机
    private Integer channel;

    //是否是三星用户  0否 1是
    private Integer isThirdStar;

    private String channelStr;

    private  String affectedCodeName;

    private Long blodTestNumber;

    ///////////////////////////////2017-02-06新增//////////////////////////////////////////////

    //地区名称
    private String areaName;

    //行业名称
    private String categoryName;

    //支付方式  0：现金 1：微信支付 2：转账 3：记账 9、未支付默认
    private Integer payType;

    private String payTypeStr;

    //缴费状态   2：已付款  1：待付款
    private Integer payState;

    private String payStateStr;

    //健康证号
    private String eCardNumber;

    private Long expTime;

    private String expTimeStr;

    //体检机构名称
    private String agenciesName;


    private Integer agenciesCode;


    private BigDecimal payMoney;

    private BigDecimal payActualMoney;





    /*用户信息*/


    private Long id;

    private String name;

    private Integer sex;

    /**
     * 民族
     */
    private String nation;

    private String birthday;

    /**
     * 身份证地址
     */

    private String idCardAddress;

    /**
     *身份证号码
     */

    private String idCardNum;

    /**
     * 是否已婚
     */
    private Integer marriage;

    private String mobile;

    /**
     * 既往病史
     */
    private String anamnesis;

    private String icon;

    private Long createTime;

    private Long updateTime;

    private String sexStr;

    private String workUnit;

    private Integer isIdCardIcon;


    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getAgenciesId() {
        return agenciesId;
    }

    public void setAgenciesId(Long agenciesId) {
        this.agenciesId = agenciesId;
    }

    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    public String getExamTimeStr() {
        return examTimeStr;
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }

    public String getExamTimeShort() {
        return examTimeShort;
    }

    public void setExamTimeShort(String examTimeShort) {
        this.examTimeShort = examTimeShort;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getExamNumberId() {
        return examNumberId;
    }

    public void setExamNumberId(Long examNumberId) {
        this.examNumberId = examNumberId;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public Integer getIsRecheck() {
        return isRecheck;
    }

    public void setIsRecheck(Integer isRecheck) {
        this.isRecheck = isRecheck;
    }

    public String getIHA() {
        return IHA;
    }

    public void setIHA(String IHA) {
        this.IHA = IHA;
    }

    public String getDDIA() {
        return DDIA;
    }

    public void setDDIA(String DDIA) {
        this.DDIA = DDIA;
    }

    public String getCOPT() {
        return COPT;
    }

    public void setCOPT(String COPT) {
        this.COPT = COPT;
    }

    public String getSTOOL() {
        return STOOL;
    }

    public void setSTOOL(String STOOL) {
        this.STOOL = STOOL;
    }

    public Long geteCardId() {
        return eCardId;
    }

    public void seteCardId(Long eCardId) {
        this.eCardId = eCardId;
    }

    public Integer getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(Integer verifyState) {
        this.verifyState = verifyState;
    }

    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public Long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Integer getVerifyConclusion() {
        return verifyConclusion;
    }

    public void setVerifyConclusion(Integer verifyConclusion) {
        this.verifyConclusion = verifyConclusion;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getExamBlodId() {
        return examBlodId;
    }

    public void setExamBlodId(Long examBlodId) {
        this.examBlodId = examBlodId;
    }

    public Long getExamIntestinalId() {
        return examIntestinalId;
    }

    public void setExamIntestinalId(Long examIntestinalId) {
        this.examIntestinalId = examIntestinalId;
    }

    public String getExamNumberIcon() {
        return examNumberIcon;
    }

    public void setExamNumberIcon(String examNumberIcon) {
        this.examNumberIcon = examNumberIcon;
    }

    public Integer getEntryState() {
        return entryState;
    }

    public void setEntryState(Integer entryState) {
        this.entryState = entryState;
    }

    public Integer getRecheckTag() {
        return recheckTag;
    }

    public void setRecheckTag(Integer recheckTag) {
        this.recheckTag = recheckTag;
    }

    public String getRecheckTagStr() {
        return recheckTagStr;
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsRecheckPrint() {
        return isRecheckPrint;
    }

    public void setIsRecheckPrint(Integer isRecheckPrint) {
        this.isRecheckPrint = isRecheckPrint;
    }

    public String getIsRecheckPrintStr() {
        return isRecheckPrintStr;
    }

    public void setIsRecheckPrintStr(String isRecheckPrintStr) {
        this.isRecheckPrintStr = isRecheckPrintStr;
    }

    public String getUpExamTimeStr() {
        return upExamTimeStr;
    }

    public void setUpExamTimeStr(String upExamTimeStr) {
        this.upExamTimeStr = upExamTimeStr;
    }

    public Integer getIsShowRecheck() {
        return isShowRecheck;
    }

    public void setIsShowRecheck(Integer isShowRecheck) {
        this.isShowRecheck = isShowRecheck;
    }

    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    public Integer getIsPhysicalPrint() {
        return isPhysicalPrint;
    }

    public void setIsPhysicalPrint(Integer isPhysicalPrint) {
        this.isPhysicalPrint = isPhysicalPrint;
    }

    public Long getLastJudgeTime() {
        return lastJudgeTime;
    }

    public void setLastJudgeTime(Long lastJudgeTime) {
        this.lastJudgeTime = lastJudgeTime;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getIsThirdStar() {
        return isThirdStar;
    }

    public void setIsThirdStar(Integer isThirdStar) {
        this.isThirdStar = isThirdStar;
    }

    public String getChannelStr() {
        return channelStr;
    }

    public void setChannelStr(String channelStr) {
        this.channelStr = channelStr;
    }

    public String getAffectedCodeName() {
        return affectedCodeName;
    }

    public void setAffectedCodeName(String affectedCodeName) {
        this.affectedCodeName = affectedCodeName;
    }

    public Long getBlodTestNumber() {
        return blodTestNumber;
    }

    public void setBlodTestNumber(Long blodTestNumber) {
        this.blodTestNumber = blodTestNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getPayStateStr() {
        return payStateStr;
    }

    public void setPayStateStr(String payStateStr) {
        this.payStateStr = payStateStr;
    }

    public String geteCardNumber() {
        return eCardNumber;
    }

    public void seteCardNumber(String eCardNumber) {
        this.eCardNumber = eCardNumber;
    }

    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }

    public String getExpTimeStr() {
        return expTimeStr;
    }

    public void setExpTimeStr(String expTimeStr) {
        this.expTimeStr = expTimeStr;
    }

    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }

    public Integer getAgenciesCode() {
        return agenciesCode;
    }

    public void setAgenciesCode(Integer agenciesCode) {
        this.agenciesCode = agenciesCode;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getPayActualMoney() {
        return payActualMoney;
    }

    public void setPayActualMoney(BigDecimal payActualMoney) {
        this.payActualMoney = payActualMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public Integer getMarriage() {
        return marriage;
    }

    public void setMarriage(Integer marriage) {
        this.marriage = marriage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public Integer getIsIdCardIcon() {
        return isIdCardIcon;
    }

    public void setIsIdCardIcon(Integer isIdCardIcon) {
        this.isIdCardIcon = isIdCardIcon;
    }
}

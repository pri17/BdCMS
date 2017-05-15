package com.bidanet.bdcms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 体检人员体检信息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-14 14:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_member_exam")
public class ExamMemberExam {

    private Long id;

    private Long createTime;

    private Long updateTime;

    private Long memberId;

    private String mobile;

    private String workUnit;

    private Integer  age;

    private Long payId;

    private Long agenciesId;

    private Long examTime;

    /**
     * 新增字段，判断该肠检是否已经打印过, 2017/05/15
     */
    private Integer inteIsPrint;

    @Column (name = "intestinalIsPrint")
    public Integer getInteIsPrint() {
        return inteIsPrint;
    }

    public void setInteIsPrint(Integer inteIsPrint) {
        this.inteIsPrint = inteIsPrint;
    }

    /**
     * 体检日期格式化：yyyy-MM-dd HH:mm:ss
     */
    private String examTimeStr;

    /**
     * 体检日期格式化：yyyy-MM-dd
     */
    private String examTimeShort;
    /**
     * 体检日期格式化：yyyy年MM月dd日
     */
    private Map examTimeShortCN=new HashMap();

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

//    private ExamBlod examBlod;

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

    /**
     * 用户主数据
     */
    private ExamMember examMember;

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

    //身份证号
    private String idCardNum;

    //姓名
    private String name;

    //性别
    private Integer sex;

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

    private String sexStr;

    private String birthday;

    private Integer agenciesCode;

    private String icon;

    private BigDecimal payMoney;

    private BigDecimal payActualMoney;

    private Integer isIdCardIcon;

    private Map<String,String> projectData=new HashMap<>();
//    /**
//     * 生成的健康证数据
//     */
//    private ExamEcard examEcard;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (name = "create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column (name = "update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Column (name = "member_id")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column (name = "work_unit")
    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    @Column (name = "agencies_id")
    public Long getAgenciesId() {
        return agenciesId;
    }

    public void setAgenciesId(Long agenciesId) {
        this.agenciesId = agenciesId;
    }

    @Column (name = "exam_time")
    public Long getExamTime() {
        return examTime;
    }

    public void setExamTime(Long examTime) {
        this.examTime = examTime;
    }

    @Transient
    public String getExamTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.examTime!=null){
            return df.format(this.examTime);
        }else{
            return "";
        }
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }

    @Transient
    public Map getExamTimeShortCN() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map map=new HashMap();
        if(this.auditTime!=null){
            map.put("year",df.format(this.auditTime).substring(0,4));
            map.put("month",df.format(this.auditTime).substring(5,7));
            map.put("day",df.format(this.auditTime).substring(8,10));
            return map;
        }else{
            map.put("year","");
            map.put("month","");
            map.put("day","");
            return  map;
        }
    }

    public void setExamTimeShortCN(Map examTimeShortCN) {
        this.examTimeShortCN = examTimeShortCN;
    }

    @Column (name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Column (name = "exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
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

    @Column (name = "verify_state")
    public Integer getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(Integer verifyState) {
        this.verifyState = verifyState;
    }

    @Column (name = "verify_id")
    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    @Column(name = "verify_name")
    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    @Column (name = "verify_time")
    public Long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Long verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Column (name = "verify_conclusion")
    public Integer getVerifyConclusion() {
        return verifyConclusion;
    }

    public void setVerifyConclusion(Integer verifyConclusion) {
        this.verifyConclusion = verifyConclusion;
    }

    @Column (name = "category_id")
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Column (name = "audit_time")
    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    @Column (name = "package_id")
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name="exam_blod_id")
    public Long getExamBlodId() {
        return examBlodId;
    }

    public void setExamBlodId(Long examBlodId) {
        this.examBlodId = examBlodId;
    }

    @Column(name="exam_intestinal_id")
    public Long getExamIntestinalId() {
        return examIntestinalId;
    }

    public void setExamIntestinalId(Long examIntestinalId) {
        this.examIntestinalId = examIntestinalId;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "area_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamArea getExamArea() {
//        return examArea;
//    }
//
//    public void setExamArea(ExamArea examArea) {
//        this.examArea = examArea;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "agencies_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamAgencies getExamAgencies() {
//        return examAgencies;
//    }
//
//    public void setExamAgencies(ExamAgencies examAgencies) {
//        this.examAgencies = examAgencies;
//    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",insertable = false,updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public ExamMember getExamMember() {
        return examMember;
    }

    public void setExamMember(ExamMember examMember) {
        this.examMember = examMember;
    }

    @Column (name = "exam_number_id")
    public Long getExamNumberId() {
        return examNumberId;
    }

    public void setExamNumberId(Long examNumberId) {
        this.examNumberId = examNumberId;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamCategory getExamCategory() {
//        return examCategory;
//    }
//
//    public void setExamCategory(ExamCategory examCategory) {
//        this.examCategory = examCategory;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pay_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamPay getExamPay() {
//        return examPay;
//    }
//
//    public void setExamPay(ExamPay examPay) {
//        this.examPay = examPay;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "verify_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    @Column (name = "is_recheck")
    public Integer getIsRecheck() {
        return isRecheck;
    }

    public void setIsRecheck(Integer isRecheck) {
        this.isRecheck = isRecheck;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column (name = "pay_id")
    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    @Column (name = "ecard_id")
    public Long geteCardId() {
        return eCardId;
    }

    public void seteCardId(Long eCardId) {
        this.eCardId = eCardId;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ecard_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamEcard getExamEcard() {
//        return examEcard;
//    }
//
//    public void setExamEcard(ExamEcard examEcard) {
//        this.examEcard = examEcard;
//    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "exam_intestinal_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamIntestinal getExamIntestinal() {
//        return examIntestinal;
//    }
//
//    public void setExamIntestinal(ExamIntestinal examIntestinal) {
//        this.examIntestinal = examIntestinal;
//    }

    @Column(name="exam_number_icon")
    public String getExamNumberIcon() {
        return examNumberIcon;
    }

    public void setExamNumberIcon(String examNumberIcon) {
        this.examNumberIcon = examNumberIcon;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "exam_blod_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamBlod getExamBlod() {
//        return examBlod;
//    }
//
//    public void setExamBlod(ExamBlod examBlod) {
//        this.examBlod = examBlod;
//    }

    @Transient
    public String getExamTimeShort() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.examTime!=null){
            return df.format(this.examTime);
        }else{
            return "";
        }
    }

    public void setExamTimeShort(String examTimeShort) {
        this.examTimeShort = examTimeShort;
    }

    @Column (name = "recheck_tag")
    public Integer getRecheckTag() {
        return recheckTag;
    }

    public void setRecheckTag(Integer recheckTag) {
        this.recheckTag = recheckTag;
    }

    @Column (name = "is_new")
    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    @Column (name = "is_recheck_print")
    public Integer getIsRecheckPrint() {
        return isRecheckPrint;
    }

    public void setIsRecheckPrint(Integer isRecheckPrint) {
        this.isRecheckPrint = isRecheckPrint;
    }

    @Column (name = "entry_state")
    public Integer getEntryState() {
        return entryState;
    }

    public void setEntryState(Integer entryState) {
        this.entryState = entryState;
    }

    @Transient
    public String getUpExamTimeStr() {
        return upExamTimeStr;
    }

    public void setUpExamTimeStr(String upExamTimeStr) {
        this.upExamTimeStr = upExamTimeStr;
    }

    @Column (name = "is_show_recheck")
    public Integer getIsShowRecheck() {
        return isShowRecheck;
    }

    public void setIsShowRecheck(Integer isShowRecheck) {
        this.isShowRecheck = isShowRecheck;
    }

//    复检记录打印：0、未打印 1、已打印
    @Transient
    public String getIsRecheckPrintStr() {
        if (this.isRecheckPrint==0){

            return "未打印";

        }else if(this.isRecheckPrint==1){

            return "已打印";

        }
        return "";
    }

    public void setIsRecheckPrintStr(String isRecheckPrintStr) {
        this.isRecheckPrintStr = isRecheckPrintStr;
    }

    @Column (name = "is_cancel")
    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    @Transient
    public String getRecheckTagStr() {
        if (this.recheckTag>0){
            return "复检";
        }else{
            return "初检";
        }
    }

    public void setRecheckTagStr(String recheckTagStr) {
        this.recheckTagStr = recheckTagStr;
    }

    @Column(name = "is_physical_print")
    public Integer getIsPhysicalPrint() {
        return isPhysicalPrint;
    }

    public void setIsPhysicalPrint(Integer isPhysicalPrint) {
        this.isPhysicalPrint = isPhysicalPrint;
    }

    @Column(name = "last_judge_time")
    public Long getLastJudgeTime() {
        return lastJudgeTime;
    }

    public void setLastJudgeTime(Long lastJudgeTime) {
        this.lastJudgeTime = lastJudgeTime;
    }

    @Column(name = "is_push")
    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    /**
     * 注册渠道  1后台 2微信 3自助机 4企业
     * @return
     */
    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    /**
     *是否是三星用户  0 否 1 是
     * @return
     */
    @Column(name = "is_third_star")
    public Integer getIsThirdStar() {
        return isThirdStar;
    }

    public void setIsThirdStar(Integer isThirdStar) {
        this.isThirdStar = isThirdStar;
    }

    @Transient
    public String getChannelStr() {
        if (this.channel==1){

            return "电脑端";

        }else if(this.channel==2){

            return "微信";

        }else if(this.channel==3){

            return "自助机";

        }else if(this.channel==4){
            return "企业";
        }else{
            return "";
        }

    }

    public void setChannelStr(String channelStr) {
        this.channelStr = channelStr;
    }

    /**
     * 该人员对应的疫区的数据
     * @return
     */
    @Transient
    public String getAffectedCodeName() {
        return affectedCodeName;
    }

    public void setAffectedCodeName(String affectedCodeName) {
        this.affectedCodeName = affectedCodeName;
    }

    /**
     * 血检检验号
     * @return
     */
    @Transient
    public Long getBlodTestNumber() {
        return blodTestNumber;
    }

    public void setBlodTestNumber(Long blodTestNumber) {
        this.blodTestNumber = blodTestNumber;
    }


    ///////////////////////////////2017-02-06新增//////////////////////////////////////////////

    @Column (name = "idCard_num")
    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
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

    @Column(name = "area_name")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column (name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column (name = "pay_type")
    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    @Column (name = "pay_state")
    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    @Transient
    public String getPayStateStr() {
        if(this.payState!=null){
            if (this.payState == 1){

                return "否";

            }else if(this.payState==2){

                return "是";

            }
        }
        return "";
    }

    public void setPayStateStr(String payStateStr) {
        this.payStateStr = payStateStr;
    }

    @Transient
    public String getPayTypeStr() {

        if(this.payState!=null){
            if (this.payState == 1){

                return "否";

            }else if(this.payState==2){

                return "是";

            }
        }
        return "";
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    @Column (name = "exp_time")
    public Long getExpTime() {
        return expTime;
    }

    public void setExpTime(Long expTime) {
        this.expTime = expTime;
    }

    @Transient
    public String getExpTimeStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        if(this.expTime!=null){
            return df.format(this.expTime);
        }else{
            return "";
        }
    }

    public void setExpTimeStr(String expTimeStr) {
        this.expTimeStr = expTimeStr;
    }

    @Column (name = "eCard_number")
    public String geteCardNumber() {
        return eCardNumber;
    }

    public void seteCardNumber(String eCardNumber) {
        this.eCardNumber = eCardNumber;
    }

    @Column (name = "agencies_name")
    public String getAgenciesName() {
        return agenciesName;
    }

    public void setAgenciesName(String agenciesName) {
        this.agenciesName = agenciesName;
    }

    @Column (name = "agencies_code")
    public Integer getAgenciesCode() {
        return agenciesCode;
    }

    public void setAgenciesCode(Integer agenciesCode) {
        this.agenciesCode = agenciesCode;
    }

    @Transient
    public String getSexStr() {
        String sexstr = "";
        if (this.sex==0){
            sexstr = "女";
        }else if(this.sex==1){
            sexstr = "男";
        }

        return sexstr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column (name = "pay_money")
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    @Column(name = "pay_actual_money")
    public BigDecimal getPayActualMoney() {
        return payActualMoney;
    }

    public void setPayActualMoney(BigDecimal payActualMoney) {
        this.payActualMoney = payActualMoney;
    }

    @Column (name = "is_idCard_icon")
    public Integer getIsIdCardIcon() {
        return isIdCardIcon;
    }

    public void setIsIdCardIcon(Integer isIdCardIcon) {
        this.isIdCardIcon = isIdCardIcon;
    }

    @Transient
    public Map<String, String> getProjectData() {
        return projectData;
    }

    public void setProjectData(Map<String, String> projectData) {
        this.projectData = projectData;
    }



}

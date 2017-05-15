package com.bidanet.bdcms.entity;/**
 * Created by jizhaoqun on 16/10/9.
 */

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 支付. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-09 16:36
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Entity
@Table(name="exam_pay")
@Inheritance(strategy= InheritanceType.JOINED)
public class ExamPay {


    private Long id;

//    private Long memberId;

    private Long examMemberId;



    private Long payTime;

    /**
     * 支付金额
     */

    private BigDecimal payMoney;

    /**
     * 支付方式  0：现金 1：微信支付 2：转账 3：记账
     */

    private Integer payType;

    private String payTypeStr;

    /**
     * 缴费状态   2：已付款  1：待付款
     */

    private Integer payState;

    private String payStateStr;

    private Long createTime;


    private Integer isPrint;


    private Long examNumberId;

    private String examNumber;

//    //用户表
//    private ExamMember member;
//
//    //用户体检号表
//    private ExamNumber number;


    private Long creatorId;

    private String creatorName;

    //创建人员列表
    private User user;


    private String createTimeStr;

    private String payTimeStr;

    private String remarks;

    private BigDecimal payActualMoney;

    private Long tollCollectorId;

    private String tollCollectorName;
    /**
     * 是否特殊人员-缴费需要  当为特殊人员时 可以不用缴费  0：不特殊默认   1：特殊人员
     */
    private Integer isSpecial;

    //复检标志：0 1 2 3 只要有复检项目需要收费就逐级递增    取最新的一条数据
    private Integer recheckTag;

    //是否最新标识：0、否 1、是  查询数据的时候按照最新的那条数据去查
    private Integer isNew;

    private Integer isCancel;//是否作废

    private Integer isRemark;//体检综合表 备注标识，用于备注特殊用户：0、否 1、是


    ////////////////////////////2017-02-06新增////////////////////////////////////////////
    /**
     * 工作单位
     */
    private String workUnit;

    /**
     * 身份证号码
     */
    private String idCardNum;

    /**
     *地区名称
     */
    private String areaName;

    /**
     *地区id
     */
    private Long areaId;

    private String name;

    private String icon;

    private String wxPayData;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="exam_number")
    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    @Column(name="pay_time")
    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    @Column(name="pay_money")
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name="pay_type")
    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 支付方式  0：现金 1：微信支付 2：转账 3：记账
     */
    @Transient
    public String getPayTypeStr() {
        if (this.payType!=null) {
            if (this.payType==0){

                return "现金";

            }else if(this.payType==1){

                return "银联";

            }else if(this.payType==2){

                return "转账";

            }else if(this.payType==3){

                return "记账";

            }else if(this.payType==4){

                return "微信";

            }
        }

       return null;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    @Column(name="pay_state")
    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    @Column(name="create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name="is_print")
    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    @Column(name="exam_number_id")
    public Long getExamNumberId() {
        return examNumberId;
    }

    public void setExamNumberId(Long examNumberId) {
        this.examNumberId = examNumberId;
    }

    @Column (name = "creator_id")
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Column (name = "creator_name")
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamMember getMember() {
//        return member;
//    }
//
//    public void setMember(ExamMember member) {
//        this.member = member;
//    }
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "exam_number_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public ExamNumber getNumber() {
//        return number;
//    }
//
//    public void setNumber(ExamNumber number) {
//        this.number = number;
//    }


//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "creator_id",insertable = false,updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    @Transient
    public String getCreateTimeStr(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.createTime!=null){
            return df.format(this.createTime);
        }else{
            return "";
        }
    }
    @Transient
    public String getPayTimeStr(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(this.payTime!=null){
            return df.format(this.payTime);
        }else{
            return "";
        }
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "pay_actual_money")
    public BigDecimal getPayActualMoney() {
        return payActualMoney;
    }

    public void setPayActualMoney(BigDecimal payActualMoney) {
        this.payActualMoney = payActualMoney;
    }

    @Column(name = "is_special")
    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

    @Column (name = "exam_member_id")
    public Long getExamMemberId() {
        return examMemberId;
    }

    public void setExamMemberId(Long examMemberId) {
        this.examMemberId = examMemberId;
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

    @Column (name = "toll_collector_id")
    public Long getTollCollectorId() {
        return tollCollectorId;
    }

    public void setTollCollectorId(Long tollCollectorId) {
        this.tollCollectorId = tollCollectorId;
    }

    @Column (name = "toll_collector_name")
    public String getTollCollectorName() {
        return tollCollectorName;
    }

    public void setTollCollectorName(String tollCollectorName) {
        this.tollCollectorName = tollCollectorName;
    }

    /**
     * 缴费状态   2：已付款  1：待付款
     */
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

    @Column (name = "is_cancel")
    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    @Column (name = "is_remark")
    public Integer getIsRemark() {
        return isRemark;
    }

    public void setIsRemark(Integer isRemark) {
        this.isRemark = isRemark;
    }


    ////////////////////////////2017-02-06新增////////////////////////////////////////////

    @Column (name = "work_unit")
    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }
    @Column (name = "idCard_num")
    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }
    @Column (name = "area_name")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    @Column (name = "area_id")
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "wx_pay_data")
    @Type(type = "text")
    public String getWxPayData() {
        return wxPayData;
    }

    public void setWxPayData(String wxPayData) {
        this.wxPayData = wxPayData;
    }
}

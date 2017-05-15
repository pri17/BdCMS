package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * 新旧数据对应表
 * Created by xuejike-pc on 2017/2/20.
 */
@Entity
@Table(name = "new_and_old_data_exchange")
public class NewAndOldDataExchange {
    /*编号*/
    private Long id;
    /*旧数据编号*/
    private Long oldId;
    /*旧数据体检号*/
    private  String physicalNumberOld;
    /*新数据编号*/
    private Long newId;
    /*新数据体检号*/
    private  String physicalNumberNew;
    /*新数据库数据转到旧数据库标识*/
    private Boolean newToOldTag;
    /*旧数据库数据转到新数据库标识*/
    private Boolean oldToNewTage;
    /*体检结果同步数据*/
    private Boolean physicalResult;
    private Long exchangeTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="old_id")
    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }
    @Column(name="new_id")
    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    @Column(name="exchange_time")
    public Long getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Long exchangeTime) {
        this.exchangeTime = exchangeTime;
    }
    @Column(name="new_to_old_tag")
    public Boolean getNewToOldTag() {
        return newToOldTag;
    }

    public void setNewToOldTag(Boolean newToOldTag) {
        this.newToOldTag = newToOldTag;
    }
    @Column(name="old_to_new_tage")
    public Boolean getOldToNewTage() {
        return oldToNewTage;
    }

    public void setOldToNewTage(Boolean oldToNewTage) {
        this.oldToNewTage = oldToNewTage;
    }

    @Column(name="physical_number_old")
    public String getPhysicalNumberOld() {
        return physicalNumberOld;
    }

    public void setPhysicalNumberOld(String physicalNumberOld) {
        this.physicalNumberOld = physicalNumberOld;
    }
    @Column(name="physical_number_new")
    public String getPhysicalNumberNew() {
        return physicalNumberNew;
    }

    public void setPhysicalNumberNew(String physicalNumberNew) {
        this.physicalNumberNew = physicalNumberNew;
    }

    @Column(name="physical_result")
    public Boolean getPhysicalResult() {
        return physicalResult;
    }

    public void setPhysicalResult(Boolean physicalResult) {
        this.physicalResult = physicalResult;
    }
}

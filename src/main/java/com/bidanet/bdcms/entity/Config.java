package com.bidanet.bdcms.entity;

import javax.persistence.*;

/**
 * Created by xuejike on 2016-05-31.
 */
@Entity
@Table(name = "_config")
public class Config {

    private long id;
    /**
     * 配置-键
     */
    private String key;
    /**
     * 配置-值
     */
    private String val;
    /**
     * text  文本框、select 下拉框
     */
    private String valType;
    /**
     * 下拉可选项 [{key:"xxx2",text:"文字说明2"},{key:"xxx1",text:"文字说明1"}]
     */
    private String valExt;
    /**
     * 配置-扩展
     */
    private String ext;
    /**
     * 配置项-描述
     */
    private String describe;
    /**
     * 配置项-分组
     */
    private String group;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "_val")
    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Column(name = "_describe")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    @Column(name = "_group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getValExt() {return valExt;}

    public void setValExt(String valExt) {this.valExt = valExt;}

    public String getValType() {return valType;}

    public void setValType(String valType) {this.valType = valType;}

}

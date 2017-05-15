package com.bidanet.bdcms.entity;

import com.bidanet.bdcms.entity.entityEnum.Status;

import javax.persistence.*;

/**
 * 机器码
 */
@Entity
@Table(name = "_machine_code")
public class MachineCode {
    private long id;
    private Integer code;
    private String ip;
    private Status status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Enumerated(EnumType.ORDINAL)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

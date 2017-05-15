package com.bidanet.bdcms.common;

import com.bidanet.bdcms.entity.MachineCode;
import com.bidanet.bdcms.entity.entityEnum.Status;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.MachineCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuejike on 2016-06-12.
 */
@Service
public class MachineCodeTool {
    public static final int TUANCOUPON = 10;
    public static final int DB_USER=1;
    public static final int DB_ORDER=2;
    public static final int DB_CASH_ORDER=3;

    protected static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");



    @Autowired
    MachineCodeService machineCodeService;
    private MachineCode machineCode;
    protected  int num=0;

    @PostConstruct
    public void initLoadCode(){
        machineCode = machineCodeService.getMachineCodeT();
        System.out.println("获取到机器码:"+machineCode.getCode());
    }

    @PreDestroy
    public void recoveryCode(){
        machineCodeService.recoveryCodeT(machineCode.getId());
        System.out.println("回收机器号:"+machineCode.getCode());
    }


    public String getCode(int serviceCode){
        String s = new Date().getTime()/10 + String.format("%01d", machineCode.getCode()) + String.format("%02d", serviceCode) + String.format("%02d", getNum());
        s=s.substring(2);
        StringBuilder sb=new StringBuilder();
        int len=4;
        sb.append(s.substring(len,len*2))
                .append(s.substring(len*3))
                .append(s.substring(0,len))
                .append(s.substring(len*2,len*3));
        return sb.toString();
    }

    /**
     * 唯一编码：字符串
     * @param serviceCode
     * @param
     * @return
     */
    public String getCodeNomal(int serviceCode){
        String format = simpleDateFormat.format(new Date());
        String s = format + String.format("%02d", machineCode.getCode()) + String.format("%02d", serviceCode) + String.format("%02d", getNum());

        return s;
    }

    /**
     * 生成唯一编号
     * @param serviceCode
     * @return
     */
    public Long getOnlyId(int serviceCode){
        String s = new Date().getTime() + String.format("%02d", machineCode.getCode()) + String.format("%02d", serviceCode) + String.format("%02d", getNum());
        String substring = s.substring(1, s.length());
        return Long.parseLong(substring);
    }

    public synchronized int getNum(){
        num++;
        if(num>99){
            num=0;
        }
        return num;
    }




}

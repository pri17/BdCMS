package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.MachineCodeDao;
import com.bidanet.bdcms.entity.MachineCode;
import com.bidanet.bdcms.entity.entityEnum.Status;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.MachineCodeService;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
*
*/
@Service
public class MachineCodeServiceImpl extends BaseServiceImpl<MachineCode> implements MachineCodeService {
    @Autowired
    private MachineCodeDao dao;


    @Override
    protected Dao getDao() {
        return dao;
    }



    @Override
    public MachineCode getMachineCodeT(){
//        Long l=8999999999999999999L;
        MachineCode query = new MachineCode();
        List<MachineCode> list=new ArrayList<>();
        String ipAddress= UUID.randomUUID().toString();
        //1.按IP查询
        try {
            ipAddress = InetAddress.getLocalHost().toString();
            query.setIp(ipAddress);
            list = dao.findByExample(query, MatchMode.EXACT);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (list.size()==0){
            //2.查询可用的
            query.setIp(null);
            query.setStatus(Status.enable);
            list = dao.findByExample(query);

        }


        if (list.size()>0){
            MachineCode machineCode = list.get(0);
            machineCode.setStatus(Status.disable);

            machineCode.setIp(ipAddress);

            updateT(machineCode);
            return machineCode;

        }else {
            int maxCode = dao.getMaxCode();
            if (maxCode>999){
                throw new CheckException("机器码长度超过规定值:"+maxCode);
            }
            MachineCode code = new MachineCode();
            code.setStatus(Status.disable);
            code.setCode(maxCode+1);
            code.setIp(ipAddress);
            insertT(code);
            return code;
//            System.out.println("无法获取服务器机号");
//            throw new CheckException("无法获取服务器机号");
        }
    }



    @Override
    public void recoveryCodeT(long id){
        MachineCode machineCode = dao.get(id);
        machineCode.setStatus(Status.enable);
        updateT(machineCode);
        System.out.println("回收机器号:"+machineCode.getCode());
    }


}

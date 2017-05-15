package com.bidanet.bdcms.dao.impl;

import com.bidanet.bdcms.dao.MachineCodeDao;
import com.bidanet.bdcms.entity.MachineCode;
import org.springframework.stereotype.Repository;

/**
*
*/
@Repository
public class MachineCodeDaoImpl extends BaseDaoImpl<MachineCode> implements MachineCodeDao {

    @Override
    public int getMaxCode() {
        Object o = getSession().createQuery("select max(code) from MachineCode ").uniqueResult();
        if (o==null){
            return 0;
        }
        int i = Integer.parseInt(o.toString());

        return i;
    }
}

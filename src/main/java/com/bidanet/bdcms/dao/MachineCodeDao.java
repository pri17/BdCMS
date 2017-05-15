package com.bidanet.bdcms.dao;

import com.bidanet.bdcms.entity.MachineCode;

/**
* MachineCode DAO
*/
public interface MachineCodeDao extends Dao<MachineCode> {
    int getMaxCode();
}

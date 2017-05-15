package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.MachineCode;

/**
*
*/
public interface MachineCodeService extends Service<MachineCode> {

    MachineCode getMachineCodeT();

    void recoveryCodeT(long id);
}

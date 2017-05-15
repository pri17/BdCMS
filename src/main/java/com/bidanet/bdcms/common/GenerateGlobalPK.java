package com.bidanet.bdcms.common;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Administrator on 2016/6/23.
 */

public class GenerateGlobalPK  implements Configurable, IdentifierGenerator {
    protected int serviceCode;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        String serviceCode = properties.getProperty("serviceCode");
        this.serviceCode=Integer.parseInt(serviceCode);



    }

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        MachineCodeTool machineCodeTool = SpringWebTool.getBean(MachineCodeTool.class);
        return machineCodeTool.getOnlyId(serviceCode);
//        return Long.parseLong(codeNomal);
    }
}

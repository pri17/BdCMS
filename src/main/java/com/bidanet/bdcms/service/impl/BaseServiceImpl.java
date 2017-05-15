package com.bidanet.bdcms.service.impl;


import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.hibernate.criterion.MatchMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xuejike on 2015/11/3.
 */
public abstract class BaseServiceImpl<T> implements Service<T> {

//    @Resource(name = "eventBus")
//    EventBus eventBus;
//
//    @Resource(name = "asyncEventBus")
//    AsyncEventBus asynEventBus;


//    @PostConstruct
//    public void registerEventBus(){
//        eventBus.register(this);
//        asynEventBus.register(this);
//
//        System.out.println("eventBus-register:"+this.getClass().getSimpleName());
//    }
//
//    @PreDestroy
//    public void unregisterEventBus(){
//        eventBus.unregister(this);
//        asynEventBus.unregister(this);
//        System.out.println("eventBus-unregister:"+this.getClass().getSimpleName());
//    }

    @Override
    public T get(Serializable id) {
        if (id == null) {
            return null;
        }
        return (T) getDao().get(id);
    }

    protected abstract Dao getDao();

    @Override
    public T load(Serializable id) {
        return (T) getDao().load(id);
    }


    @Override
    public void query(T entity, Page<T> page) {
        List list = getDao().findByExample(entity, page.getPageCurrent(), page.getPageSize());
        long count = getDao().countByExample(entity);
        page.setTotal(count);
        page.setList(list);
    }

    @Override
    public List<T> query(T entity) {
        return getDao().findByExample(entity);
//        return null;
    }

    @Override
    public void insertT(T t) {
        getDao().save(t);
    }

    @Override
    public void updateT(T t) {
        getDao().update(t);
    }

    @Override
    public T updateBack(T t)
    {
        getDao().updateBack(t);
        return t;
    }


    @Override
    public List<T> getList() {
        return getDao().findAll();

    }

    @Override
    public void deleteByIdT(Serializable id) {
        canDelete(id);
        getDao().delete(id);

    }

    @Override
    public void deleteAllT() {

    }

    @Override
    public long count() {
        return getDao().count();
    }

    protected void checkNull(Object obj, String msg) {
        if (obj == null) {
            throw new CheckException(msg);
        }
    }

    protected void checkString(String str, String msg) {
        checkNull(str, msg);
        if (str.trim().isEmpty()) {
            throw new CheckException(msg);
        }
    }

    protected void checkTime(long time1, long time2, String msg) {
        if (time1 > time2) {
            throw new CheckException(msg);
        }
    }

    protected void checkPen(Float pensionPrice, String msg) {
        if (pensionPrice < 0) {
            throw new CheckException(msg);
        }
    }

    protected void checkPrice(BigDecimal pensionPrice, String msg) {
        if (pensionPrice.compareTo(new BigDecimal(0)) == -1) {
            throw new CheckException(msg);
        }
    }

    protected void errorMsg(String msg) {
        throw new CheckException(msg);
    }


    public void canDelete(Serializable id) {

    }

    @Override
    public boolean has(T t) {
        return getDao().has(t);
    }

    @Override
    public List<T> findByExample(T example) {
        return getDao().findByExample(example);
    }

    /**
     * 字符串全匹配
     *
     * @param example
     * @return
     */
    @Override
    public List<T> findByExampleExact(T example) {
        return getDao().findByExample(example, MatchMode.EXACT);
    }

    @Override
    public void getPageByExample(T query, Page<T> page) {
        List<T> list = getDao().findByExample(query, page.getPageCurrent(), page.getPageSize());
        long count = getDao().countByExample(query);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void getPageByExampleNe(T query, Page<T> page, Map<String, Object> neqProperty) {
        List<T> list = getDao().findByExampleNeProperty(query, page.getPageCurrent(), page.getPageSize(), neqProperty);
        long count = getDao().countByExampleNeProperty(query, neqProperty);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public List<T> findByExampleOrderDesc(T example,String order){

        return getDao().findByExampleDESC(example,order);
    }


}

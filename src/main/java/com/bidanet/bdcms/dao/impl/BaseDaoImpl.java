package com.bidanet.bdcms.dao.impl;


import com.bidanet.bdcms.dao.Dao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by avatek on 2015/11/2 0002.
 */
public class BaseDaoImpl<T> implements Dao<T> {
    protected Logger logger=Logger.getLogger(this.getClass());
    protected   Class<T> clazz;

    public BaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
        System.out.println("DAO的真实实现类是：" + this.clazz.getName());
    }
    /**
     * 向DAO层注入SessionFactory
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 获取当前工作的Session
     */
    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }


    @Override
    public T load(Serializable id) {
        return getSession().load(clazz, id);
    }

    @Override
    public T get(Serializable id) {
        T t = getSession().get(clazz, id);

        return t;
    }



    @Override
    public void persist(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void save(T entity) {
        Serializable save = getSession().save(entity);
//        return (PK) save;
    }

    @Override
    public void update(T entity) {
        getSession().update(entity);
        flush();
    }

    @Override
    public T saveBack(T entity) {
        getSession().save(entity);
        return entity;
    }

    @Override
    public  T updateBack(T entity){
        update(entity);
        return entity;
    }

    @Override
    public List<T> findAll() {
        return getSession().createCriteria(clazz).list();
    }

    /**
     * 求 数量
     * @return
     */
    @Override
    public int count() {
        return (int) getSession().createQuery(
                "select count(id) from "+clazz.getSimpleName())
                .uniqueResult();
    }


    @Override
    public List<T> findByHql(String hql, Object... params) {
        Query query = getQuery(hql, params);
        return query.list();
//        return null;
    }

    private Query getQuery(String hql, Object[] params) {
        Query query = this.getSession().createQuery(hql);
        for (int i = 0; params != null && i < params.length; i++) {
            query.setParameter(i, params[i]);
        }

        return query;
    }

    public List<T> findByHqlPage(String hql, int pageNo, int pageSize, Object... params) {
        Query query = getQuery(hql, params);

        query.setFirstResult((pageNo-1)*pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }


    @Override
    public void delete(Serializable id) {
        T load = getSession().load(clazz, id);
        getSession().delete(load);
        getSession().flush();
    }

    @Override
    public List<T> findByExample(T example){
        return findByExample(example,MatchMode.ANYWHERE);
    }

    @Override
    public List<T> findByExample(T example,String order){
        List list =  getSession().createCriteria(clazz)

                .add(Example.create(example)).addOrder(Order.asc(order)).list();

        return list;
    }

    @Override
    public List<T> findByExample(T example, MatchMode matchMode){

        List list = getSession().createCriteria(clazz)
                .add(Example.create(example)
                        .enableLike(matchMode)).list();
        return list;
    }

    public List<T> findByExampleNoLike(T example){
        List list = getSession().createCriteria(clazz)
                .add(Example.create(example)).list();
        return list;
    }

    @Override
    public List<T> findByExample(T example, int pageNo, int pageSize){
        return findByExample(example, pageNo, pageSize,"id");
    }

    public List<T> findByExample(T example, int pageNo, int pageSize,String order){

        return findByExample(example, pageNo, pageSize, Order.desc(order));
    }

    @Override
    public List<T> findByExampleOrderAsc(T example, int pageNo, int pageSize, String order) {
        return findByExample(example, pageNo, pageSize, Order.asc(order));
    }


    @Override
    public List<T> findByExampleNeProperty(T example, int pageNo, int pageSize, Map<String, Object> neqProperty, Order order){
        Criteria criteria = createExample(example, pageNo, pageSize, order);
        for (String key : neqProperty.keySet()) {
            criteria.add(Restrictions.ne(key,neqProperty.get(key)));
        }
        return criteria.list();
    }


    @Override
    public List<T> findByExampleNeProperty(T example, int pageNo, int pageSize, Map<String, Object> neqProperty){
        return findByExampleNeProperty(example, pageNo, pageSize, neqProperty,Order.desc("id"));
    }


    @Override
    public long countByExampleNeProperty(T example, Map<String, Object> neqProperty){
        Criteria criteria = getSession().createCriteria(clazz)
                .add(Example.create(example).enableLike(MatchMode.ANYWHERE))
                .setProjection(Projections.count("id"));
        for (String key : neqProperty.keySet()) {
            criteria.add(Restrictions.ne(key,neqProperty.get(key)));
        }
        return (long)criteria.uniqueResult();
    }


    @Override
    public List<T> findByExample(T example, int pageNo, int pageSize, Order order){
        List list = createExample(example, pageNo, pageSize, order)
                .list();
        return list;
    }
    public List<T> findByExampleEq(T example, int pageNo, int pageSize, Order order){
        Criteria criteria = createExampleNoLike(example, pageNo, pageSize, order);
        List list = criteria.list();
        return list;
    }

    Criteria createExample(T example, int pageNo, int pageSize, Order order) {
        Criteria criteria = getSession().createCriteria(clazz)
                .add(Example.create(example).enableLike(MatchMode.ANYWHERE));
        return createPageOrder(criteria,pageNo,pageSize,order);
    }

    private Criteria createExampleNoLike(T example, int pageNo, int pageSize, Order order) {
        Criteria criteria = getSession().createCriteria(clazz)
                .add(Example.create(example));
        return createPageOrder(criteria,pageNo,pageSize,order);
    }

    private Criteria createPageOrder(Criteria criteria,int pageNo, int pageSize, Order order){
        return criteria.setFirstResult((pageNo-1) * pageSize)
                .setMaxResults(pageSize)
                .addOrder(order);
    }

    @Override
    public long countByExample(T example){
        return (long) getSession().createCriteria(clazz)
                .add(Example.create(example).enableLike(MatchMode.ANYWHERE))
                .setProjection(Projections.count("id")).uniqueResult();
    }

    @Override
    public boolean has(T example){
        long count = countByExample(example);
        return count>0;
    }
    @Override
    public void flush() {
        getSession().flush();
    }

    @Override
    public int execUpdateSQL(String sql) {
        return getSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public List<T> findByExampleDESC(T example,String order){
        List list =  getSession().createCriteria(clazz)

                .add(Example.create(example)).addOrder(Order.desc(order)).list();

        return list;
    }

//    public int remove(QueryParams... params) {
//        StringBuffer hql = new StringBuffer("delete from ")
//                .append(getPersistentClass().getName());
//        hql.append(" where 1=1 ");
//        getQueryHql(hql, params);
//
//        Session session = getSession();
//        Query query = session.createQuery(hql.toString());
//        if (params != null){
//            int i = 0;
//            for (QueryParams param : params){
//
//                String val = param.getField() + i;
//
//                if (param == null || param.getValue() == null)
//                    continue;
//                else if (param.getValue() instanceof Collection<?>)
//                    query.setParameterList(val,
//                            (Collection<?>) param.getValue());
//                else
//                    query.setParameter(val, param.getValue());
//                i++;
//            }
//        }
//
//        int res = query.executeUpdate();
//        return res;
//    }
}

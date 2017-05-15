package com.bidanet.bdcms.dao;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xuejike on 2015/10/31.
 */
@Transactional
public interface Dao<T> {


    T load(Serializable id);

    T get(Serializable id);



    void persist(T entity);

    void save(T entity);
    void update(T entity);

    T saveBack(T entity);

    T updateBack(T entity);

    List<T> findAll();
    int count();
    List<T> findByHql(String hql, Object... params);
    List<T> findByHqlPage(String hql, int pageNo, int pageSize, Object... params) ;
    List<T> findByExample(T example);
    List<T> findByExample(T example,String order);
    List<T> findByExample(T example, MatchMode matchMode);

    List<T> findByExample(T example, int pageNo, int pageSize);
    List<T> findByExample(T example, int pageNo, int pageSize, String order);

    List<T> findByExampleOrderAsc(T example, int pageNo, int pageSize, String order);

    List<T> findByExampleNeProperty(T example, int pageNo, int pageSize, Map<String, Object> neqProperty, Order order);

    List<T> findByExampleNeProperty(T example, int pageNo, int pageSize, Map<String, Object> neqProperty);

    long countByExampleNeProperty(T example, Map<String, Object> neqProperty);

    List<T> findByExample(T example, int pageNo, int pageSize, Order order);

    long countByExample(T example);

    void delete(Serializable id);

    boolean has(T example);

    void flush();
    int execUpdateSQL(String sql);

    List<T> findByExampleDESC(T example,String order);

    /**
     * 按条件删除对象
     *
     * @param params
     * @return
     */
//    public int remove(QueryParams... params);
}

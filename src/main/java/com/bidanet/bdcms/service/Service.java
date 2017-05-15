package com.bidanet.bdcms.service;



import com.bidanet.bdcms.vo.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xuejike on 2015/10/31.
 */
public interface Service<T> {


     T load(Serializable id);
     T get(Serializable id);


     void query(T entity, Page<T> page);
     List<T> query(T entity);

     void insertT(T t);
     void updateT(T t);

     T updateBack(T t);

     List<T> getList();

     void deleteByIdT(Serializable id);

     void deleteAllT();

     long count();

     boolean has(T t);

     List<T> findByExample(T example);

     List<T> findByExampleOrderDesc(T example,String order);


     List<T> findByExampleExact(T example);

     void getPageByExample(T query, Page<T> page);

     void getPageByExampleNe(T query, Page<T> page, Map<String, Object> neqProperty);



}

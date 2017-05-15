package com.bidanet.bdcms.service.examBusiness.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.examBusiness.ExamAreaDao;
import com.bidanet.bdcms.entity.ExamArea;
import com.bidanet.bdcms.service.examBusiness.ExamAreaService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * 体检登记. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-28 11:00:09
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author zhangbinbin
 * @version 1.0.0
 */
@Service("examAreaService")
public class ExamAreaServiceImpl extends BaseServiceImpl<ExamArea> implements ExamAreaService {

    @Autowired
    private ExamAreaDao examAreaDao;

    @Override
    protected Dao getDao() {
        return examAreaDao;
    }

    /**
     * 新修改：全部、疾控、下面按照code排序
     * 在名称后面增加code
     * @return
     */
    @Override
    public List<ExamArea> getRootExamArea() {

        ExamArea examArea = new ExamArea();

        List<ExamArea> examAreaList = examAreaDao.findByExample(examArea,"areaCode");

        List<ExamArea> examNewList = new LinkedList<>();

        ExamArea jkExamArea = new ExamArea();

        //整合数据
        for (Iterator<ExamArea> it = examAreaList.iterator(); it.hasNext();) {

            ExamArea examArea1 = it.next();

            String areaCode = examArea1.getAreaCode().toString();
            examArea1.setAreaName(examArea1.getAreaName()+"--"+areaCode.substring(1,3));

            if (examArea1.getAreaCode() == 132) {
                jkExamArea = examArea1;
                it.remove();

            }

        }


        examNewList.add(jkExamArea);
        //剔除数据
        for(ExamArea examArea2 :examAreaList ){

            examNewList.add(examArea2);
        }


        return examNewList;
    }

    public  static void main(String[] args){
        String code = "101";
        System.out.print(code.substring(1,3));

    }
}

package com.bidanet.bdcms.service.businessSetting.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamUnitDao;
import com.bidanet.bdcms.entity.ExamUnit;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.businessSetting.ExamUnitService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.vo.Page;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
@Service("examUnitService")
public class ExamUnitServiceImpl extends BaseServiceImpl<ExamUnit> implements ExamUnitService {
    @Autowired
    private ExamUnitDao examUnitDao;
    @Override
    protected Dao getDao() {
        return examUnitDao;
    }

    @Override
    public void getExamUnitList(ExamUnit examUnit, Page<ExamUnit> page){
        List<ExamUnit> list = examUnitDao.findByExample(examUnit, page.getPageCurrent(), page.getPageSize());
        long count = examUnitDao.countByExample(examUnit);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateExamUnitT(ExamUnit examUnit) {
        checkString(examUnit.getUnitName(),"请填写体检单位名称！");
        ExamUnit examUnit1 = examUnitDao.get(examUnit.getId());
        checkNull(examUnit1,"没有找到此体检单位！");
        examUnit1.setUnitName(examUnit.getUnitName());
        examUnitDao.update(examUnit1);
    }

    @Override
    public void addExamUnitT(ExamUnit examUnit) {
        checkString(examUnit.getUnitName(),"请填写体检单位名称！");
        ExamUnit unit = new ExamUnit();
        String unitName = examUnit.getUnitName();
        //工作单位必须已“常熟市”三个字开头
//        if(unitName.length()>=3) {
//            String workBegin = unitName.substring(0, 3);
//            if (!workBegin.equals("常熟市")) {
//                unitName = "常熟市" + unitName;
//            }
//        }else{
//            unitName = "常熟市" + unitName;
//        }
        unit.setUnitName(unitName);
        List<ExamUnit> units = examUnitDao.findByExample(unit, MatchMode.EXACT);
        if (units.size()>0) {
            throw new CheckException("已有相同体检单位");
        } else {
            Date date =  new Date();
            examUnit.setUnitName(unitName);
            examUnit.setCreateTime(date.getTime());
            examUnitDao.save(examUnit);
        }

    }

    @Override
    public void addExamUnitByThirdT(ExamUnit examUnit) {
//        checkString(examUnit.getUnitName(),"请填写体检单位名称！");
        ExamUnit unit = new ExamUnit();
        String unitName = examUnit.getUnitName();
//        //工作单位必须已“常熟市”三个字开头
//        if(unitName.length()>=3) {
//            String workBegin = unitName.substring(0, 3);
//            if (!workBegin.equals("常熟市")) {
//                unitName = "常熟市" + unitName;
//            }
//        }else{
//            unitName = "常熟市" + unitName;
//        }
        unit.setUnitName(unitName);
        List<ExamUnit> units = examUnitDao.findByExample(unit, MatchMode.EXACT);
        if (units.size()>0) {
            //微信端已经自助机，有重复单位不做处理
        } else {
            Date date =  new Date();
            examUnit.setUnitName(unitName);
            examUnit.setCreateTime(date.getTime());
            examUnitDao.save(examUnit);
        }

    }

    @Override
    public JSONObject checkUnitName(String unitName) {
        JSONObject json =new JSONObject();
        ExamUnit examUnit = new ExamUnit();
        examUnit.setUnitName(unitName);
        List<ExamUnit> examUnitList = examUnitDao.findByExample(examUnit, MatchMode.EXACT);
        if(examUnitList!=null&&examUnitList.size()>0){
            json.put("error","体检机构名称重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }




}

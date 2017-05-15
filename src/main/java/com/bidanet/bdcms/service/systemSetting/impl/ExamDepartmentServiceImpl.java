package com.bidanet.bdcms.service.systemSetting.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.ExamDepartmentDao;

import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamDepartment;

import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamDepartmentService;
import com.bidanet.bdcms.vo.Page;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*/
@Service("examDepartmentService")
public class ExamDepartmentServiceImpl extends BaseServiceImpl<ExamDepartment> implements ExamDepartmentService {
    @Autowired
    private ExamDepartmentDao departmentDao;
    @Override
    protected Dao getDao() {
        return departmentDao;
    }


    /**
     * 查询
     * @param department
     * @param page
     */
    @Override
    public void getExamDepartmentList(ExamDepartment department, Page<ExamDepartment> page) {

        List<ExamDepartment> list = departmentDao.findByExample(department, page.getPageCurrent(), page.getPageSize());

        long count = departmentDao.countByExample(department);
        page.setList(list);
        page.setTotal(count);

    }

    /**
     * 更新科室
     * @param examDepartment
     */
    @Override
    public void updateDepartmentT(ExamDepartment examDepartment) {

        checkString(examDepartment.getDepartmentName(),"请填写科室名称！");

        ExamDepartment department = departmentDao.get(examDepartment.getId());

        department.setDepartmentName(department.getDepartmentName());

        departmentDao.update(department);

    }

    /**
     * 保存科室
     * @param examDepartment
     */
    @Override
    public void addDepartmentT(ExamDepartment examDepartment) {

        checkString(examDepartment.getDepartmentName(),"请填写科室名称！");

        departmentDao.save(examDepartment);

    }

    /**
     * 检查科室姓名
     * @param departmentName
     * @return
     */
    @Override
    public JSONObject checkDepartmentName(String departmentName) {
        JSONObject json =new JSONObject();
        ExamDepartment examDepartment = new ExamDepartment();
        examDepartment.setDepartmentName(departmentName);

        List<ExamDepartment> examDepartmentList = departmentDao.findByExample(examDepartment,MatchMode.EXACT);

        if(examDepartmentList!=null&&examDepartmentList.size()>0){
            json.put("error","科室名称重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }

    /**
     * 获取所有科室
     * @return
     */
    @Override
    public List<ExamDepartment> getAllDepartment() {
        List<ExamDepartment> departmentList = departmentDao.findAll();

        return departmentList;
    }
}

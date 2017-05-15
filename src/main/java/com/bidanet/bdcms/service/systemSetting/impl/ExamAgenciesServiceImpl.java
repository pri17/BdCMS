package com.bidanet.bdcms.service.systemSetting.impl;

import com.alibaba.fastjson.JSONObject;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamDoctorDao;
import com.bidanet.bdcms.dao.examBusiness.ExamAreaDao;
import com.bidanet.bdcms.dao.systemSetting.ExamAgenciesDao;
import com.bidanet.bdcms.dao.systemSetting.ExamProjectDao;
import com.bidanet.bdcms.entity.ExamAgencies;
import com.bidanet.bdcms.entity.ExamArea;
import com.bidanet.bdcms.entity.ExamDoctor;
import com.bidanet.bdcms.entity.ExamProject;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamAgenciesService;
import com.bidanet.bdcms.vo.Page;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xuejike on 2016-05-24.
 */
@Service("examAgenciesService")
public class ExamAgenciesServiceImpl extends BaseServiceImpl<ExamAgencies> implements ExamAgenciesService {
    @Autowired
    private ExamAgenciesDao examAgenciesDao;
    @Override
    protected Dao getDao() {
        return examAgenciesDao;
    }

    @Autowired
    private ExamAreaDao examAreaDao;

    @Autowired
    private ExamProjectDao examProjectDao;

    @Autowired
    private ExamDoctorDao examDoctorDao;

    @Override
    public void getExamAgenciesList(ExamAgencies examAgencies, Page<ExamAgencies> page){
        List<ExamAgencies> list = examAgenciesDao.findByExampleOrderAsc(examAgencies, page.getPageCurrent(), page.getPageSize(),"agenciesCode");

        for (ExamAgencies examAgencies1:list){

            ExamArea examArea = examAreaDao.get(examAgencies1.getAreaId());

            examAgencies1.setAreaName(examArea.getAreaName());
        }

        long count = examAgenciesDao.countByExample(examAgencies);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateAgenciesT(ExamAgencies examAgencies) {
        checkString(String.valueOf(examAgencies.getAgenciesCode()),"请填写机构代码！");
        checkString(examAgencies.getAgenciesName(),"请填写机构名称！");
        if (examAgencies.getAreaId()==null){
            errorMsg("请选择所在地区！");
        }
        ExamAgencies agencies = examAgenciesDao.get(examAgencies.getId());
        checkNull(agencies,"没有找到此机构！");
        agencies.setAgenciesCode(examAgencies.getAgenciesCode());
        agencies.setAgenciesName(examAgencies.getAgenciesName());
        agencies.setAreaId(examAgencies.getAreaId());
        examAgenciesDao.update(agencies);
    }

    @Override
    public ExamAgencies addAgenciesT(ExamAgencies examAgencies) {
        checkString(String.valueOf(examAgencies.getAgenciesCode()),"请填写机构代码！");
        checkString(examAgencies.getAgenciesName(),"请填写体检机构！");
        if (examAgencies.getAreaId()==null){
            errorMsg("请选择所在地区！");
        }
        examAgencies =  examAgenciesDao.saveBack(examAgencies);

        return examAgencies;
    }

    /**
     * 检测机构代码是否已使用
     * @param agenciesCode
     * @return
     */
    @Override
    public JSONObject checkAgenciesCode(String agenciesCode) {
        JSONObject json =new JSONObject();
        ExamAgencies examAgencies = new ExamAgencies();
        examAgencies.setAgenciesCode(Integer.valueOf(agenciesCode));

        List<ExamAgencies> examAgenciesList = examAgenciesDao.findByExample(examAgencies, MatchMode.EXACT);
        if(examAgenciesList!=null&&examAgenciesList.size()>0){
            json.put("error","机构代码重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }

    @Override
    public JSONObject checkAgenciesName(String agenciesName) {
        JSONObject json =new JSONObject();
        ExamAgencies examAgencies = new ExamAgencies();
        examAgencies.setAgenciesName(agenciesName);
        List<ExamAgencies> examAgenciesList = examAgenciesDao.findByExample(examAgencies,MatchMode.EXACT);
        if(examAgenciesList!=null&&examAgenciesList.size()>0){
            json.put("error","体检机构名称重复，请修改！");
        }else{
            json.put("ok","可以使用！");
        }
        return json;
    }

        @Override
        public  List<ExamAgencies> getAllAgencies() {

            List<ExamAgencies> agenciesList = examAgenciesDao.findAll();

            return agenciesList;

        }

    @Override
    public ExamAgencies findAgenciesById(Long id) {
        return examAgenciesDao.get(id);
    }

    @Override
    public void addAgenciesDoctor(ExamAgencies examAgencies) {

        ExamDoctor examDoctor = new ExamDoctor();
        examDoctor.setAgenciesId(examAgencies.getId());
        examDoctor.setLevel(1);
        examDoctor.setParentId(0L);
        examDoctor.setAreaId(examAgencies.getAreaId());
        examDoctor.setCreateTime(new Date().getTime());

        examDoctor = examDoctorDao.saveBack(examDoctor);

        if(examDoctor.getId()!=0){

            //更新code
            examDoctor.setCode(examDoctor.getId().intValue());

            List<ExamProject> examProjectList = examProjectDao.findAll();

            for(ExamProject examProject : examProjectList){
                //先添加父类 再循环添加
                ExamDoctor childExamDoctor = new ExamDoctor();
                childExamDoctor.setAgenciesId(examDoctor.getAgenciesId());
                childExamDoctor.setCreateTime(new Date().getTime());
                childExamDoctor.setLevel(2);
                childExamDoctor.setParentId(examDoctor.getId());
                childExamDoctor.setCode(examDoctor.getId().intValue());
                childExamDoctor.setAreaId(examDoctor.getAreaId());

                //医生数据
                childExamDoctor.setProjectId(examProject.getId());
                childExamDoctor.setProjectName(examProject.getProjectName());


                childExamDoctor = examDoctorDao.saveBack(childExamDoctor);

            }


        }



    }


}

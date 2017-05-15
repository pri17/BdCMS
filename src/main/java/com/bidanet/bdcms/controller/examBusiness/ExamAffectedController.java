package com.bidanet.bdcms.controller.examBusiness;/**
 * Created by MyPC on 2016/10/27.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.FileOutput;
import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.*;
import com.bidanet.bdcms.service.examBusiness.*;
import com.bidanet.bdcms.vo.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 疫区&疫区体检人员管理.
 * 类详细说明.
 * Copyright: Copyright (c) 16-10-27 9:09
 * Company: 苏州必答网络科技有限公司
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("affected")
@RequestMapping("/admin/examBusiness/examAffected")
public class ExamAffectedController extends AdminBaseController{

    @Autowired
    private ExamMemberService examMemberService;
    @Autowired
    private ExamMemberExamService examMemberExamService;
    @Autowired
    private ExamAffectedService examAffectedService;
    @Autowired
    private ExamAreaCodeService examAreaCodeService;
    @Autowired
    private ExamBlodService examBlodService;

    /**
     * 疫区人员页面
     */
    @RequestMapping("/index")
    public void index(
            String beginTime,String endTime,String areaId,String examAffectedIsQuery,
            @ModelAttribute Page<ExamMemberExam> page,
             Model model) throws ParseException {

        User user = uc.getUser();

        if(areaId == null){
            areaId = user.getAreaId().toString();
        }

    if (StringUtils.isNotEmpty(examAffectedIsQuery) && examAffectedIsQuery!=null){
        /**
         * 疫区范围内的体检人员信息
         */
        examMemberExamService.findAffectedExamMember(page,beginTime,endTime,areaId);

        model.addAttribute("beginTime",beginTime);
        model.addAttribute("endTime",endTime);
        model.addAttribute("areaId",areaId);
    }/*else{*/
        model.addAttribute("examAffectedIsQuery","1");
    /*}*/



    }

    /**
     * 设置疫区页面
     * 2017-01-19 新增 ：需要显示省市信息
     */
    @RequestMapping("/toSetAffected")
    public void toSetAffected(@ModelAttribute("query") ExamAffected query,@ModelAttribute Page<ExamAffected> page
    ){

        examAffectedService.findAllAffectedList(query,page);
    }

    /**
     * 添加疫区
     * @return
     */
    @RequestMapping("/setAffected")
    @ResponseBody
    public AjaxCallBack setAffected(String affectedCode){
        examAffectedService.setAffectedT(affectedCode);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

    /**
     * 删除疫区
     * @param id
     * @return     */
    @RequestMapping("/deleteAffected")
    @ResponseBody
    public AjaxCallBack deleteAffected(Long id){
        examAffectedService.deleteAffectedT(id);
        AjaxCallBack ajaxCallBack = AjaxCallBack.deleteSuccess();
        return ajaxCallBack;
    }

    /**
     * 保存疫区人员血检额外信息
     * @param ids
     * @param iha
     * @param ddia
     * @param copt
     * @param stool
     * @return
     */
    @RequestMapping("/saveAffectedMember")
    @ResponseBody
    public AjaxCallBack saveAffectedMember(String ids,String iha,String ddia,String copt,String stool){
        if(StringUtils.isBlank(ids)){
            errorMsg("请选择一项再操作");
        }
        String [] idses = ids.split(",");
        int a =idses.length;
        if(idses.length>1){
            errorMsg("只能选择一项！");
        }
        examMemberExamService.saveAffectedMemberT(idses,iha,ddia,copt,stool);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid("table_54");
        return ajaxCallBack;
    }
    /**
     * 跳转预览肠检单
     */
    @RequestMapping("/printExamAffected")
    public void printExamAffected(Model model,String ids){
        if (StringUtils.isNotBlank(ids)) {
            ids = ids.substring(0,ids.length()-1);
            List<ExamMemberExam> examMemberExams = examMemberExamService.findAffectedExamMemberByIds(ids);
            for(int i=0;i<examMemberExams.size();i++){
                ExamMember em=examMemberService.get(examMemberExams.get(i).getMemberId());
                Map map= examMemberExams.get(i).getProjectData();
                map.put("name",em.getName());
                map.put("sex",em.getSex());
                map.put("birthday",em.getBirthday());
                map.put("workUnit",em.getWorkUnit());
                map.put("IdCardAddress",em.getIdCardAddress());

                //Gcx
                //获取该用户的疫区code对应的具体地址
                String affectedCode = examMemberExams.get(i).getIdCardNum().substring(0, 6);

                ExamAreaCode examAreaCode = examAreaCodeService.get(Long.parseLong(affectedCode));

                ExamAreaCode examAreaParentTwo = examAreaCodeService.get(examAreaCode.getPid());

                ExamAreaCode examAreaParentOne = examAreaCodeService.get(examAreaParentTwo.getPid());

                String affectedName = examAreaParentOne.getName() + examAreaParentTwo.getName() + examAreaCode.getName();

                examMemberExams.get(i).setAffectedCodeName(affectedName);

                //获取该用户的血检流水号
                List<ExamBlod> examBlodList = new ArrayList<>();
                ExamBlod examBlod = new ExamBlod();
                examBlod.setMemberExamId(examMemberExams.get(i).getId());

                examBlodList = examBlodService.findByExample(examBlod);

                if (examBlodList.size() > 0) {

                    examBlod = examBlodList.get(0);
                    examMemberExams.get(i).setBlodTestNumber(examBlod.getTestNumber());

                }
            }
            model.addAttribute("examMemberExams", examMemberExams);
        }
    }

    @RequestMapping(value="/exportExcel")
    @ResponseBody
    public FileOutput exportExcel(HttpServletRequest request, HttpServletResponse response, Model model,String beginTime,String endTime,String areaId ) throws Exception {
        HSSFWorkbook hw = examAffectedService.downAffectedExcel(beginTime,endTime,areaId);
        String path = SpringWebTool.getRealPath("/temp/");
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String filename = new Date().getTime() + "yiqu.xls";
        File file = new File(path + "/" + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);

        hw.write(stream);
        stream.flush();
        stream.close();
        return new FileOutput(file, filename);



    }

}

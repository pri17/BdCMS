package com.bidanet.bdcms.controller.examBusiness;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.bean.CompanyPeopleInform;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.exception.CheckException;
import com.bidanet.bdcms.service.examBusiness.ExamNumberService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by asus on 2017/3/7.
 */
@Controller("examCompany")
@RequestMapping("/admin/examBusiness/examCompany")
public class ExamCompanyController extends AdminBaseController {
    @Autowired
    ExamNumberService examNumberService;
    @RequestMapping("/index")
    public void index(Model model,HttpServletRequest request){
        String path=request.getServletPath();
        model.addAttribute("path","/assets/企业登记模板.xlsx");

    }

    @RequestMapping("/importCompanyPeopleInform")
    @ResponseBody
    public AjaxCallBack importCompanyPeopleInform(@RequestParam("file") MultipartFile files) {

        ImportParams importParam = new ImportParams();
        List<CompanyPeopleInform> companyPeopleInformList = null;
        try {
            companyPeopleInformList = ExcelImportUtil.importExcel(files.getInputStream(), CompanyPeopleInform.class, importParam);
            examNumberService.addExamForCompanyT(companyPeopleInformList);
            AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
            ajaxCallBack.setMessage("企业数据导入成功");
            ajaxCallBack.setDivid("gradeStudentList");
            ajaxCallBack.setTabid("table_66");
            return ajaxCallBack;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CheckException(e.getMessage());
        }
    }

}

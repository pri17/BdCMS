package com.bidanet.bdcms.controller.businessSetting;/**
 * Created by Administrator on 2016/10/12 0012.
 */

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamCategory;
import com.bidanet.bdcms.entity.ExamCategoryPackage;
import com.bidanet.bdcms.entity.ExamPackage;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryPackageService;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 行业管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 16-10-12 14:42
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author cf
 * @version 1.0.0
 */

@Controller("examCategory")
@RequestMapping("/admin/businessSetting/examCategory")
public class ExamCategoryController extends AdminBaseController{

    @Autowired
    private ExamCategoryService categoryService;
    @Autowired
    private ExamCategoryPackageService categoryPackageService;

    private String tableId = "";

    /**
     * 查询行业分类数据
     * @param query
     * @param page
     */
    @RequestMapping("/index")
    public void index    (
            Model model,
            @ModelAttribute("query") ExamCategory query,
            @ModelAttribute Page<ExamCategory> page,String tabid){
//        categoryService.getPageByExample(query,page);
//        List<ExamCategory>  examCategories = categoryService.getList();
        List<ExamCategory>  examCategories = categoryService.getAllCategory();
        model.addAttribute("examCategories",examCategories);

        tableId = tabid;
    }

    /**
     * 跳转到添加行业
     */
    @RequestMapping("/toAddCategory")
    public void toAddCategory(){

    }

    /**
     * 添加行业
     */
    @RequestMapping("/addCategory")
    @ResponseBody
    public AjaxCallBack addCategory(ExamCategory examCategory,String categoryNameSon,Long code){
        categoryService.addCategoryT(examCategory,categoryNameSon,code);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转去修改行业
     * @param id
     * @param model
     */
    @RequestMapping("/toUpCategory")
    public void toUpCategory(Long id,Model model){
        ExamCategory examCategory = categoryService.get(id);
        model.addAttribute("examCategory",examCategory);
    }

    /**
     * 修改行业
     * @param examCategory
     * @return
     */
    @RequestMapping("/upCategory")
    @ResponseBody
    public AjaxCallBack upCategory(ExamCategory examCategory){
        categoryService.upCategoryT(examCategory);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 跳转到添加子级行业
     */
    @RequestMapping("/toAddCategorySon")
    public void toAddCategorySon(Long id,Model model){
        ExamCategory examCategory = categoryService.get(id);
        model.addAttribute("examCategory",examCategory);
    }

    /**
     * 添加子级行业
     */
    @RequestMapping("/addCategorySon")
    @ResponseBody
    public AjaxCallBack addCategorySon(ExamCategory examCategory){
        categoryService.addCategorySonT(examCategory);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 删除行业
     * @param id
     * @return
     */
    @RequestMapping("/deleteCategory")
    @ResponseBody
    public AjaxCallBack deleteCategory(Long id){
        categoryService.deleteCategoryT(id);
        AjaxCallBack ajaxCallBack = AjaxCallBack.deleteSuccess();
        return ajaxCallBack;
    }

    /**
     * 跳转去修改子行业类别
     * @param id
     * @param model
     */
    @RequestMapping("/toUpCategorySon")
    public void toUpCategorySon(Long id,Model model){
        ExamCategory examCategory = categoryService.get(id);
        model.addAttribute("examCategory",examCategory);
    }

    /**
     * 修改子行业类别
     * @param examCategory
     * @return
     */
    @RequestMapping("/upCategorySon")
    @ResponseBody
    public AjaxCallBack upCategorySon(ExamCategory examCategory){
        categoryService.upCategorySonT(examCategory);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    /**
     * 行业对应体检套餐页面
     * @param id
     * @param model
     */
    @RequestMapping("/toSelectPackage")
    public void toSelectPackage(Long id,Model model){
        ExamCategory examCategory = categoryService.get(id);
        ExamCategoryPackage categoryPackage = new ExamCategoryPackage();
        categoryPackage.setCategoryId(id);
        List<ExamCategoryPackage> categoryPackageList = categoryPackageService.findByExample(categoryPackage);
        if(categoryPackageList!=null&&categoryPackageList.size()>0){
            model.addAttribute("categoryPackageId",categoryPackageList.get(0).getId());
            model.addAttribute("oldPackageId",categoryPackageList.get(0).getPackageId());
        }else{
            model.addAttribute("categoryPackageId",null);
        }
        model.addAttribute("examCategory",examCategory);
    }

    /**
     * 选择行业对应体检套餐(首次为新增)
     * @param categoryId
     * @param packageId
     * @return
     */
    @RequestMapping("/selectPackage")
    @ResponseBody
    public AjaxCallBack selectPackage(Long categoryId, Long packageId,Long categoryPackageId){
        categoryService.selectPackageT(categoryId,packageId,categoryPackageId);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        return ajaxCallBack;
    }

}

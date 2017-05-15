package com.bidanet.bdcms.service.businessSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryDao;
import com.bidanet.bdcms.dao.businessSetting.ExamCategoryPackageDao;
import com.bidanet.bdcms.entity.ExamCategory;

import com.bidanet.bdcms.entity.ExamCategoryPackage;
import com.bidanet.bdcms.service.businessSetting.ExamCategoryService;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


/**
*
*/
@Service("examCategoryService")
public class ExamCategoryServiceImpl extends BaseServiceImpl<ExamCategory> implements ExamCategoryService {
    @Autowired
    private ExamCategoryDao categoryDao;
    @Autowired
    private ExamCategoryPackageDao categoryPackageDao;
    @Override
    protected Dao getDao() {
        return categoryDao;
    }



    /**
     * 添加行业
     * @param examCategory
     * @param categoryNameSon
     * @param code
     */
    @Override
    public void addCategoryT(ExamCategory examCategory, String categoryNameSon, Long code) {
        /**
         * 保存父级分类
         */
        checkString(examCategory.getCategoryName(),"请输入行业类别！");
        checkString(String.valueOf(examCategory.getCode()),"请输入排序号！");
        checkString(categoryNameSon,"请输入行业名称！");
        examCategory.setLevel(1);
        examCategory.setParentId(0L);
        examCategory.setCode(code);
        List<ExamCategory> categoryList = categoryDao.findAll();
        if(categoryList!=null&&categoryList.size()>0){
            for(ExamCategory c:categoryList){
                if(c.getCategoryName().equals(examCategory.getCategoryName())){
                    errorMsg("行业名称重复，请重新输入！");
                }

                if(c.getCode()==code){
                    errorMsg("排序号不能重复，请重新输入整数！");
                }
            }
        }

        categoryDao.save(examCategory);
        /**
         * 保存子分类
         */
        ExamCategory examCategorySon = new ExamCategory();
        examCategorySon.setCategoryName(categoryNameSon);
        examCategorySon.setLevel(2);
        examCategorySon.setParentId(examCategory.getId());
        examCategorySon.setCode(examCategory.getCode());
        categoryDao.save(examCategorySon);
    }

    /**
     *修改行业
     * @param examCategory
     */
    @Override
    public void upCategoryT(ExamCategory examCategory) {
        checkString(examCategory.getCategoryName(),"请输入行业类别！");
        checkString(String.valueOf(examCategory.getCode()),"请输入排序号！");
        ExamCategory upExamCategory = categoryDao.get(examCategory.getId());
        checkNull(upExamCategory,"没有找到此行业类别！");
        List<ExamCategory> categoryList = categoryDao.findAll();


        //移除当前行业实体
        categoryList.remove(upExamCategory);



 /*       Iterator<ExamCategory> it = categoryList.iterator();
        while(it.hasNext()){
            ExamCategory x = it.next();
            if(x.getCode() == upExamCategory.getCode()){
                it.remove();
            }

        }*/

        if(categoryList!=null&&categoryList.size()>0){
            for(ExamCategory c:categoryList){
                if(c.getCategoryName().equals(examCategory.getCategoryName())){
                    errorMsg("行业名称重复，请重新输入！");
                }

                //去除排序号检查
/*                if(c.getCode()==examCategory.getCode()){
                    errorMsg("排序号不能重复，请重新输入整数！");
                }*/
            }
        }


        upExamCategory.setCategoryName(examCategory.getCategoryName());
        upExamCategory.setCode(examCategory.getCode());
        categoryDao.update(upExamCategory);

        ExamCategory sonCategory = new ExamCategory();
        sonCategory.setParentId(examCategory.getId());
        List<ExamCategory> sonCategoryList = categoryDao.findByExample(sonCategory);
        if(sonCategoryList!=null&&sonCategoryList.size()>0){
            for(ExamCategory cat:sonCategoryList){
                cat.setCode(examCategory.getCode());
                categoryDao.update(cat);
            }
        }
    }

    /**
     * 添加子行业
     * @param examCategory
     */
    @Override
    public void addCategorySonT(ExamCategory examCategory) {
        checkString(examCategory.getCategoryName(),"请输入行业名称！");
        ExamCategory examCategorySon = new ExamCategory();
        examCategorySon.setCategoryName(examCategory.getCategoryName());
        examCategorySon.setLevel(2);
        examCategorySon.setParentId(examCategory.getId());
        examCategorySon.setCode(examCategory.getCode());
        categoryDao.save(examCategorySon);
    }

    /**
     * 删除行业
     * @param id
     */
    @Override
    public void deleteCategoryT(Long id) {
        ExamCategory examCategory = categoryDao.get(id);
        checkNull(examCategory,"没有找到此行业！");
        if(examCategory.getParentId()==0){
            ExamCategory examCategorySon = new ExamCategory();
            examCategorySon.setLevel(2);
            examCategorySon.setParentId(id);
            List<ExamCategory> examCategoryList = categoryDao.findByExample(examCategorySon);
            if(examCategoryList!=null&&examCategoryList.size()>0){
                errorMsg("该行业类别下有子行业，不能删除！如要删除，请先删除子行业！");
            }
            categoryDao.delete(id);
        }else{
            categoryDao.delete(id);
        }


    }

    /**
     * 修改子行业类别
     * @param examCategory
     */
    @Override
    public void upCategorySonT(ExamCategory examCategory) {
        checkString(examCategory.getCategoryName(),"请输入行业名称！");
        ExamCategory upExamCategory = categoryDao.get(examCategory.getId());
        checkNull(upExamCategory,"没有找到此行业类别！");
        upExamCategory.setCategoryName(examCategory.getCategoryName());
        categoryDao.update(upExamCategory);
    }

    /**
     * 根据行业id查下级行业
     *
     * @param pid
     * @return
     */
    @Override
    public List<ExamCategory> getExamCategoryByPid(int level,Long pid) {
        if (pid == null || pid == 0L) {
            return new ArrayList<ExamCategory>();
        }
        ExamCategory examCategory = new ExamCategory();
        examCategory.setLevel(level);
        examCategory.setParentId(pid);
        return categoryDao.findByExample(examCategory);
    }

    @Override
    public List<ExamCategoryPackage> getExamCategoryPackageByPid(Long pid) {
        ExamCategoryPackage examCategoryPackage = new ExamCategoryPackage();
        examCategoryPackage.setCategoryId(pid);
        return categoryPackageDao.findByExample(examCategoryPackage);
    }

    @Override
    public List<ExamCategory> getRootCategory() {
        ExamCategory examCategory = new ExamCategory();
        examCategory.setLevel(1);
        List<ExamCategory> list = categoryDao.findByExample(examCategory);
        return list;
    }

    /**
     * 选择行业对应体检套餐
     * @param categoryId
     * @param packageId
     * @param categoryPackageId
     */
    @Override
    public void selectPackageT(Long categoryId, Long packageId, Long categoryPackageId) {
        if(categoryPackageId==null) {
            ExamCategoryPackage examCategoryPackage = new ExamCategoryPackage();
            examCategoryPackage.setCategoryId(categoryId);
            examCategoryPackage.setPackageId(packageId);
            categoryPackageDao.save(examCategoryPackage);
        }else{
            ExamCategoryPackage upCategory = categoryPackageDao.get(categoryPackageId);
            checkNull(upCategory,"没有找到此行业类别！");
            upCategory.setPackageId(packageId);
            categoryPackageDao.update(upCategory);
        }
    }

    /**
     * 不排序查询行业分类数据
     * @return
     */
    @Override
    public List<ExamCategory> getAllCategory() {
        List<ExamCategory> categories = categoryDao.findCategoryByExample();
        return  categories;
    }
}

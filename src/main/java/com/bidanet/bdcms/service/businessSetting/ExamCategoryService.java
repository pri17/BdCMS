package com.bidanet.bdcms.service.businessSetting;

import com.bidanet.bdcms.entity.ExamCategory;
import com.bidanet.bdcms.entity.ExamCategoryPackage;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
*
*/
public interface ExamCategoryService extends Service<ExamCategory> {

    /**
     * 添加行业
     * @param examCategory
     * @param categoryNameSon
     * @param code
     */
    void addCategoryT(ExamCategory examCategory, String categoryNameSon, Long code);

    /**
     * 修改行业
     * @param examCategory
     */
    void upCategoryT(ExamCategory examCategory);

    /**
     * 添加子级行业
     * @param examCategory
     */
    void addCategorySonT(ExamCategory examCategory);

    /**
     * 删除行业
     * @param id
     */
    void deleteCategoryT(Long id);

    /**
     *修改子行业类别
     * @param examCategory
     */
    void upCategorySonT(ExamCategory examCategory);

    List<ExamCategory> getExamCategoryByPid(int level,Long pid);

    List<ExamCategoryPackage> getExamCategoryPackageByPid(Long pid);

    List<ExamCategory> getRootCategory();

    /**
     * 选择行业对应体检套餐
     * @param categoryId
     * @param packageId
     * @param categoryPackageId
     */
    void selectPackageT( Long categoryId, Long packageId,Long categoryPackageId);

    /**
     * 不排序查询行业分类数据
     * @return
     */
    List<ExamCategory> getAllCategory();
}

package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.IndexImageDao;
import com.bidanet.bdcms.entity.ExamIndexImage;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.IndexImageService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gao on 2017/2/27.
 */
@Service
public class IndexImageServiceImpl extends BaseServiceImpl<ExamIndexImage> implements IndexImageService {

    @Autowired
    private IndexImageDao indexImageDao;

    protected Dao getDao() {
        return indexImageDao;
    }

    @Override
    public void getAllIndexImages(ExamIndexImage examIndexImage,Page<ExamIndexImage> page) {
       List<ExamIndexImage> list= indexImageDao.findByExample(examIndexImage,page.getPageCurrent(),page.getPageSize());
        long count=indexImageDao.countByExample(examIndexImage);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public void updateIndexImageT(ExamIndexImage examIndexImage) {
        checkString(examIndexImage.getUrl(),"请上传图标");
        checkString(examIndexImage.getTitle(),"请填写图标名称");
        ExamIndexImage indexImage=indexImageDao.get(examIndexImage.getId());
        indexImage.setUrl(examIndexImage.getUrl());
        indexImageDao.update(indexImage);
    }

    @Override
    public void addIndexImageT(ExamIndexImage examIndexImage) {
        checkString(examIndexImage.getUrl(),"请上传图标");
        checkString(examIndexImage.getTitle(),"请填写图标名称");
        indexImageDao.save(examIndexImage);
    }
}

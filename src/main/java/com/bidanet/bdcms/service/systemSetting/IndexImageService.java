package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamIndexImage;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

/**
 * Created by gao on 2017/2/27.
 */
public interface IndexImageService extends Service<ExamIndexImage> {
    void getAllIndexImages(ExamIndexImage examIndexImage,Page<ExamIndexImage> page);
    void updateIndexImageT(ExamIndexImage examIndexImage);
    void addIndexImageT(ExamIndexImage examIndexImage);
}

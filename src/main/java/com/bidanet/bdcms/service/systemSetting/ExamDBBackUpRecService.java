package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamDBBackUpRec;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jizhaoqun on 16/10/11.
 */
public interface ExamDBBackUpRecService  extends Service<ExamDBBackUpRec>{

    /**
     * 获取全部备份
     * @param
     */
    void getDbBackUpList(ExamDBBackUpRec examDBBackUpRec, Page<ExamDBBackUpRec> page);

    ExamDBBackUpRec findById(Long id);

    void addDBBackUp(ExamDBBackUpRec examDBBackUpRec);

    boolean backUpDb(OutputStream output, String dbName);

    boolean recoveryDb(InputStream in, String dbName);

}

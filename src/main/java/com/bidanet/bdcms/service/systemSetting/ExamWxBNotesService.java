package com.bidanet.bdcms.service.systemSetting;

import com.bidanet.bdcms.entity.ExamWxBNotes;
import com.bidanet.bdcms.service.Service;

/**
*
*/
public interface ExamWxBNotesService extends Service<ExamWxBNotes> {

    /**
     * 修改从业人员须知内容（首次为新增）
     * @param wxBNotes
     * @param upId
     */
    void upWxBNotesT(ExamWxBNotes wxBNotes, Long upId);
}

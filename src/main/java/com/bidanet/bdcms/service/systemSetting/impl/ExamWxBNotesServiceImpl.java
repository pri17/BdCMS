package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.ExamWxBNotesDao;
import com.bidanet.bdcms.entity.ExamWxBNotes;

import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamWxBNotesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
*
*/
@Service
public class ExamWxBNotesServiceImpl extends BaseServiceImpl<ExamWxBNotes> implements ExamWxBNotesService {
    @Autowired
    private ExamWxBNotesDao examWxBNotesDao;
    @Override
    protected Dao getDao() {
        return examWxBNotesDao;
    }


    /**
     * 修改从业人员须知内容（首次为新增）
     * @param wxBNotes
     * @param upId
     */
    @Override
    public void upWxBNotesT(ExamWxBNotes wxBNotes, Long upId) {
        checkString(wxBNotes.getContent(),"请输入从业人员须知内容！");
        if(upId!=null){
            ExamWxBNotes upWxBNotes = examWxBNotesDao.get(upId);
            checkNull(upWxBNotes,"没有找到此人员须知内容！");
            upWxBNotes.setContent(wxBNotes.getContent());
            examWxBNotesDao.update(upWxBNotes);
        }else{
            wxBNotes.setCreateTime(new Date().getTime());
            examWxBNotesDao.save(wxBNotes);
        }
    }
}

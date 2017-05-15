package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamMember;

import java.util.List;

/**
 * Created by jizhaoqun on 16/10/14.
 */
public interface ExamAddDao extends Dao<ExamMember> {
    List<ExamMember> getExamMemberByIdCardNum(String idCardNum);
}

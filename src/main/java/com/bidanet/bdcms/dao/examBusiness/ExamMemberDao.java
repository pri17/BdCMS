package com.bidanet.bdcms.dao.examBusiness;

import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.ExamMember;

import java.util.List;

/**
* ExamMember DAO
*/
public interface ExamMemberDao extends Dao<ExamMember> {
    public List<ExamMember > findById(Long id);
    public List<ExamMember> findByNameID(String name,String id);
}

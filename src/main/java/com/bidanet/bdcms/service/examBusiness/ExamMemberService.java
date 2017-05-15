package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamMember;
import com.bidanet.bdcms.service.Service;

import java.util.List;

/**
*
*/
public interface ExamMemberService extends Service<ExamMember> {

    public List<ExamMember> findById(Long id);
    public List<ExamMember> findBynameId(String name,String Id);

    public void updateMemberIcon(Long memberId,String memberPhoto);

    public int updateMemberInfo(Long id,Long memberId,String memberPhoto,String memberName,String memberAddress,String memberSex,int tag);

}

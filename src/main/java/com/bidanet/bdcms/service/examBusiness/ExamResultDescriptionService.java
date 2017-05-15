package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamResultDescription;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.Service;

import java.util.List;
import java.util.Map;

/**
*  cf
*/
public interface ExamResultDescriptionService extends Service<ExamResultDescription> {

    void joinMedicalT( String description,String descriptionId);

    /**
     * 根据项目类型查询该类型所有描述术语
     * @param projectId
     * @return
     */
    List<ExamResultDescription> getAllResultDescription(Long projectId);

    void upMedicalT(String updescriptionId, String updescription);

    void joinXT(String description, String descriptionId);

    void joinBlodT(String description, String descriptionId,String projectId);


    void joinMedicalT(String description, Integer type, Long projectId);

    /**
     * 修改体检信息结果内容
     * @param updescriptionId
     * @param updescription
     * @param upId
     * @param loginUser
     */
    void upExamResultT(String updescriptionId, String updescription, Long upId, User loginUser);

    /**
     * @param upExamNumber
     * @param upIdLength  所有描述的合格与否类型(如果有不合格的，则为不合格)
     * @param updescription 待修改的结果描述术语
     * @param loginUser
     * @param projectId
     * @param map
     */
    void upExamResultMedicalXT(String upExamNumber, String upIdLength, String updescription, User loginUser, Long projectId, Map<String, Object> map);
}

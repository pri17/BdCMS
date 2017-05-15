package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.bean.PhysicalItemDetails;
import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamMemberExam;
import com.bidanet.bdcms.entity.User;
import com.bidanet.bdcms.service.Service;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
*
*/
public interface ExamBlodIntestinalService extends Service<ExamBlodIntestinal> {

    List<ExamBlodIntestinal> findSelectedBlodDetail(long id);

    void entryBlodResultT(Long blodId, String isQualified5, String isQualified7, String isQualified8,
                          String blodResult5, String blodResult7, String blodResult8,
                          String isRecheck5, String isRecheck7, String isRecheck8, String descriptionVal, String realName);

    /**
     * 查询统计-体检结果查询
     * @param memberExamId
     * @return
     */
    List<ExamBlodIntestinal> findSelectedBlodIntestinalDetail(String memberExamId);

    /**
     * 采样记录查询-血检、肠检结果列表
     * @param ids
     */


    void defaultSelectedResultT(String[] ids);

    List<ExamBlodIntestinal> findSelectedIntestinalDetail(Long intestinalId);

    void entryIntestinalResultT(Long intestinalId, String isQualified6, String blodResult6, String isRecheck6, User user);

    /**
     * 查找选定的体检用户体检结果详情
     * @param examNumber
     * @return
     */
    List<ExamBlodIntestinal> findSelectedResultDetail(String examNumber);

    void defaultSelectedResultDecide(String[] ids,User loginUser);

    /**
     * 通过memberExamId删除相关信息
     * @param memberExamId
     */
    void deleteByMemberExamIdT(Long memberExamId);

    /**
     * 保存内科或者X线体检结果
     * @param user
     * @param isQualified
     * @param descriptionVal
     * @param descriptionId
     * @param examNumber
     * @param projectId
     */
    void saveMedicalResultT(User user, Integer isQualified, String descriptionVal, Long descriptionId, ExamMemberExam examNumber, Long projectId);

    /**
     * 设置选中的体检信息对应的结果都设为合格(体检结果判断用，以最后一次操作为准)
     * @param ids
     */
    void defaultSelectedResultForResultDecideT(String[] ids);

    /**
     * 保存内科或者X线体检结果(新)
     * @param user
     * @param isQualified
     * @param description
     * @param memberExam
     * @param projectId
     */
    void saveMedicalResultT(User user, Integer isQualified, String description, ExamMemberExam memberExam, Long projectId);

    /**
     * 通过项目id体检信息id查询结果信息
     * @param id
     * @param projectMedical
     * @return
     */
    ExamBlodIntestinal findResultByProIdMeId(Long id, Long projectMedical);

    /**
     * 设置筛选范围内的血检信息对应的血检结果都设为合格（原录入的不合格项保留）
     * @param beginTime
     * @param endTime
     * @param entryState
     * @param areaId
     * @param packageId
     * @param examNumber
     * @param type
     * @param loginUser
     */
    void defaultSelectedResultByConditionT(String beginTime, String endTime, Integer entryState, Long areaId, Long packageId, String examNumber, String type, User loginUser) throws ParseException;

    void entryBlodResultSecondT(Long blodId, String isQualified5, String isQualified7, String isQualified8, String blodResult5, String blodResult7, String blodResult8, String realName);

    /**
     * 录入肠检结果信息
     * @param intestinalId
     * @param isQualified6
     * @param intestinalResult6
     * @param user
     */
    void entryIntestinalResultSecondT(Long intestinalId, String isQualified6, String intestinalResult6, User user);

    /**
     * 所有项目判断为合格(除肠检外)
     * @param examMemberId
     * @param loginUser
     */
    void decideQualifiedT(Long examMemberId, User loginUser);

    void addBlod(ExamBlodIntestinal examBlodIntestinal);

    /**
     * 查找选定的体检用户体检结果详情(返回map保存登录用户)
     * @param examNumber
     * @param map
     * @return
     */
    List<ExamBlodIntestinal> findSelectedResultDetailBackMap(String examNumber, Map<String, Object> map);

    /**
     * 检测血检肠检是否已采集过
     * @param examNumber
     * @param type
     * @param map
     */
    void checkIsCollectT(String examNumber, String type, Map<String, Object> map);

    /**
     *设置筛选范围内的体检结果都设为合格
     * @param memberName
     * @param idNum
     * @param startTime
     * @param endTime
     * @param payState
     * @param areaId
     * @param isRecheck
     * @param verifyConclusion
     * @param examNumber
     * @param loginUser
     */
    void defaultSelectedResultDecideT(String memberName, String idNum, String startTime, String endTime, Integer payState, String areaId, Integer isRecheck, Integer verifyConclusion, String examNumber, User loginUser) throws ParseException;

    //录入体检结果数据
    void entryResultDecideT(JSONObject jsonObject, String realName);

    //录入右侧体检结果数据
    void saveRightResultDecideT(JSONObject jsonObject, String realName);

    //通过体检号,体检项目类别获取体检人信息，检测内科、X线体检状态
    void checkResultByProIdMeIdT(Map<String, Object> map, String examNumber, String projectId, String realName);

    /*
    * 新旧数据库同步
    * */
    Boolean updateExamBlodIntestinalT(int type, Long memberExamId, PhysicalItemDetails physicalItemDetails);
}

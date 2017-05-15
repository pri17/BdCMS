package com.bidanet.bdcms.service.examBusiness;

import com.bidanet.bdcms.entity.ExamAffected;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.ParseException;

/**
* cf
*/
public interface ExamAffectedService extends Service<ExamAffected> {

    void setAffectedT(String affectedCode);

    void deleteAffectedT(Long id);

    void findAllAffectedList(ExamAffected query,Page<ExamAffected> page);

    HSSFWorkbook downAffectedExcel(String beginTime,String endTime,String areaId)  throws ParseException;
}

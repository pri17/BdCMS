package com.bidanet.bdcms.service.fee;

import com.bidanet.bdcms.entity.ExamPayItem;
import com.bidanet.bdcms.entity.ExamPayType;
import com.bidanet.bdcms.service.Service;
import com.bidanet.bdcms.vo.Page;
import java.util.*;

/**
 * Created by zhangbinbin on 2016-10-11 11:35:59.
 */
public interface ExamPayTypeService extends Service<ExamPayType> {

    List<ExamPayType> getAllPayType();

}

package com.bidanet.bdcms.service.systemSetting.impl;

import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.dao.systemSetting.ExamDBBackUpRecDao;
import com.bidanet.bdcms.entity.ExamDBBackUpRec;
import com.bidanet.bdcms.service.impl.BaseServiceImpl;
import com.bidanet.bdcms.service.systemSetting.ExamDBBackUpRecService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * 数据库备份还原. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-11 14:40
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Service
public class ExamDBBackUpRecServiceImpl extends BaseServiceImpl<ExamDBBackUpRec> implements ExamDBBackUpRecService {

    @Autowired
    private ExamDBBackUpRecDao examDBBackUpRecDao;

    @Override
    protected Dao getDao() {
        return examDBBackUpRecDao;
    }


    @Override
    public void getDbBackUpList(ExamDBBackUpRec examAgencies, Page<ExamDBBackUpRec> page) {
//        return examDBBackUpRecDao.findAll();
        examAgencies.setIsCurrent(1);

        List<ExamDBBackUpRec> list = examDBBackUpRecDao.findByExample(examAgencies, page.getPageCurrent(), page.getPageSize());


        long count = examDBBackUpRecDao.countByExample(examAgencies);
        page.setList(list);
        page.setTotal(count);
    }

    @Override
    public ExamDBBackUpRec findById(Long id) {
        return examDBBackUpRecDao.get(id);
    }

    @Override
    public void addDBBackUp(ExamDBBackUpRec examDBBackUpRec) {

        examDBBackUpRecDao.save(examDBBackUpRec);

    }

    @Override
    public boolean backUpDb(OutputStream output, String dbName) {

        String command = "mysqldump -h "+SystemContent.MYSQL_URL+" -u" + SystemContent.DB_USERNAME
                + " -p" + SystemContent.DB_PASSWORD + " --set-charset=UTF8 " + dbName;
        PrintWriter p = null;
        BufferedReader reader = null;
        try {
            p = new PrintWriter(new OutputStreamWriter(output, "utf8"));
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader inputStreamReader = new InputStreamReader(process
                    .getInputStream(), "utf8");
            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                p.println(line);
            }
            p.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (p != null) {
                    p.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean recoveryDb(InputStream in, String dbName) {

        String command = SystemContent.DB_PATH + "mysql -h "+SystemContent.MYSQL_URL+ "-u" + SystemContent.DB_USERNAME
                + " -p" + SystemContent.DB_PASSWORD + " --default-character-set=utf8  " + dbName;
        try {
            Process process = Runtime.getRuntime().exec(command);

            String line = null;
            String outStr = null;
            StringBuffer sb = new StringBuffer("");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "utf8"));
            while ((line = br.readLine()) != null) {
                sb.append(line + "\r\n");
            }

            outStr = sb.toString();

            System.out.print("outStr:" + outStr);

            OutputStream out = process.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
            writer.write(outStr);
            writer.flush();
            out.flush();
            out.close();
            br.close();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}

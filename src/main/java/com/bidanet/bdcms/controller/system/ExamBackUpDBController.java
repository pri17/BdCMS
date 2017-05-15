package com.bidanet.bdcms.controller.system;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.common.SystemContent;
import com.bidanet.bdcms.controller.admin.AdminBaseController;
import com.bidanet.bdcms.entity.ExamDBBackUpRec;
import com.bidanet.bdcms.service.systemSetting.ExamDBBackUpRecService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 备份数据库. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-10-11 14:00
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
@Controller("examBackUpDB")
@RequestMapping ("/admin/systemSetting/examDBBackUpRecovery")
public class ExamBackUpDBController extends AdminBaseController {

    @Autowired
    private ExamDBBackUpRecService examDBBackUpRecService;

    private String tableId = "";


    @RequestMapping ("/index")
    public void index(@ModelAttribute("query") ExamDBBackUpRec query,
                      @ModelAttribute Page<ExamDBBackUpRec> page,String tabid){

        examDBBackUpRecService.getDbBackUpList(query,page);


        tableId = tabid;
    }

    /**
     * 新增备份
     * @param examDBBackUpRec
     * @return
     */
    @RequestMapping("/addBackUp")
    @ResponseBody
    public AjaxCallBack addBackUp(ExamDBBackUpRec examDBBackUpRec){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String backUpTime = df.format(date);


        String dbBackUpPath  = SystemContent.DB_STOREPATH+"/"+backUpTime+".sql";

        examDBBackUpRec.setCreator(uc.getUser().getUsername());
        examDBBackUpRec.setCreateTime(date.getTime());
        examDBBackUpRec.setPath(dbBackUpPath);
        examDBBackUpRec.setIsCurrent(1);

        examDBBackUpRecService.addDBBackUp(examDBBackUpRec);

        OutputStream out = null;
        try {
            out = new FileOutputStream(dbBackUpPath);

            examDBBackUpRecService.backUpDb(out,SystemContent.DB_NAME);

            System.out.print("userName:"+uc.getUser().getUsername());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AjaxCallBack ajaxCallBack = AjaxCallBack.backUpSuccess();
        ajaxCallBack.setTabid(tableId);
        return ajaxCallBack;
    }

    @RequestMapping ("/toRecoveryDB")
    @ResponseBody
    public AjaxCallBack recoveryDb(Long id){
        //获取数据库数据
        ExamDBBackUpRec examDBBackUpRec = examDBBackUpRecService.findById(id);

        InputStream in = null;
        try {
            in = new FileInputStream(examDBBackUpRec.getPath());

            examDBBackUpRecService.recoveryDb(in,SystemContent.DB_NAME);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return AjaxCallBack.recoverySuccess();
    }

    /**
     * 删除体检项目及价格
     * @param id
     * @return
     */
    @RequestMapping("/deleteBackUp")
    @ResponseBody
    public AjaxCallBack deleteBackUp(Long id){
        examDBBackUpRecService.deleteByIdT(id);
        return AjaxCallBack.deleteSuccess();
    }



    /**
     * 备份数据库
     *
     * @param output
     *            输出流
     * @param dbname
     *            要备份的数据库名
     */
    public static void backup(OutputStream output, String dbname) {

        String command = SystemContent.DB_PATH+"mysqldump -h "+SystemContent.MYSQL_URL+" -u" + SystemContent.DB_USERNAME
                + " -p" + SystemContent.DB_PASSWORD + " " + dbname;
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
    }

    /**
     * 恢复数据库
     *
     * @param input
     *            输入流
     * @param dbname
     *            数据库名
     */
    public static void restore(InputStream input, String dbname) {
        String command = SystemContent.DB_PATH+"mysql -hlocalhost -u" + SystemContent.DB_USERNAME
                + " -p" + SystemContent.DB_PASSWORD + " --default-character-set=utf8  " + dbname;
        try {
            Process process = Runtime.getRuntime().exec(command);
            OutputStream out = process.getOutputStream();
            String line = null;
            String outStr = null;
            StringBuffer sb = new StringBuffer("");
            BufferedReader br = new BufferedReader(new InputStreamReader(input,
                    "utf8"));
            while ((line = br.readLine()) != null) {
                sb.append(line + "/r/n");
            }
            outStr = sb.toString();
            OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
            writer.write(outStr);
            writer.flush();
            out.close();
            br.close();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void backup(String backPath, String dbname) {
//        try {
//            OutputStream out = new FileOutputStream(backPath);
//            backup(out, dbname);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) {

        Long time = new Date().getTime();

//        String backPath="/Users/zangli/examDB"+"/"+time+".sql";

        String backPath="/Users/zangli/examDB/1479184289898.sql";

        OutputStream out = null;
//        InputStream in = null;
        try {
            out = new FileOutputStream(backPath);
//            in = new FileInputStream(backPath);

            backup(out, "db_exam");
//            restore(in,"db_exam");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }


    }

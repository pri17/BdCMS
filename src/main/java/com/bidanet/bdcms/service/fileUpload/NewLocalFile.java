package com.bidanet.bdcms.service.fileUpload;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.service.examManage.ExamEcardService;
import com.bidanet.bdcms.vo.ExamEcardVo;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by gao on 2017/2/22.
 */
public class NewLocalFile {


    public static void doGet(String content)
            throws ServletException, IOException {
                //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
                String savePath = SpringWebTool.getRealPath("/WEB-INF/templates/upload");
                //上传时生成的临时文件保存目录
                String tempPath = SpringWebTool.getRealPath("/WEB-INF/templates/temp");
                File tmpFile = new File(tempPath);
                if (!tmpFile.exists()) {
                    //创建临时目录
                    tmpFile.mkdir();
                }
                    String filename="data.json";
                   //得到文件保存的名称
                   String saveFilename = makeFileName(filename);
                    SpringWebTool.getSession().setAttribute("saveFilename",saveFilename);
                    //得到文件的保存目录
                    String realSavePath = makePath(saveFilename, savePath);
                    SpringWebTool.getSession().setAttribute("realSavePath",realSavePath);
                    //创建一个文件输出流
                    FileOutputStream txtfile = new FileOutputStream(realSavePath+ "\\" + saveFilename);
                    PrintWriter p = new PrintWriter(txtfile);
                    p.println(content);
                    txtfile.close();
                    p.close();
    }


//        request.setAttribute("message",message);
//        request.getRequestDispatcher("/file_message.jsp").forward(request, response);
//    }

    /**
     * @Method: makeFileName
     * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
     * @Anthor:孤傲苍狼
     * @param filename 文件的原始名称
     * @return uuid+"_"+文件的原始名称
     */
    public static String makeFileName(String filename){  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }

    /**
     * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
     * @Method: makePath
     * @Description:
     * @Anthor:孤傲苍狼
     *
     * @param filename 文件名，要根据文件名生成存储目录
     * @param savePath 文件存储路径
     * @return 新的存储目录
     */
    public static String makePath(String filename,String savePath){//得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        int dir1 = hashcode&0xf;  //0--15
        int dir2 = (hashcode&0xf0)>>4;  //0-15
        //构造新的保存目录
        String dir = savePath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
        //File既可以代表文件也可以代表目录
        File file = new File(dir);//如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
        return dir;
    }


}

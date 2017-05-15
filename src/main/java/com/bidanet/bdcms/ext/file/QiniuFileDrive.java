package com.bidanet.bdcms.ext.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuejike on 2016-06-02.
 */
@Service("qiNiuFileDrive")
public class QiniuFileDrive extends BaseFileDrive implements FileDrive {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "qjzaXSq9cy3MWwrW-crP3vGzdpR7yOhW1ZH_OqHy";
    String SECRET_KEY = "jqjCzDHBOz7zcY-oZ52A4ER6ZLGapeYxsGvQ5VYb";
    //要上传的空间
    String fileBucketName = "yinpubao";
    String tempPath="/temp";
    String fileHost="http://img.yingegou.com/";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(fileBucketName);
    }

    protected String uploadQiNiu(String filePath,String fileName) throws IOException{
        try {
            //调用put方法上传
            Response res = uploadManager.put(filePath, fileName, getUpToken());
            //打印返回的信息
            String s = res.bodyString();
            return fileHost+fileName;

        } catch (QiniuException e) {
            Response r = e.response;

            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
                throw new IOException(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return "";
    }




    @Override
    public String upload(File file, String saveName) throws IOException {
        return uploadQiNiu(file.getPath(),saveName);
    }

    @Override
    public String upload(File file) throws IOException {
        return uploadQiNiu(file.getPath(),createRandomName(file.getName()));
    }

    @Override
    public String upload(InputStream in, String oldName) throws IOException {
        return uploadToFileName(in,createRandomName(oldName));
    }

    @Override
    public String uploadToFileName(InputStream in, String newName) throws IOException {
        String s = uploadTempFile(in, newName);
        String url = uploadQiNiu(s, createRandomName(newName));
        FileUtils.deleteQuietly(new File(s));
        return url;
    }

    @Override
    public String uploadImg(File file, String saveName) throws IOException {
        return uploadQiNiu(file.getPath(),saveName);
    }

    @Override
    public String uploadImg(File file) throws IOException {
        return uploadQiNiu(file.getPath(),createRandomName(file.getName()));
    }
    @Override
    public String uploadImgToFileName(InputStream in, String saveName) throws IOException {
        String s = uploadTempFile(in, saveName);
        String url = uploadQiNiu(s, saveName);
        FileUtils.deleteQuietly(new File(s));
        return url;
    }
    @Override
    public String uploadImg(InputStream in, String oldName) throws IOException {
        return uploadToFileName(in,createRandomName(oldName));
    }

}

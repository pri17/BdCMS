package com.bidanet.bdcms.ext.file;

import com.bidanet.bdcms.common.SpringWebTool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * Created by xuejike on 2016-06-03.
 */
public class BaseFileDrive {
    protected String tempPath="/temp";

    protected String getTempPath(){
        String realPath = SpringWebTool.getRealPath(tempPath);
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        return realPath;
    }

    public String uploadTempFile(InputStream in,String name) throws IOException {
        File file = new File(getTempFilePath(name));
        FileUtils.copyInputStreamToFile(in,file);
        return file.getPath();
    }

    public String getTempFilePath(String name) {
        if (name!=null){
            int lastIndexOf = name.lastIndexOf("/");
            if (lastIndexOf>=0){
                name=name.substring(lastIndexOf+1);
            }

        }
        return getTempPath() + "/" + new Date().getTime()+UUID.randomUUID().toString()+name;
    }

    public String createRandomName(String fileName) {
        String extName = getFileExt(fileName);
        byte[] bytes = Base64.encodeBase64(UUID.randomUUID().toString().getBytes());
        String s = new String(bytes);
        return s + extName;
    }

    private String getFileExt(String fileName) {
        int i = fileName.lastIndexOf(".");
        String extName="";
        if (i>=0){
            extName = fileName.substring(i);
        }
        return extName;
    }
}

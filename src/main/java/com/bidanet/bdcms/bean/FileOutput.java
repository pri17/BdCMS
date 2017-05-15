package com.bidanet.bdcms.bean;

import java.io.File;
import java.util.HashMap;

/**
 * 文件输出类，用于SpringMVC 文件输出
 */
public class FileOutput {
    protected File file;
    protected String filename;
    protected String contentType="application/octet-stream";

    private static  HashMap<String,String> contentTypeMap;
    static {
        contentTypeMap=new HashMap<>();
        contentTypeMap.put(".png","image/png");
        contentTypeMap.put(".ico","image/x-ico");
        contentTypeMap.put(".jpe","image/jpeg");
        contentTypeMap.put(".jfif","image/jpeg");
        contentTypeMap.put(".jpg","image/jpeg");
        contentTypeMap.put(".jpeg","image/jpeg");
        contentTypeMap.put(".xls","application/octet-stream");
        contentTypeMap.put(".wbmp","image/vnd.wap.wbmp");

    }
    public FileOutput(File file, String filename) {
        this.file = file;
        this.filename = filename;
        int lastIndexOf = filename.lastIndexOf(".");
        String substring = filename.substring(lastIndexOf);
        String s = contentTypeMap.get(substring);
        if (s!=null){
            contentType=s;
        }

    }

    public FileOutput(File file) {
        this(file,file.getName());
    }
    public FileOutput(String path, String filename){
        this(new File(path),filename);
    }
    public FileOutput(String path){
        this(new File(path));
    }


    public File getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}

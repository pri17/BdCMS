package com.bidanet.bdcms.ext.file;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.image.Base64Image;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 *
 */
@Service("localFileDrive")
public class LocalFileDrive extends BaseFileDrive implements FileDrive {
    protected static final String path="/upload";
    protected static final String filePath="file";
    protected static final String imgPath="img";

    protected static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    private String getSavePath(String subPath){

        String tempPath = path + "/" + subPath + "/" + simpleDateFormat.format(new Date());
        String realPath = SpringWebTool.getRealPath(tempPath);
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        return tempPath;
    }
    private String getFileSavePath(){
        return getSavePath(filePath);
    }
    private String getImgSavePath(){
        return getSavePath(imgPath);
    }

    @Override
    public String upload(File file, String saveName) throws IOException {
        String fileSavePath = getFileSavePath();
        String s = fileSavePath + "/" + saveName;
        FileUtils.moveFile(file,new File(SpringWebTool.getRealPath(s)));
        return SpringWebTool.getWebRootUrl() +s;
    }

    @Override
    public String upload(File file) throws IOException {
        String fileName = createRandomName(file.getName());
        return upload(file,fileName);

    }
    @Override
    public String upload(InputStream in, String oldName) throws IOException {
        String fileName = createRandomName(oldName);
        return uploadToFileName(in,fileName);
    }
    @Override
    public String uploadToFileName(InputStream in, String newName) throws IOException {
        String fileSavePath = getFileSavePath();
        String s = fileSavePath + "/" + newName;
        File file = new File(SpringWebTool.getRealPath(s));
        FileUtils.copyInputStreamToFile(in,file);
        return SpringWebTool.getWebRootUrl()+s;
    }

    public String uploadBase64(String base64,String saveName){

//        byte[] bytes = Base64.decodeBase64(base64);
        String fileSavePath = getFileSavePath();
        String s = fileSavePath + "/" + saveName;
//        File file = new File(SpringWebTool.getRealPath(s));
        try {
            Base64Image.getImage(base64,SpringWebTool.getRealPath(s));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (ImageWriteException e) {
            e.printStackTrace();
        }
        return SpringWebTool.getWebRootUrl()+s;

    }
    @Override
    public String uploadImg(File file, String saveName) throws IOException {
        String fileSavePath = getImgSavePath();
        String s = fileSavePath + "/" + fileSavePath;
        FileUtils.moveFile(file,new File(SpringWebTool.getRealPath(s)));
        return SpringWebTool.getWebRootUrl()+s;
//        return null;
    }

    @Override
    public String uploadImg(File file) throws IOException {
        
        String fileName = createRandomName(file.getName());
        return upload(file,fileName);

    }
    public String getFileRealPath(String url){
        String replace = url.replace(SpringWebTool.getWebRootUrl(), "");
        String realPath = SpringWebTool.getRealPath(replace);
        return realPath;
    }

    @Override
    public String uploadImgToFileName(InputStream in, String saveName) throws IOException {
        return null;
    }

    @Override
    public String uploadImg(InputStream in, String oldName) throws IOException {
        return null;
    }


}

package com.bidanet.bdcms.service.fileUpload;

import com.bidanet.bdcms.common.SpringWebTool;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by gao on 2017/2/22.
 */
public class ZipFile {

        private ZipFile(){}

        /**
         * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
         * @param sourceFilePath :待压缩的文件路径
         * @param zipFilePath :压缩后存放路径
         * @param fileName :压缩后文件的名称
         * @return
         */
        public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){
            boolean flag = false;
            File sourceFile = new File(sourceFilePath);
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            ZipOutputStream zos = null;

            File zipFileCatalog = new File(zipFilePath);
            if(!zipFileCatalog.exists()){
                //创建目录
                zipFileCatalog.mkdir();
            }

            if(sourceFile.exists() == false){
                System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
            }else{
                try {
                    File zipFile = new File(zipFilePath + "/" + fileName +".zip");
                    String zipPath=zipFilePath + "/" + fileName +".zip";
                    SpringWebTool.getSession().setAttribute("zipPath",zipPath);
                    if(zipFile.exists()){
                        System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
                    }else{

                        String sourcefilePath=(String)SpringWebTool.getSession().getAttribute("realSavePath");

                        File dfile = new File(sourcefilePath);
                        File[] sourceFiles = dfile.listFiles();
                        if(null == sourceFiles || sourceFiles.length<1){
                            System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                        }else{
                            fos = new FileOutputStream(zipFile);
                            zos = new ZipOutputStream(new BufferedOutputStream(fos));
                            byte[] bufs = new byte[1024*10];
                            for(int i=0;i<sourceFiles.length;i++){
                                //创建ZIP实体，并添加进压缩包
                                ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                                zos.putNextEntry(zipEntry);
                                //读取待压缩的文件并写进压缩包里
                                fis = new FileInputStream(sourceFiles[i]);
                                bis = new BufferedInputStream(fis, 1024*10);
                                int read = 0;
                                while((read=bis.read(bufs, 0, 1024*10)) != -1){
                                    zos.write(bufs,0,read);
                                }
                            }
                            flag = true;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally{
                    //关闭流
                    try {
                        if(null != fis) fis.close();
                        if(null != bis) bis.close();
                        if(null != zos) zos.close();
                        if(null != fos) fos.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
            return flag;
        }

        public static void main(String[] args){
            String name=(String) SpringWebTool.getSession().getAttribute("saveFilename");
            String filePath=(String)SpringWebTool.getSession().getAttribute("realSavePath")+"\\"+name;
            String sourceFilePath = filePath;
            String zipFilePath = "/WEB-INF/templates/zip";
           // String fileName = "data.json";
            boolean flag = ZipFile.fileToZip(filePath, zipFilePath, name);
            if(flag){
                System.out.println("文件打包成功!");
            }else{
                System.out.println("文件打包失败!");
            }
        }
        public static boolean zipFile(){
            String name=(String) SpringWebTool.getSession().getAttribute("saveFilename");
            String filePath=(String)SpringWebTool.getSession().getAttribute("realSavePath")+"\\"+name;
            String sourceFilePath = filePath;
            String zipFilePath = SpringWebTool.getRealPath("/WEB-INF/templates/zip");
            // String fileName = "data.json";
            boolean flag = ZipFile.fileToZip(filePath, zipFilePath, name);
            return flag;
        }


    }


package com.bidanet.bdcms.common;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.*;
import java.util.Properties;

/**
 * . <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public class velocityTool {
    static{
        Properties p = new Properties();
        // 设置输入输出编码类型。和这次说的解决的问题无关
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        //文件缓存
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "false");
        // 这里加载类路径里的模板而不是文件系统路径里的模板
        p.setProperty("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }

    public static Template getTemplateInstance(String templtePath){

        Template template = Velocity.getTemplate(templtePath);
        return template;
    }

    public static void main(String[] args){

//        List<Phone> phoneList = new ArrayList<Phone>();
//        for(int i=0;i<4;i++){
//            Phone phone = new Phone();
//            phone.setId(i+"");
//            phone.setName("手机"+i);
//            phone.setDesc("描述"+i);
//            phoneList.add(phone);
//        }

//        //实例化一个VelocityEngine对象

        VelocityEngine ve = new VelocityEngine();

        Properties properties=new Properties();
//        //设置velocity资源加载方式为class
//        properties.setProperty("resource.loader", "class");
//        //设置velocity资源加载方式为file时的处理类
//        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            String basePath = "src/main/java/com.bidanet.bdcms/";//这里需要这样写路径！！！
            // 设置模板的路径
            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, basePath);

        Velocity.init(properties);

try {

    Template t = Velocity.getTemplate("exam.xml");
            /* create a context and add data */
    VelocityContext context = new VelocityContext();
    context.put("sex", "DingDangXiaoMa");
            /* now render the template into a StringWriter */
    Writer writer = new FileWriter(new File("/Users/zangli/Documents/testExam/test.doc"));
    t.merge(context, writer);
            /* show the World */
    System.out.println(writer.toString());

    writer.flush();
            writer.close();
            System.out.println("over");

}catch (Exception e){

}


//        try {
//            Writer writer = new FileWriter(new File("/Users/zangli/Documents/testExam/test.doc"));
//            Context con = new VelocityContext();
//            con.put("name","sb");
//            con.put("time","2016-11-25");
////            con.put("phones", phoneList);
////            con.put("wordName", "我传过去的文档名称");
//
//            velocityEngine.mergeTemplate("com/velocity/test/hello.vm", "gbk", con, writer);
//
//            template.merge(con, writer);
//            writer.flush();
//            writer.close();
//            System.out.println("over");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//        //初始化参数
//        Properties properties=new Properties();
////        //设置velocity资源加载方式为class
////        properties.setProperty("resource.loader", "class");
////        //设置velocity资源加载方式为file时的处理类
////        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//            String basePath = "src/main/java/com.bidanet.bdcms";//这里需要这样写路径！！！
//            // 设置模板的路径
//            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, basePath);
//
//
//            //实例化一个VelocityEngine对象
//        VelocityEngine velocityEngine=new VelocityEngine(properties);
//
//        //实例化一个VelocityContext
//        VelocityContext context=new VelocityContext();
//        //向VelocityContext中放入键值
//            context.put("name","sb");
//            context.put("time","2016-11-25");
//
//        //实例化一个StringWriter
//            Writer writer = new FileWriter(new File("/Users/zangli/Documents/testExam/test.doc"));
//
//        //从src目录下加载hello.vm模板
//        //假若在com.velocity.test包下有一个hello.vm文件,那么加载路径为com/velocity/test/hello.vm
//
//        writer.flush();
//
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("over");

    }

}

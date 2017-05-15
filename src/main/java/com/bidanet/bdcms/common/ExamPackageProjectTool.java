package com.bidanet.bdcms.common;

import com.bidanet.bdcms.entity.ExamBlodIntestinal;
import com.bidanet.bdcms.entity.ExamPackageProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuejike on 2017/2/22.
 */
public class ExamPackageProjectTool {
    public static HashMap<Long, String> examProjectMap = new HashMap<>();
      static {
          examProjectMap = new HashMap<>();
          examProjectMap.put(5L,"血检");
          examProjectMap.put(6L,"肠检");
          examProjectMap.put(8L,"血检");
          examProjectMap.put(7L,"血检");
          examProjectMap.put(9L,"内科检查");
          examProjectMap.put(10L,"胸部X线检查");
      }

      public static List<String> buildProject(List<ExamPackageProject> eppList){
            return buildProject(eppList,null);

      }
      public static List<String> buildProjectByBlod(List<ExamBlodIntestinal> examBlodIntestinalList ){
//          new ExamPackageProject()
          ArrayList<ExamPackageProject> list = new ArrayList<>();
          for (ExamBlodIntestinal intestinal : examBlodIntestinalList) {
              ExamPackageProject project = new ExamPackageProject();
              project.setProjectId(intestinal.getProjectId());
              list.add(project);
          }
          return buildProject(list,0);

      }

    public static List<String> buildProject(List<ExamPackageProject> eppList,Integer begin){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();

        for (ExamPackageProject examPackageProject : eppList) {
            String name = examProjectMap.get(examPackageProject.getProjectId());
            int i = nameList.indexOf(name);
            if (i==-1){
                nameList.add(name);
                String newName="";
                if (begin!=null){
                    begin++;
                    newName=begin+".";
                }
                newName+=name;
                list.add(newName);
            }
        }
        return list;

    }
}

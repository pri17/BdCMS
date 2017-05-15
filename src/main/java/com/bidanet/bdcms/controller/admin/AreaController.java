package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.entity.Area;
import com.bidanet.bdcms.service.AreaService;
import com.bidanet.bdcms.vo.Page;
import com.bidanet.bdcms.vo.ValueLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CF on 2016/5/27.
 */

/**
 * 开通城市管理
 */
@Controller
@RequestMapping("admin/area")
public class AreaController extends AdminBaseController {

    @Autowired
    private AreaService areaService;

    @RequestMapping("/index")
    public void index(
            @ModelAttribute("query") Area query,
            @ModelAttribute Page<Area> page
    ){

        areaService.getPageByExample(query,page);
    }

    @RequestMapping("/toAddArea")
    public void toAddArea(String parentId,Model model){
        model.addAttribute("parentId",parentId);
    }

    @RequestMapping("/addArea")
    @ResponseBody
    public AjaxCallBack addArea(Area area){
        areaService.addAreaT(area);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setTabid("areaIndex");
        return AjaxCallBack.addSuccess();
    }

    @RequestMapping("/toUpdateArea")
    public void toUpdateArea(Long id,Model model){
        Area area = areaService.get(id);
        model.addAttribute("area",area);
    }

    @RequestMapping("/updateArea")
    @ResponseBody
    public AjaxCallBack updateArea(Area area){
        areaService.updateAreaT(area);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setTabid("areaIndex");
        return ajaxCallBack;
    }

    @RequestMapping("/deleteArea")
    @ResponseBody
    public AjaxCallBack deleteArea(Long id){
        areaService.deleteAreaT(id);
        return AjaxCallBack.deleteSuccess();
    }

    /**
     * 根据省份id获取城市
     * @param pid
     * @return
     */
    @RequestMapping("/getOpenCity")
    @ResponseBody
    public List<ValueLabel> getOpenCity(Long pid){
        List<ValueLabel> list = new ArrayList<ValueLabel>();
        if(pid!=null){
            List<Area> cityList = areaService.getOpenCityByPid(pid);
            if(cityList!=null&&cityList.size()>0) {
                list.add(new ValueLabel("","请选择城市！"));
                for (Area city : cityList) {
                    list.add(new ValueLabel(String.valueOf(city.getId()), city.getName()));
                }
            }else{
                list.add(new ValueLabel("","暂无城市" ));
            }
        }else{
            list.add(new ValueLabel("","请选择城市" ));
        }
        return  list;
    }

    /**
     * 地区查找带回
     * @param query
     * @param page
     */
    @RequestMapping("/lookBackArea")
    public void lookBackArea(
            @ModelAttribute("query") Area query,
            @ModelAttribute Page<Area> page
    ){
        areaService.getPageByExample(query,page);
    }

    /**
     * 地区查找带回(单选)
     * @param query
     * @param page
     */
    @RequestMapping("/lookBackAreaSingle")
    public void lookBackAreaSingle(
            @ModelAttribute("query") Area query,
            @ModelAttribute Page<Area> page
    ){
        areaService.getPageByExample(query,page);
    }

}

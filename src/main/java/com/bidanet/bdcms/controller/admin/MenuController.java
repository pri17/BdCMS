package com.bidanet.bdcms.controller.admin;

import com.bidanet.bdcms.bean.AjaxCallBack;
import com.bidanet.bdcms.entity.Menu;
import com.bidanet.bdcms.service.MenuService;
import com.bidanet.bdcms.service.RoleMenuService;
import com.bidanet.bdcms.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * MenuController
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuController extends AdminBaseController{
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     *菜单列表页
     * @param query
     * @param page
     */
    @RequestMapping("/menuList")
    public void menuList(@ModelAttribute("query") Menu query,
                         @ModelAttribute Page<Menu> page){
        page.setPageSize(100000);
        menuService.getPageByExample(query, page);
    }
    /**
     *菜单
     * @param query
     * @param page
     */
    @RequestMapping("/menus")
    public void menus(
            long parentId,
            @ModelAttribute("query") Menu query,
            @ModelAttribute Page<Menu> page){
        query.setId(parentId);
        menuService.getPageByExample(query, page);
    }

    /**
     * 跳转增加页面
     * @param menu
     * @param model
     */
    @RequestMapping("/toAddMenu")
    public void toAddMenu(Menu menu, Model model){
        model.addAttribute("vo",menu);
    }

    /**
     * 调用增加方法
     * @param menu
     * @return
     */
    @RequestMapping("/addMenu")
    @ResponseBody
    public AjaxCallBack addMenu(Menu menu, String divId){
        menuService.addMenuT(menu);
        AjaxCallBack ajaxCallBack = AjaxCallBack.addSuccess();
        ajaxCallBack.setDivid(divId);
        return ajaxCallBack;
    }

    /**
     * 跳转修改页面
     * @param id
     * @param model
     */
    @RequestMapping("/toUpdateMenu")
    public void toUpdateMenu(Long id,Model model){
       Menu menu=menuService.get(id);
        model.addAttribute("vo",menu);
    }

    /**
     * 调用修改方法
     * @param menu
     * @return
     */
    @RequestMapping("/updateMenu")
    @ResponseBody
    public AjaxCallBack updateMenu(Menu menu, String upId){
        menuService.updateMenuT(menu);
        AjaxCallBack ajaxCallBack = AjaxCallBack.saveSuccess();
        ajaxCallBack.setDivid(upId);
        return ajaxCallBack;
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @RequestMapping("/deleteMenus")
    @ResponseBody
    public AjaxCallBack deleteMenus(Long id, String deleteId){
//        Long roleMenuId=roleMenuService.getById(id);
//        roleMenuService.deleteT(roleMenuId);
        roleMenuService.deleteByIdT(id);
        AjaxCallBack ajaxCallBack = AjaxCallBack.deleteSuccess();
        ajaxCallBack.setDivid(deleteId);
        return ajaxCallBack;
    }

    @RequestMapping("/getMenuTree")
    @ResponseBody
    public List<Menu> getMenuTree(){
        List<Menu> menuList= menuService.getTree();
        return  menuList;
    }
}

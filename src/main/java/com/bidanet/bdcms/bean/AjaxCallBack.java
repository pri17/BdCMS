package com.bidanet.bdcms.bean;

/**
 * Created by avatek on 2015/11/3 0003.
 */
public class AjaxCallBack extends Result {



    private String tabid;
    private String dialogid;
    private String divid;
    private boolean closeCurrent;
    private String forward;
    private String forwardConfirm;

    public AjaxCallBack() {
    }
    public static AjaxCallBack success(String msg){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, msg,"", true);

        return ajaxCallBack;
    }

    public static AjaxCallBack successParam(String msg,String param){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, msg,param, true);

        return ajaxCallBack;
    }
    public static AjaxCallBack addSuccess(){
        return success("添加成功，您还可以继续添加");
    }

    public static AjaxCallBack saveSuccess(){
        return success("保存成功");
    }

    public static AjaxCallBack editSuccess(){
        return success("修改成功");
    }

//    public  static  AjaxCallBack saveBackParam(String msg,String param){return  successParam("保存成功",)}

    /**
     * 备份成功
     * @return
     */
    public static  AjaxCallBack backUpSuccess(){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, "备份成功","", false);
        return ajaxCallBack;
    }

    /**
     * 数据库还原成功
     * @return
     */
    public static  AjaxCallBack recoverySuccess(){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, "还原成功","", false);
        return ajaxCallBack;
    }

    public static AjaxCallBack deleteSuccess(){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, "删除成功","", false);
        return ajaxCallBack;
    }

    /**
     * 解绑成功，by pri17
     */
    public static AjaxCallBack unbindSuccess(){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK,"解绑成功","",false);
        return ajaxCallBack;
    }

    public static AjaxCallBack handleSuccess(){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_OK, "操作成功","", false);
        return ajaxCallBack;
    }

    public static AjaxCallBack error(String msg){
        AjaxCallBack ajaxCallBack = new AjaxCallBack(STATUS_ERROR, msg,"", false);
        return ajaxCallBack;
    }
    public static AjaxCallBack timeout(){
        return new AjaxCallBack(STATUS_TIMEOUT,"未登录","",false);
    }


    public AjaxCallBack(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public AjaxCallBack(int statusCode, String message,String param, boolean closeCurrent) {
        this.statusCode = statusCode;
        this.message = message;
        this.param = param;
        this.closeCurrent = closeCurrent;
    }

    public AjaxCallBack(int statusCode, String message, boolean closeCurrent) {
        this.statusCode = statusCode;
        this.message = message;
        this.closeCurrent = closeCurrent;
    }

    public String getTabid() {
        return tabid;
    }

    public void setTabid(String tabid) {
        this.tabid = tabid;
    }

    public String getDialogid() {
        return dialogid;
    }

    public void setDialogid(String dialogid) {
        this.dialogid = dialogid;
    }

    public String getDivid() {
        return divid;
    }

    public void setDivid(String divid) {
        this.divid = divid;
    }

    public boolean isCloseCurrent() {
        return closeCurrent;
    }

    public void setCloseCurrent(boolean closeCurrent) {
        this.closeCurrent = closeCurrent;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getForwardConfirm() {
        return forwardConfirm;
    }

    public void setForwardConfirm(String forwardConfirm) {
        this.forwardConfirm = forwardConfirm;
    }


}

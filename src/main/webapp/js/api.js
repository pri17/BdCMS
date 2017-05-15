/**
 * Created by Administrator on 2016/7/2.
 */
var api={};
var apiHost=window.location.host+"/api/";


api.cache={};
api.cache.set=function (key, val) {
    if(window.sessionStorage){  //或者 window.sessionStorage
        window.sessionStorage.setItem(key,JSON.stringify(val));
    }else{
        // alert("浏览暂不支持localStorage")
    }
};
api.cache.get=function (key) {
    if(window.sessionStorage){  //或者 window.sessionStorage
        return JSON.parse(window.sessionStorage.getItem(key));
    }else{
        // alert("浏览暂不支持localStorage")
    }
};
api.storage={};
api.storage.set=function (key,val){
    if(window.localStorage){  //或者 window.sessionStorage
        window.localStorage.setItem(key,JSON.stringify(val));
    }else{
        // alert("浏览暂不支持localStorage")
    }
};
api.storage.get=function (key) {
    if(window.localStorage){  //或者 window.sessionStorage
        return JSON.parse(window.localStorage.getItem(key));
    }else{
        // alert("浏览暂不支持localStorage")
    }
};



api.req= buildRequest();
api.debug=true;

$(function () {


});


function buildRequest() {
    var url = window.location.search; //获取url中"?"符后的字串
    var theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            //就是这句的问题
            theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
            //之前用了unescape()
            //才会出现乱码
        }
    }
    return theRequest;
}

function sendCallBack(data,successFun,errorFun,nologinFun) {
    if(data.statusCode==200){
        successFun(data.data);
    }else if(data.statusCode==300){
        errorFun(data.message);
    }else if(data.statusCode==301){
        nologinFun();
    }else{
        errorFun("请求失败")
    }
}

function post(path,param,successFun,errorFun,noLoginFun) {

    param.token=api.storage.get("token");
    param.areaId=api.storage.get("areaId");
    $.getJSON(apiHost+path+"?jsonpcallback=?",param,
        function(res){
//                    console.log(data);
            sendCallBack(res,successFun,errorFun,noLoginFun);

        });
}





function postCache(path, param, successFun, errorFun, noLoginFun) {
    var key=path+"->"+JSON.stringify(param);
    param.token=api.storage.get("token");
    param.areaId=api.storage.get("areaId");
    if(api.cache.get(key)&&!api.debug){
        sendCallBack(api.cache.get(key),successFun,errorFun,noLoginFun);
    }else{
        $.getJSON(apiHost+path+"?jsonpcallback=?",param,
            function(res){
                api.cache.set(key,res);
                sendCallBack(res,successFun,errorFun,noLoginFun);

            });
    }
}

function saveLogin(data) {
    api.storage.set("token",data);
}





function getQuery() {
    res={};
    var param=window.location.search.substr(1);
    param.split('&').forEach(function(i){
        var j = i.split('=');
        res[j[0]]=j[1];
    });
    return res;
}
function setQuery(param) {
    var p= $.param(param);
    window.location.hash=p;
}
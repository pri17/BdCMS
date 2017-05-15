/**
 * Created by xuejike on 2017/2/28.
 */

function getNavEle(selector) {
    if(typeof selector=="string"){
        if($.CurrentDialog){
            var dobj=$.CurrentDialog.find(selector);
            if(dobj.length>0){
                return dobj;
            }
        }

        var obj=$.CurrentNavtab.find(selector);
        if(obj.length>0){
            return obj;
        }else{
            return $(selector);
        }
    }else{
        return $(selector);
    }
}

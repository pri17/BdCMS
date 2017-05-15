/**
 * Created by Administrator on 2016/6/20.
 */
function dateFormat(longDate,format) {
    if(format==undefined){
        format='yyyy-MM-dd';
    }
    return $.format.date(new Date(longDate),format);
}

/**
 * Created by xuejike-pc on 2017/1/19.
 */
function printRm(imageData,successFun,errorFun){
    $.post("http://localhost:9999/",imageData,function (res) {
        console.log(res);
    })
}
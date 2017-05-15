var localService = new Object();
localService.getInfo=function(successFun,errorFun) {
    $.post("http://localhost:9998/",
        function (data) {
            localService.serviceCallback(data,successFun,errorFun);
        }, "json");
}

localService.print = function (imageData, successFun, errorFun) {
    $.post("http://localhost:9999/?depth=170",imageData,
      function (data) {

        localService.serviceCallback(data,successFun,errorFun);
      }, "json");
}

localService.pvcPrint = function (info, successFun, errorFun) {
    $.post("http://localhost:9997/",JSON.stringify(info),
      function (data) {

          localService.serviceCallback(data,successFun,errorFun);

      }, "json");
}

localService.k532Print=function (imageData, successFun, errorFun) {
    $.post("http://localhost:9996/",imageData,
        function (data) {
            localService.serviceCallback(data,successFun,errorFun);
        }, "json");
}

localService.serviceCallback=function (data, successFun, errorFun) {
    if (data.Success) {
        successFun(data.Result);
    } else {
        errorFun(data.Result);
    }
}

localService.ztIdreader=function (repCount,successFun, errorFun) {
    var rs= SynCardOcx1.SetReaderPort(5);
    if(rs==0){
        SynCardOcx1.SetPhotoType(2);
        SynCardOcx1.SetReadType(0);
        localService.ztIdReaderAction(repCount,successFun,errorFun);
    }else{
        errorFun("读卡器打开失败")
    }
}
localService.ztIdReaderAction=function (repCount,successFun, errorFun) {
    if(repCount<0){
        errorFun("读取失败");
    }
    repCount--;
    var nRet = SynCardOcx1.ReadCardMsg();
    if(nRet==0)
    {
        var userData={
            name:SynCardOcx1.NameA,
            sex:SynCardOcx1.Sex,
            nation:SynCardOcx1.Nation,
            born:SynCardOcx1.Born,
            address:SynCardOcx1.Address,
            cardNo:SynCardOcx1.CardNo,
            image:SynCardOcx1.Base64Photo
        };
        successFun(userData);
    }else{
        console.log("读取失败，重新读取"+repCount);
        setTimeout(function(){
            localService.ztIdReaderAction(repCount,successFun,errorFun);
        },1000)

    }
}



var bowerPrint=new Object();
bowerPrint.rmPrint=function () {
    var oldPrintStr="";
    if(!jsPrintSetup){
        alert("请安装打印插件");
        window.open("https://addons.mozilla.org/en-US/firefox/addon/js-print-setup/")
    }else{
        oldPrintStr=jsPrintSetup.getPrinter();
        jsPrintSetup.setPrinter("GP-H80300BM");
        jsPrintSetup.setSilentPrint(true);

        // set page header/ /设置页面标题

        jsPrintSetup.setOption('marginTop', 0);
        jsPrintSetup.setOption('marginBottom', 0);
        jsPrintSetup.setOption('marginLeft', 0);
        jsPrintSetup.setOption('marginRight', 0);

        jsPrintSetup.setOption('headerStrLeft', '');

        jsPrintSetup.setOption('headerStrCenter', '');

        jsPrintSetup.setOption('headerStrRight', '');

        // set empty page footer/ /设置空页页脚

        jsPrintSetup.setOption('footerStrLeft', '');

        jsPrintSetup.setOption('footerStrCenter', '');

        jsPrintSetup.setOption('footerStrRight', '');
        // jsPrintSetup.setPrint()

        jsPrintSetup.printWindow(window);
        jsPrintSetup.setPrinter(oldPrintStr);

    }
}

bowerPrint.rmPrintCallBack=function (successF,errorF) {
    var oldPrintStr="";
    if(!jsPrintSetup){
        errorF();
        window.open("https://addons.mozilla.org/en-US/firefox/addon/js-print-setup/")
    }else{
        oldPrintStr=jsPrintSetup.getPrinter();
        jsPrintSetup.setPrinter("GP-H80300BM");
        jsPrintSetup.setSilentPrint(true);

        // set page header/ /设置页面标题

        jsPrintSetup.setOption('marginTop', 0);
        jsPrintSetup.setOption('marginBottom', 0);
        jsPrintSetup.setOption('marginLeft', 0);
        jsPrintSetup.setOption('marginRight', 0);

        jsPrintSetup.setOption('headerStrLeft', '');

        jsPrintSetup.setOption('headerStrCenter', '');

        jsPrintSetup.setOption('headerStrRight', '');

        // set empty page footer/ /设置空页页脚

        jsPrintSetup.setOption('footerStrLeft', '');

        jsPrintSetup.setOption('footerStrCenter', '');

        jsPrintSetup.setOption('footerStrRight', '');
        // jsPrintSetup.setPrint()

        jsPrintSetup.printWindow(window);
        jsPrintSetup.setPrinter(oldPrintStr);

        successF();
    }
}

bowerPrint.a4Print=function () {

    if(!jsPrintSetup){
        alert("请安装打印插件");
        window.open("https://addons.mozilla.org/en-US/firefox/addon/js-print-setup/")
    }else{

        jsPrintSetup.setSilentPrint(true);

        // set page header/ /设置页面标题

        jsPrintSetup.setOption('headerStrLeft', '');

        jsPrintSetup.setOption('headerStrCenter', '');

        jsPrintSetup.setOption('headerStrRight', '');

        // set empty page footer/ /设置空页页脚

        jsPrintSetup.setOption('footerStrLeft', '');

        jsPrintSetup.setOption('footerStrCenter', '');

        jsPrintSetup.setOption('footerStrRight', '');
        // jsPrintSetup.setPrint()

        jsPrintSetup.printWindow(window);

    }
}
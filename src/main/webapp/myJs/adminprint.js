/**
 * Created by xuejike on 2017/2/28.
 */
function printA4() {
    if(!jsPrintSetup){
        alert("未安装打印插件，点击确定进行安装")
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

        jsPrintSetup.printWindow(window);
    }
}

function printRM() {
    var oldPrintStr="";
    if(!jsPrintSetup){
        window.open("https://addons.mozilla.org/en-US/firefox/addon/js-print-setup/")
    }else{
        oldPrintStr=jsPrintSetup.getPrinter();
        jsPrintSetup.setPrinter("");
        jsPrintSetup.setSilentPrint(true);

        // set page header/ /设置页面标题

        jsPrintSetup.setOption('headerStrLeft', '');

        jsPrintSetup.setOption('headerStrCenter', '');

        jsPrintSetup.setOption('headerStrRight', '');

        // set empty page footer/ /设置空页页脚

        jsPrintSetup.setOption('footerStrLeft', '');

        jsPrintSetup.setOption('footerStrCenter', '');

        jsPrintSetup.setOption('footerStrRight', '');
        jsPrintSetup.setPrint()

        jsPrintSetup.printWindow(window);
        jsPrintSetup.setPrinter(oldPrintStr);
    }
}

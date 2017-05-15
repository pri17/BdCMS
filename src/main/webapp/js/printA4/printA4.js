/**
 * Created by jizhaoqun on 2017/1/21.
 */

function printA4(){

    if(!jsPrintSetup){
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

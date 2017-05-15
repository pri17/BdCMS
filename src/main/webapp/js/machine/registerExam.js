/**
 * Created by admin on 2016/12/16.
 */
/* 设置屏幕宽度 */
//  document.getElementsByTagName('body')[0].style.width = 1280 + 'px';
//  document.getElementsByTagName('body')[0].style.height = 1080 + 'px';
//  document.getElementsByTagName('body')[0].style.fontSize = window.innerWidth / 10 + 'px';
//    document.getElementById("prompt").onclick = function () {
//        this.style.display = "none";
//    };
//    document.getElementsByTagName('button')[0].onclick = function () {
//        window.location.href = "printing.html";
//    }
// function promptDispear() {
//     $("#prompt").hide();
//     //提示区域消失的同时，调用readIdCard()方法，读取身份证信息
//     readIdCard();
// }
// // setTimeout("promptDispear()",3000);//3秒
// //读取身份证信息
// function readIdCard() {
//     var obj = document.getElementById("CardReader1");
//     if (false == isInit) {
//         //设置端口号，1表示串口1，2表示串口2，依此类推；1001表示USB。0表示自动选择
//         obj.setPortNum(0);
//         isInit = true;
//     }
//     //使用重复读卡功能
//     obj.Flag = 0;
//     //obj.BaudRate=115200;
//     //设置照片保存路径，照片文件名：(身份证号).bmp。默认路径为系统临时目录,照片文件名：image.bmp
//     obj.PhotoPath = form1.photoPath.value;
//     //读卡
//     var rst = obj.ReadCard();
//     if (0x90 == rst) {
//         $("#idCardNum").val(obj.CardNo());
//         $("#name").val(obj.NameL());
//         $("#birthday").val(obj.NationL());
//         $("#sex").val(obj.SexL());
//         $("#address").val(obj.Address());
//         $("#memberIcon").attr("src", obj.GetImage());
//     } else {
//         $("#idCardNum").val("");
//         $("#name").val("");
//         $("#birthday").val("");
//         $("#sex").val("");
//         $("#address").val("");
//         $("#memberIcon").attr("src", "");
//     }
//     //清空体检历史信息表中数据
//     $(".examHistoryTableTd").remove();
//
//     //根据身份证号码，提取生日
//     var idCardNum = $("#idCardNum").val();
//     if (idCardNum.length != 18) {
//         $(this).alertmsg('error', "身份证格式错误！");
//         return false;
//     }
//     var year = idCardNum.substring(6, 10);//截取年
//     var month = idCardNum.substring(10, 12);//截取月
//     var day = idCardNum.substring(12, 14);//截取日
//     var birthday = year + "-" + month + "-" + day;
//     $("#birthday").val(birthday);
//     //计算年龄
//     var age = getAges(birthday);
//     $("#age").val(age);
// }
// //行业类别二级选项卡
// var addOptionEa = function () {
//     $(".categoryLevelTwoClass").remove();
//     var pid = $("#eaopenCategory").val();
//     $.getJSON("/wap/getCategoryByPid.do",
//         {
//             pid: pid
//         },
//         function (data) {
//             if (data.categoryList.length > 0) {
//                 var html = "";
//                 for (var i = 0; i < data.categoryList.length; i++) {
//                     var category = data.categoryList[i];
//                     html += "<option class='categoryLevelTwoClass' value='" + category.id + "'>" + category.categoryName + "</option>";
//                 }
//                 $("#eaopenCategoryLevelTwo").append(html);
//             }
//             if (data.examCategoryPackage != null) {
//                 if (data.examPackage != null) {
//                     $("#packagePrice").val(data.examPackage.money);
//                 }
//                 $("#packageId").val(data.examCategoryPackage.packageId);
//             }
//         }
//     );
// };
// //页面加载时，调用一次addOption方法，对行业类别加默认值
// addOptionEa();


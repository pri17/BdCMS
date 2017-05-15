/**
 * Created by admin on 2016/12/14.
 */
/* 设置根元素的font-size大小 */
document.getElementsByTagName('html')[0].style.fontSize = window.innerWidth / 10 + 'px';
document.getElementsByTagName('html')[0].style.height = window.innerHeight + 'px';

//点击按钮向用户手机发送获取验证码
// $("#getCodeBtn").click(function () {
//     var phoneNum = $("#phoneNum").val();
//     if (phoneNum.length<0) {
//         $(this).alertmsg('error', "验证码不能为空!");
//         return;
//     }
//     timedMsg();
//     // $.getJSON("http://"+window.location.host+"/wap/getMobileCode.do",
//     // {
//     //     phoneNum:phoneNum
//     // },
//     //     function (data) {
//     //         if (data.statusCode==300) {
//     //             $(this).alertmsg('error', data.message);
//     //         } else {
//     //             $(this).alertmsg('ok', data.message);
//     //         }
//     //     });
// });

$("#nextBtn").click(function () {
    var openId = $("#openId").val();
    // var idCardNum = $("#idCardNum").val();
    // var name = $("#name").val();
    // var sex = $("#sex").val();
    // var address = $("#address").val();
    var idCardNum = $("#idCardNum").val();
    var company = $("#company").val();
    var phoneNum = $("#phoneNum").val();
    var categoryId = $("#eaopenCategoryLevelTwo").val();
    var packageId = $("#packageId").val();
    // var checkCode = $("#checkCode").val();
    var packagePrice = $("#packagePrice").val();
    // var birthday = $("#year").val()+"-"+$("#month").val()+"-"+$("#date").val();
    // var examTime = $("#eTyear").val()+"-"+$("#eTmonth").val()+"-"+$("#eTdate").val();
    if (openId==null || openId=="") {
        alertNotice("","无法获取用户openId！","error");
        return false;
    }
    // if (name==null || name=="") {
    //     alertNotice("","用户姓名不能为空！","error");
    //     return false;
    // }
    // if (address==null || address=="") {
    //     alertNotice("","现住址不能为空！","error");
    //     return false;
    // }
    if (idCardNum==null || idCardNum=="") {
        alertNotice("","身份证号码不能为空！","error");
        return false;
    } else {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(idCardNum) === false)
        {
            alertNotice("","身份证输入不合法","error");
            return false;
        }
    }
    // 根据用户所输入的身份证号码，截取出生日期和性别
    var birthday = idCardNum.substring(6,14);
    birthday = birthday.substring(0,4)+"-"+birthday.substring(4,6)+"-"+birthday.substring(6,8);
    var sex = birthday.substring(16,17);
    if(sex%2 == 0){
        sex = 0;
    }else{
        sex = 1;
    }
    var age = getAges(birthday);
    if (company==null || company=="") {
        alertNotice("","工作单位不能为空！","error");
        return false;
    }
    if (phoneNum==null || phoneNum=="") {
        alertNotice("","联系电话不能为空！","error");
        return false;
    }
    // if (checkCode==null || checkCode=="") {
    //     alert("验证码不能为空！");
    // }
    // if (birthday==null || birthday=="") {
    //     alert("出生年月不能为空！");
    //     return;
    // }
    // if (examTime==null || examTime=="") {
    //     alert("体检日期不能为空！");
    //     return;
    // }
    //登记体检用户信息，并在成功后跳转到体检项目及费用页面
    $.getJSON("http://"+window.location.host+"/wap/addExamMember.do",
        {
            openId:openId,
            // name:name,
            sex:sex,
            age:age,
            // address:address,
            idCardNum:idCardNum,
            company:company,
            phoneNum:phoneNum,
            categoryId:categoryId,
            packageId:packageId,
            packagePrice:packagePrice,
            birthday:birthday
        },
        function (data) {
            if (data.success!="" && data.success!=null) {
                window.location.replace("http://"+window.location.host+"/wap/wapSaveSuccess.do");
            } else if (data.error!="" && data.error!=null) {
                alertNotice("",data.error,"error");
            } else if (data.unOverDueError!="" && data.unOverDueError!=null) {
                // alertUnFinishNotice("",data.unOverDueError,"error");
                swal({
                        title: "",
                        text: data.unOverDueError,
                        type: "info",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        cancelButtonText: "取消",
                        confirmButtonText: "确定",
                        closeOnConfirm: false
                    },
                    function(){
                        //健康证有效期超过三个月，继续体检登记
                        $.getJSON("http://"+window.location.host+"/wap/addExam.do",
                            {
                                openId:openId,
                                // name:name,
                                sex:sex,
                                age:age,
                                // address:address,
                                idCardNum:idCardNum,
                                company:company,
                                phoneNum:phoneNum,
                                categoryId:categoryId,
                                packageId:packageId,
                                packagePrice:packagePrice,
                                birthday:birthday
                            },
                            function (data) {
                                if (data.success!="" && data.success!=null) {
                                    window.location.replace("http://"+window.location.host+"/wap/wapSaveSuccess.do");
                                } else {
                                    swal("操作失败!", "","error");
                                }
                            });
                    });
            } else if (data.unFinishError!="" && data.unFinishError!=null) {
                // alertUnFinishNotice("",data.unFinishError,"error");
                swal({
                        title: "",
                        text: data.unFinishError,
                        type: "info",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        cancelButtonText: "取消",
                        confirmButtonText: "确定",
                        closeOnConfirm: false
                    },
                    function(){
                        //健康证有效期超过三个月，继续体检登记
                        $.getJSON("http://"+window.location.host+"/wap/addExam.do",
                            {
                                openId:openId,
                                // name:name,
                                sex:sex,
                                age:age,
                                // address:address,
                                idCardNum:idCardNum,
                                company:company,
                                phoneNum:phoneNum,
                                categoryId:categoryId,
                                packageId:packageId,
                                packagePrice:packagePrice,
                                birthday:birthday
                            },
                            function (data) {
                                if (data.success!="" && data.success!=null) {
                                    window.location.replace("http://"+window.location.host+"/wap/wapSaveSuccess.do");
                                } else {
                                    swal("操作失败!", "","error");
                                }
                            });
                    });
            }
        });
});
//通过出生日期计算年龄
var getAges = function(str){
    var yy = str.substring(0, 4);//截取年
    var mm = str.substring(5, 7);//截取月
    var dd = str.substring(8, 10);//截取日
//        console.info(yy+","+mm+","+dd);
    var days = new Date();
    var gdate = days.getDate();
    var gmonth = days.getMonth();
    var gyear = days.getYear();
    if (gyear < 2000) gyear += 1900;
    var age = gyear - yy;
    if ((mm == (gmonth + 1)) && (dd <= parseInt(gdate))) {
        age = age;
    } else {
        if (mm <= (gmonth)) {
            age = age;
        } else {
            age = age - 1;
        }
    }
    if (age == 0)
        age = age;
    return age;
//        var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
//        if(r==null)
//            return false;
//        var d = new Date(r[1], r[3]-1, r[4]);
//        if (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]) {
//            var Y = new Date().getFullYear();
////            console.info("年龄 = "+ (Y-r[1]) +" 周岁");
//            return Y-r[1];
//        } else {
//            $(this).alertmsg("输入的日期格式错误！");
//            return false;
//        }
}
function alertNotice(title,text,type) {
    swal({
        title: title,
        text: text,
        type: type,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定"
    });
}

function alertUnFinishNotice(title,text,type) {
    swal({
            title: title,
            text: text,
            type: type,
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            cancelButtonText: "取消",
            confirmButtonText: "确定",
            closeOnConfirm: false
        },
        function(){
            swal("Deleted!", "Your imaginary file has been deleted.", "success");
        });
}

var countdown=60;
function settime(val) {
    if (countdown == 0) {
        val.removeAttribute("disabled");
        // val.value="免费获取验证码";
        document.getElementById("getCodeBtn").innerText="获取验证码";
        countdown = 60;
        return;
    } else {
        val.setAttribute("disabled", true);
        // val.value="重新发送(" + countdown + ")";
        document.getElementById("getCodeBtn").innerText="重新发送(" + countdown + ")";
        countdown--;
    }
    setTimeout(function() {
        settime(val)
    },1000)
}

//行业类别二级选项卡
var addOptionEa = function () {
    $(".categoryLevelTwoClass").remove();
    var pid = $("#eaopenCategory").val();
    $.getJSON("/wap/getCategoryByPid.do",
        {
            pid:pid
        },
        function (data) {
            if (data.categoryList.length>0) {
                var html = "";
                for (var i=0;i<data.categoryList.length;i++) {
                    var category = data.categoryList[i];
                    html += "<option class='categoryLevelTwoClass' value='"+category.id+"'>"+category.categoryName+"</option>";
                }
                $("#eaopenCategoryLevelTwo").append(html);
            }
            if (data.examCategoryPackage!=null) {
                if (data.examPackage!=null) {
                    $("#packagePrice").val(data.examPackage.money);
                }
                $("#packageId").val(data.examCategoryPackage.packageId);
            }
        }
    );
};
//页面加载时，调用一次addOption方法，对行业类别加默认值
addOptionEa();

/*加载年份*/
function years(obj, Cyear) {
    var len = 100; // select长度,即option数量
    var selObj = document.getElementById(obj);
    var selIndex = len - 1;
    var newOpt; // OPTION对象

    // 循环添加OPION元素到年份select对象中
    for (i = 1; i <= len; i++) {
        if (selObj.options.length != len) { // 如果目标对象下拉框升度不等于设定的长度则执行以下代码
            newOpt = window.document.createElement("OPTION"); // 新建一个OPTION对象
            newOpt.text = Cyear - len + i; // 设置OPTION对象的内容
            newOpt.value = Cyear - len + i; // 设置OPTION对象的值
            selObj.options.add(newOpt, i); // 添加到目标对象中
        }

    }
}

function months(){
    var month = document.getElementById("month");
    month.length = 0;
    for (i = 1; i < 13; i++) {
        month.options.add(new Option(i, i));
    }

}

function change_date(){
    // var birthday = document.birthday;
    var year  = $("#year").val();
    var month = $("#month").val();
    var date   = document.getElementById("date");
    vYear  = parseInt(year);
    vMonth = parseInt(month);
    date.options.length=0;

    //根据年月获取天数
    var max = (new Date(vYear,vMonth, 0)).getDate();
    for (var i=1; i <= max; i++) {
        date.options.add(new Option(i, i));
    }
}

function eTmonths(){
    var month = document.getElementById("eTmonth");
    month.length = 0;
    for (i = 1; i < 13; i++) {
        month.options.add(new Option(i, i));
    }

}

function eTchange_date(){
    var year  = $("#eTyear").val();
    var month = $("#eTmonth").val();
    var eTdate   = document.getElementById("eTdate");
    vYear  = parseInt(year);
    vMonth = parseInt(month);
    eTdate.options.length=0;

    //根据年月获取天数
    var max = (new Date(vYear,vMonth, 0)).getDate();
    for (var i=1; i <= max; i++) {
        var html="";
        html += "<OPTION>"+i+"</OPTION>";
        $("#eTdate").append(html);
    }
}
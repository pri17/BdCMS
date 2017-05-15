// startPrint(examECard.examMember.name,examECard.examMember.sexStr,examECard.memberAge,
//     examECard.examMember.idCardNum,examECard.eCardNumber,
//     examECard.issueTimeStr,examECard.examNumber,examECard.memberIcon,examECard.qrCodeUrl);
function startPrint(xingming, xingbie,memberAge, shenfenzhenghao, jiankangzhenghao, tijianhao ,fazhengqiri, memberIcon,qrCodeUrl)
{
    //静态字体参数
    mfc.StaticFontName = "宋体";
    mfc.StaticFontSize = 40;//字体大小
    mfc.StaticFontWeight = 400;//粗细  400为正常  800为加粗

   //动态字体参数
    mfc.FontName = "宋体";
    mfc.FontSize = 40;//字体大小
    mfc.FontWeight = 800;//粗细  400为正常  800为加粗
   
    //静态姓名
    mfc.StaticName = "姓名：";
    mfc.StaticName_X = 25;
    mfc.StaticName_Y = 200;

    //动态姓名
    mfc.Name=xingming;
    mfc.Name_X=150;
    mfc.Name_Y=200;

    //静态性别
    mfc.StaticSex="性别：";
    mfc.StaticSex_X=25;
    mfc.StaticSex_Y=260;
    
    //动态性别
    mfc.Sex=xingbie;
    mfc.Sex_X=150;
    mfc.Sex_Y=260;

    //静态年龄
    mfc.StaticAge="年龄：";
    mfc.StaticAge_X=25;
    mfc.StaticAge_Y=320;

    //动态年龄
    mfc.Age=memberAge;
    mfc.Age_X=150;
    mfc.Age_Y=320;


    //静态身份证号码
    mfc.StaticIDCardNo = "身份证号码:";
    mfc.StaticIDCardNo_X = 24.5;
    mfc.StaticIDCardNo_Y = 380;

    //动态身份证号码
    mfc.IDCardNo=shenfenzhenghao;
    mfc.IDCardNo_X=260;
    mfc.IDCardNo_Y=380;


    //静态编号
    mfc.StaticNo="编号：";
    mfc.StaticNo_X=25;
    mfc.StaticNo_Y=440;

    //动态编号
    mfc.No="苏"+ jiankangzhenghao +"号";
    mfc.No_X=150;
    mfc.No_Y=440;
    
    
    //静态发证日期
    mfc.StaticGrantDate = "发证日期：";
    mfc.StaticGrantDate_X = 25;
    mfc.StaticGrantDate_Y = 500;

    //动态发证日期
    mfc.GrantDate=fazhengqiri;
    mfc.GrantDate_X=220;
    mfc.GrantDate_Y=500;


    //静态发证机构
    mfc.StaticGrantDept="发证机构：";
    mfc.StaticGrantDept_X=25;
    mfc.StaticGrantDept_Y=560;
    
    //动态发证机构
    mfc.GrantDept="常熟市疾病预防控制中心";
    mfc.GrantDept_X=220;
    mfc.GrantDept_Y=560;

    //动态健康证号
    mfc.HealthNo=tijianhao;
    mfc.HealthNo_X=750;
    mfc.HealthNo_Y=540;


    mfc.PhotoURL = memberIcon;  //本地照片
    mfc.Photo_X = 62.7;         //照片X坐标
    mfc.Photo_Y = 16.6;          //照片Y坐标8
    mfc.Photo_CX = 240;         //照片宽度
    mfc.Photo_CY =320;          //照片高度


    mfc.QRURL = qrCodeUrl;  //本地二维码图片
    mfc.QR_X = 35;//二维码X坐标
    mfc.QR_Y = 16;//二维码照片Y坐标
    mfc.QR_CX = 180;//二维码照片宽度
    mfc.QR_CY =180;//二维码照片高度

    

    return mfc.PrintCard;
}

function checkPrint(){
    var dwRet = mfc.CheckPrinterStatus();
    return dwRet;
}


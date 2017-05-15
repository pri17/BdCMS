/**
 * Created by Administrator on 2016/7/26.
 */
$(document).ready(function(){
//    判断表单是否为空
    $(".login-bt").click(function(){
        var error = $('.ipt-remind');
        var flag = true;

        $(".validate-ipt").each(function(index){
            if($(this).val() == '') {
                error.eq(index).css("display","block");
                flag = false;
            }else {
                error.eq(index).css("display","none");
                if(!flag){
                    flag = false;
                }
            }
        });

        if(flag){


            var param = {username:$("#username").val(),password:$("#password").val(),code:$("#code").val(),uuid:$("#uuid").val()};


            $.post("/shopman/public/loginSave1.do",param, function (data) {
                data= $.parseJSON(data);
                if(data.statusCode==200){
                    window.location.href="/shopman/index.do";
                }else{
                    alert(data.message);
                }
            }
            ); $('#ypb_errormessage').text(msg).show();

            rePicCode($('.ypb_yanzhenimg'));

        }

    });
    $(".validate-ipt").focus(function(){
        $(this).parent().children(".ipt-remind").css("display","none");
    });
});
/**
 * Created by jizhaoqun on 2016/12/11.
 */
var host="http://"+window.location.host;

var projectPriceVM = avalon.define({
    $id: "projectPrice",
    projectMoneyValue: "",
    checkedIds:[]
});


projectPriceVM.$watch("checkedIds.length",function observe(a, b) {
    // console.log(this.checkedIds);
    $.getJSON(host+'/admin/businessSetting/examPackage/countPrice.do',
        {
            projectIds:this.checkedIds.toString()
        },
        function (totalPrice) {
            projectPriceVM.projectMoneyValue=totalPrice;
        });
    
});


var userRoleVM = avalon.define({
    $id:"userRole",
    menusList:"",
    checkedList:[],
    tree:[],
    checkedAll:function(el) {
        if(this.checkedList.indexOf(parseInt(el.id))<0){
            //之前未选中，操作为选中操作
            userRoleCheckPush(el.id);
            userRoleCheckPush(el.parentId);
            for(var i=0;i<el.childrend.length;i++){
                var c=el.childrend[i];
                userRoleCheckPush(c.id);
                for(var j=0;j<c.childrend.length;j++){
                    var t=c.childrend[j];
                    userRoleCheckPush(t.id);
                }
            }

        }else{
            //之前选中，操作为，取消选中
            this.checkedList.$remove(parseInt(el.id));
            for (var i=0;i<el.childrend.length;i++){
                var c=el.childrend[i];
                this.checkedList.$remove(parseInt(c.id));
                for(var j=0;j<c.childrend.length;j++){
                    var t=c.childrend[j];
                    this.checkedList.$remove(parseInt(t.id));
                }
            }
            for(var i=0;i<el.childrend.length;i++){
                var c=el.childrend[i];
                this.checkedList.$remove(parseInt(c.id));
                for(var j=0;j<c.childrend.length;j++){
                    var t=c.childrend[j];
                    this.checkedList.$remove(parseInt(t.id));
                }
            }
        }
    },

    checkedSecond:function (se) {//如果二级菜单都去除了，则将一级菜单也置为未选中
        if(this.checkedList.indexOf(parseInt(se.id))<0){
            userRoleCheckPush(se.id);
            userRoleCheckPush(se.parentId);
            for(var j=0;j<se.childrend.length;j++){
                var t=se.childrend[j];
                userRoleCheckPush(t.id);
            }
        }else {
            this.checkedList.$remove(parseInt(se.id));
            if (this.checkedList.indexOf(parseInt(se.id))<0){
                for (var i=0;i<se.childrend.length;i++){
                    var t=se.childrend[i];
                    this.checkedList.$remove(parseInt(t.id));
                }
            }
            for(var j=0;j<se.childrend.length;j++){
                var t=se.childrend[j];
                this.checkedList.$remove(parseInt(t.id));
            }
        }
    },


    checkedThir:function (th) {//如果第三季每个模块全部取消则将上级目录页取消

        var thirdRowSelected=[];
        var secondRowSelected = [];

        if(this.checkedList.indexOf(parseInt(th.id))<0){
            userRoleCheckPush(th.id);
            userRoleCheckPush(th.parentId)

        }else {



            var parentId = th.parentId;

            var rootTree = this.tree[0];


            for(var i=0;i<rootTree.childrend.length;i++) {

                var secondTree = rootTree.childrend[i];


                    for (var j=0;j<secondTree.childrend.length;j++) {

                        // console.log("secondTree.id:==="+secondTree.childrend[j].id);

                        if (parentId == secondTree.childrend[j].id){

                            var thirdTree = secondTree.childrend[j];


                            thirdRowSelected = [];

                            for(var k=0;k<thirdTree.childrend.length;k++){


                                var thirdId = thirdTree.childrend[k].id;

                                if (this.checkedList.indexOf(thirdId)>=0){
                                    thirdRowSelected.push(thirdId);
                                }


                            }





                            this.checkedList.$remove(parseInt(th.id));

                            thirdRowSelected.$remove(th.id);

                            // var isDeleteParent = false;

                            for(var x=0;x<thirdRowSelected.length;x++){
                                var rowId = thirdRowSelected[x];

                                if (this.checkedList.indexOf(rowId)>=0){
                                    // isDeleteParent = false;
                                    // thirdRowSelected.push(rowId);

                                }else{

                                    thirdRowSelected.$remove(rowId);
                                }
                            }

                            if(thirdRowSelected.length==0){

                                this.checkedList.$remove(parentId);
                            }



                        }





                    }




            }

            // var parentTree = rootTree.childrend;
            //
            // var thirdTree ;
            //
            // for(var i=0;i<parentTree.length;i++){
            //
            //     if (parentTree.indexOf(parentId)==-1){
            //
            //         thirdTree = parentTree.childrend[i];
            //     }
            //
            // }
            //
            // this.checkedList.$remove(parseInt(th.id));
            //
            //
            // for (var k=0;k<this.checkedList.length;k++){
            //
            //     for (var j=0;j<thirdTree.length;j++){
            //
            //         if (this.checkedList.indexOf(thirdTree[j])>=0){
            //
            //         }
            //     }
            // }


        }
    }

});
function userRoleCheckPush(id) {
    if(userRoleVM.checkedList.indexOf(parseInt(id))==-1){
        userRoleVM.checkedList.push(parseInt(id));
    }

}

$.getJSON(host+'/admin/menu/getMenuTree.do',function (treeData, result) {
    userRoleVM.tree = treeData;
});

userRoleVM.$watch("checkedList.length",function observe(newVal, oldVal,cc) {
    // console.log(this.menusList);
    this.menusList=JSON.stringify(this.checkedList.$model);
    // console.log("a="+a+"-->b="+b);
    // if(newVal>oldVal){
    //     //add
    //     var newId=this.checkedList[newVal-1];
    //     var rootTree=this.tree[0];
    //     for(var i=0;i<rootTree.childrend.length;i++){
    //         var tree=rootTree.childrend[i];
    //         if(tree.id==newId){
    //             for(var k=0;k<tree.childrend.length;k++){
    //
    //                 if(this.checkedList.indexOf(tree.childrend[k].id)==-1){
    //                     this.checkedList.push(tree.childrend[k].id)
    //                 }
    //
    //             }
    //         }else{
    //             for(var j=0;j<tree.childrend.length;j++){
    //                 var ctree=tree.childrend[j]
    //                 if(ctree.id==newId){
    //                     for(var k=0;k<ctree.childrend.length;k++){
    //
    //                         if(this.checkedList.indexOf(ctree.childrend[k].id)==-1){
    //                             this.checkedList.push(ctree.childrend[k].id)
    //                         }
    //
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }else{
    //     //remove
    //     var newId=this.checkedList[newVal-1];
    //     var rootTree=this.tree[0];
    //     for(var i=0;i<rootTree.childrend.length;i++){
    //         var tree=rootTree.childrend[i];
    //         if(tree.id==newId){
    //             for(var k=0;k<tree.childrend.length;k++){
    //                 this.checkedList.$remove(tree.childrend[k].id)
    //             }
    //         }else{
    //             for(var j=0;j<tree.childrend.length;j++){
    //                 var ctree=tree.childrend[j]
    //                 if(ctree.id==newId){
    //                     for(var k=0;k<ctree.childrend.length;k++){
    //                         this.checkedList.$remove(ctree.childrend[k].id)
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }





});
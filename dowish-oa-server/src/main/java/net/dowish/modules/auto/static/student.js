$(function () {
    $("#jqGrid").jqGrid({
        url: '../student/list',
        datatype: "json",
        colModel: [
							                    { label: '${column.attrname}', name: '${column.attrname}', index: 'id', width: 50, key: true },
											                    { label: '姓名', name: '${column.attrname}', index: 'name', width: 80 }, 
											                    { label: '地址', name: '${column.attrname}', index: 'address', width: 80 }
							        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
student: {}
},
methods: {
    query: function () {
        vm.reload();
    },
    add: function(){
        vm.showList = false;
        vm.title = "新增";
        vm.student = {};
    },
    update: function (event) {
        var $pk.attrname = getSelectedRow();
        if($pk.attrname == null){
            return ;
        }
        vm.showList = false;
        vm.title = "修改";

        vm.getInfo(${pk.attrname})
    },
    saveOrUpdate: function (event) {
        var url = vm.student.${pk.attrname} == null ? "../student/save" : "../student/update";
        $.ajax({
            type: "POST",
            url: url,
            contentType: "application/json",
            data: JSON.stringify(vm.student),
            success: function(r){
                if(r.code === 0){
                    alert('操作成功', function(index){
                        vm.reload();
                    });
                }else{
                    alert(r.msg);
                }
            }
        });
    },
    del: function (event) {
        var ${pk.attrname}s = getSelectedRows();
        if(${pk.attrname}s == null){
            return ;
        }

        confirm('确定要删除选中的记录？', function(){
            $.ajax({
                type: "POST",
                url: "../student/delete",
                contentType: "application/json",
                data: JSON.stringify(${pk.attrname}s),
                success: function(r){
                    if(r.code == 0){
                        alert('操作成功', function(index){
                            $("#jqGrid").trigger("reloadGrid");
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        });
    },
    getInfo: function(${pk.attrname}){
        $.get("../student/info/"+${pk.attrname}, function(r){
            vm.student = r.student;
        });
    },
    reload: function (event) {
        vm.showList = true;
        var page = $("#jqGrid").jqGrid('getGridParam','page');
        $("#jqGrid").jqGrid('setGridParam',{
            page:page
        }).trigger("reloadGrid");
    }
}
});
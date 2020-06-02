<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        var url;

        function searchCase() {
            $("#dg").datagrid('load', {
                "userName": $("#s_userName").val()
            });
        }

        function deleteCase() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的数据！");
                return;
            }
            var strIds = [];
            for (var i = 0; i < selectedRows.length; i++) {
                strIds.push(selectedRows[i].id);
            }
            var ids = strIds.join(",");
            $.messager.confirm("系统提示", "您确认要删除这<font color=red>"
                + selectedRows.length + "</font>条数据吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/case/delete.do", {
                        ids: ids
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "数据已成功删除！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "数据删除失败！");
                        }
                    }, "json");
                }
            });

        }

        function openCaseAddDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "添加用户信息");
            url = "${pageContext.request.contextPath}/user/save.do";
            $("#roleId").combobox("select","");
        }

        function saveUser() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    $.messager.alert("系统提示", "保存成功");
                    resetValue();
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }
        function closeCase(){
            const check = $("#dataSaveCheck").is(':checked');
            let tips = "确定要结案吗?";
            if(check){
                tips = "确定要结案，并移除当事人信息吗？"
                url += "&clear=1";
            }
            $.messager.confirm("系统提示", tips, function (r) {
                if (r) {
                    $.get(url, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "结案成功");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "数据操作失败！");
                        }
                    }, "json");
                }
            });
        }

        function openCaseModifyDialog() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length != 1) {
                $.messager.alert("系统提示", "请选择一条要编辑的数据！");
                return;
            }
            var row = selectedRows[0];
            $("#dlg").dialog("open").dialog("setTitle", "编辑用户信息");
            $('#fm').form('load', row);
            $("#password").val("******");
            url = "${pageContext.request.contextPath}/user/save.do?id=" + row.id;
        }
        function openCaseCloseDialog(idx){
            $("#caseClsDlg").dialog("open").dialog("setTitle", "结案");
            url = "${pageContext.request.contextPath}/case/solve.do?id=" + idx;
        }
        function resetValue() {
            $("#userName").val("");
            $("#password").val("");
        }

        function closeCaseDialog() {
            $("#caseClsDlg").dialog("close");
        }
        function rowFormatter(value, row, index) {

            return '<a href="javascript:openCaseEditDialog(' + value +')" >编辑</a> ' + '   ' +
                '<a href="javascript:openCaseCloseDialog(' + value + ')">结案</a> ';

        }
        $(function(){
            $("#roleId").combobox({
                url : "${pageContext.request.contextPath}/role/roleConfig.do",
                method: "get",
                valueField: "roleId",
                textField: "roleName",
                required : true,
                editable : false,
            });
        })
    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="案件信息" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/case/list.do" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
<%--        <th field="id" width="50" align="center">ID</th>--%>
        <th field="caseCode" width="100" align="center">案号</th>
        <th field="categoryShow" width="100" align="center">类别</th>
        <th field="clientNameShow" width="100" align="center">委托人</th>
        <th field="opponentNameShow" width="100" align="center">对方当事人</th>
        <th field="createName" width="100" align="center">录入人</th>
        <th field="dealer" width="100" align="center">承办人</th>
        <th field="statusShow" width="100" align="center">状态</th>
        <th field="id" width="100" formatter="rowFormatter">操作</th>

    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openCaseAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加</a><a
            href="javascript:deleteCase()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;案号：&nbsp;<input type="text" id="s_caseCode" size="20" placeholder="请输入案号"
                               onkeydown="if(event.keyCode===13) searchCase()"/>
            委托人：<input type="text" id="s_client" size="20" placeholder="请输入委托人姓名"
                   onkeydown="if(event.keyCode===13) searchCase()"/>
            对方当事人：<input type="text" id="s_opponent" size="20" placeholder="请输入对方当事人姓名"
                       onkeydown="if(event.keyCode===13) searchCase()"/>
            录入人：<input type="text" id="s_create" size="20" placeholder="请输入录入人姓名或电话"
                         onkeydown="if(event.keyCode===13) searchCase()"/>
        <a href="javascript:searchCase()" class="easyui-linkbutton"
                iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>
<div id="caseClsDlg" class="easyui-dialog"
     style="width: 280px;height:170px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="messager-body">
        <label>
            <input type="checkbox"   id="dataSaveCheck"   />将当事人信息移出利冲校验
        </label>
    </div>
    <div id="caseClsDlg-buttons">
        <a href="javascript:closeCase()" class="easyui-linkbutton"
           iconCls="icon-ok">确定</a>
        <a href="javascript:closeCaseDialog()" class="easyui-linkbutton"
           iconCls="icon-cancel">取消</a>
    </div>
</div>
<div id="dlg" class="easyui-dialog"
     style="width: 620px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="userName" name="userName"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="realName" name="realName"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="password" id="password" name="password"
                           class="easyui-validatebox" required="true"/>&nbsp;<span
                        style="color: red; ">*</span>
                </td>
            </tr>
            <tr>
                <td>角色：</td>
                <td>
                    <input id="roleId" class="easyui-combobox" name="roleId" editable="false" value="请选择角色">
                    <span style="color: red; ">*</span>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:saveUser()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeUserDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>
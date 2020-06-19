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
        var currentRow = [];
        let currentUserId  = 0;

        function searchUser() {
            $("#dg").datagrid('load', {
                "userName": $("#s_userName").val()
            });
        }

        function deleteUser() {
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
                    $.post("${pageContext.request.contextPath}/user/delete.do", {
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

        function openUserAddDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "添加用户信息");
            url = "${pageContext.request.contextPath}/user/save.do";
            $("#roleId").combobox("select","");
        }


        function saveUser() {
            $("#fm").form("submit", {
                url: url,
                contentType:"json",
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    const data = $.parseJSON(result);
                    if (data.success) {
                        $.messager.alert("系统提示", "保存成功");
                        resetValue();
                        $("#dlg").dialog("close");
                        $("#dg").datagrid("reload");
                    }
                    else{
                        if(data.msg != null){
                            $.messager.alert("系统提示", data.msg);
                        }
                        else{
                            $.messager.alert("系统提示", "数据操作失败，请重试");
                        }
                    }
                }
            });
        }

        function saveUser2() {
            $("#fm2").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    $.messager.alert("系统提示", "保存成功");
                    resetValue();
                    $("#dlgEdit").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function rowFormatter(value, row, index) {
            currentRow[index] = row;
            return '<a href="javascript:openUserModifyDialog(' + index + ')">编辑</a>';
        }
        function openUserModifyDialog(index) {
            var row = currentRow[index];
            $("#dlgEdit").dialog("open").dialog("setTitle", "编辑用户信息");
            $('#fm2').form('load', row);
            currentUserId = row.id;
            url = "${pageContext.request.contextPath}/user/save.do?id=" + row.id;
        }
        function resetValue() {
            $("#userName").val("");
            $("#realName").val("");
            $("#password").val("");
        }

        function closeUserDialog() {
            $("#dlg").dialog("close");
            resetValue();
        }

        function closeUserDialog2() {
            $("#dlgEdit").dialog("close");
            resetValue();
        }

        function modifyPassword(){
            $("#passDlg").dialog("open").dialog("setTitle", "修改密码");
        }
        function modify(){
            let pass = $("#pwd").val();
            let passRepeat = $("#pwd-repeat").val();
            if(pass !== passRepeat){
                $.messager.alert("系统提示", "两次密码不一致！");
                return ;
            }
            $.post("${pageContext.request.contextPath}/user/modifyPassword.do", {
                id: currentUserId,
                password: pass
            }, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "修改成功");
                    $("#pwd").val("");
                    $("#pwd-repeat").val("");
                    $("#passDlg").dialog("close");
                } else {
                    $.messager.alert("系统提示", "修改失败，请稍后重试");
                }
            }, "json");
        }
        function closeModifyDialog(){
            $("#pwd").val("");
            $("#pwd-repeat").val("");
            $("#passDlg").dialog("close");
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
            $("#roleId2").combobox({
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
<table id="dg" title="用户管理" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/user/list.do" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="userName" width="100" align="center">用户名</th>
        <th field="realName" width="100" align="center">真实姓名</th>
        <th field="roleName" width="100" align="center">角色</th>
        <th field="id" width="100" formatter="rowFormatter">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openUserAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加</a> <a
            href="javascript:deleteUser()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;用户名：&nbsp;<input type="text" id="s_userName" size="20"
                               onkeydown="if(event.keyCode===13) searchUser()"/> <a
            href="javascript:searchUser()" class="easyui-linkbutton"
            iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 620px;height:270px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>登录帐号：</td>
                <td><input type="text" id="userName" name="userName"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>真实姓名：</td>
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

<div id="dlgEdit" class="easyui-dialog"
     style="width: 620px;height:270px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons2">
    <form id="fm2" method="post">
        <table cellspacing="8px">
            <tr>
                <td>登录帐号：</td>
                <td><input type="text" id="userName2" name="userName"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>真实姓名：</td>
                <td><input type="text" id="realName2" name="realName"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="button" value="修改密码" onClick="modifyPassword();"/>
                </td>
            </tr>
            <tr>
                <td>角色：</td>
                <td>
                    <input id="roleId2" class="easyui-combobox" name="roleId" editable="false" value="请选择角色">
                    <span style="color: red; ">*</span>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons2">
    <a href="javascript:saveUser2()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeUserDialog2()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>


<div id="passDlg" class="easyui-dialog"
     style="width: 270px;height:210px;padding: 10px 20px" closed="true"
     buttons="#password-buttons">
    <div class="messager-body">
        <div>
            <label>
                新密码：<input type="password" id="pwd" data-options="validType:'length[6,32]'"
                           class="easyui-validatebox" required="true"/>
            </label>
        </div>
        <div>
            <label>
                确认密码：<input type="password" id="pwd-repeat" data-options="validType:'length[6,32]'"
                            class="easyui-validatebox" required="true"/>
            </label>
        </div>
    </div>
</div>

<div id="password-buttons">
    <a href="javascript:modify()" class="easyui-linkbutton"
       iconCls="icon-ok">确定</a>
    <a href="javascript:closeModifyDialog()" class="easyui-linkbutton"
       iconCls="icon-cancel">取消</a>
</div>
</body>
</html>
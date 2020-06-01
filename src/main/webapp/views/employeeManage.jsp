<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/21
  Time: 21:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>employeeManager</title>
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
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/ueditor/ueditor.config.js">

    </script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/ueditor/ueditor.all.min.js">

    </script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/common.js"></script>

</head>
<body style="margin:1px;" id="ff">
<table id="dg" title="员工信息管理" class="easyui-datagrid" pagination="true"
       rownumbers="true" fit="true" data-options="pageSize:10"
       url="${pageContext.request.contextPath}/employee/list.do" toolbar="#tb">
    <thead data-options="frozen:true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="10%" align="center" hidden="true">编号</th>
        <th field="name" width="80" align="center">姓名</th>
        <th field="mobile" width="100" align="center">手机号</th>
        <th field="isPartyMemberStr" width="75" align="center">是否党员</th>
        <th field="isMarriedStr" width="60" align="center">婚否</th>
        <th field="position" width="60" align="center">职位</th>
        <th field="nationality" width="60" align="center">民族</th>
        <th field="profession" width="90" align="center">专业</th>
        <th field="remarks" width="200" align="center">备注</th>
<%--        <th field="content" width="70" align="center"--%>
<%--            formatter="formatHref">操作--%>
<%--        </th>--%>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openEmployeeAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加</a> <a
            href="javascript:openEmployeeModifyDialog()"
            class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> <a
            href="javascript:deleteEmployee()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;关键词：&nbsp;<input type="text" id="keyword" size="20"
                              onkeydown="if(event.keyCode==13) searchEmployee()"/>&nbsp; <a
            href="javascript:searchEmployee()" class="easyui-linkbutton"
            iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 850px;height:555px;padding: 10px 20px; position: relative; z-index:1000;"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>姓名：</td>
                <td><input type="text" id="name" name="name"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>手机：</td>
                <td><input type="text" id="mobile" name="mobile"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>是否党员：</td>
                <td>
                    <select id="isPartyMember" class="easyui-combobox" name="isPartyMember" editable="false">
                    <option value="0">不是</option>
                    <option value="1">是</option>
                </select>
                </td>
            </tr>
            <tr>
                <td>婚姻状况：</td>
                <td>
                    <select id="isMarried" class="easyui-combobox" name="isMarried" editable="false">
                        <option value="0">未婚</option>
                        <option value="1">已婚</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>职位：</td>
                <td><input type="text" id="position" name="position"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>民族：</td>
                <td><input type="text" id="nationality" name="nationality"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>专业：</td>
                <td><input type="text" id="profession" name="profession"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td><input type="text" id="remarks" name="remarks"
                           class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveEmployee()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeEmployeeDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>


<script type="text/javascript">
    var url;
    function searchEmployee() {
        $("#dg").datagrid('load', {
            "keyword": $("#keyword").val(),
        });
    }

    function deleteEmployee() {
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
        $.messager
            .confirm(
                "系统提示",
                "您确认要删除<font color=red>" + selectedRows.length
                + "</font>条数据吗？",
                function (r) {
                    if (r) {
                        $
                            .post(
                                "${pageContext.request.contextPath}/employee/delete.do",
                                {
                                    ids: ids
                                },
                                function (result) {
                                    if (result.success) {
                                        $.messager.alert(
                                            "系统提示",
                                            "数据已成功删除！");
                                        $("#dg").datagrid(
                                            "reload");
                                    } else {
                                        $.messager.alert(
                                            "系统提示",
                                            "数据删除失败！");
                                    }
                                }, "json");
                    }
                });

    }

    function openEmployeeAddDialog() {
        $("#dlg").dialog("open").dialog("setTitle", "添加员工");
        url = "${pageContext.request.contextPath}/employee/add.do";
    }

    function saveEmployee() {
        $("#fm").form("submit", {
            url: url,
            onSubmit: function () {
                return $(this).form("validate");
            },
            success: function (result) {
                $.messager.alert("系统提示", "保存成功");
                $("#dlg").dialog("close");
                $("#dg").datagrid("reload");
                resetValue();
            }
        });
    }

    function openEmployeeModifyDialog() {
        var selectedRows = $("#dg").datagrid('getSelections');
        if (selectedRows.length != 1) {
            $.messager.alert("系统提示", "请选择一条要编辑的数据！");
            return;
        }
        var row = selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle", "修改信息");
        $('#fm').form('load', row);
        url = "${pageContext.request.contextPath}/employee/update.do?id="
            + row.id;
    }

    <%--function formatHref(val, row) {--%>
    <%--    return "<a href='${pageContext.request.contextPath}/article.html?id=" + row.id + "' target='_blank'>查看详情</a>";--%>
    <%--}--%>

    function resetValue() {
        $("#name").val("");
        $("#mobile").val("");
        $("#position").val("");
        $("#nationality").val("");
        $("#profession").val("");
        $("#remarks").val("");
        $("#isPartyMember").val("");
    }

    function closeEmployeeDialog() {
        $("#dlg").dialog("close");
        resetValue();
    }
</script>
</body>
</html>

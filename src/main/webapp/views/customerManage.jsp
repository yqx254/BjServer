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
    <title>customerManager</title>
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
<table id="dg" title="客户信息管理" class="easyui-datagrid" pagination="true"
       rownumbers="true" fit="true" data-options="pageSize:10"
       url="${pageContext.request.contextPath}/customer/list.do" toolbar="#tb">
    <thead data-options="frozen:true">
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="10%" align="center" hidden="true">编号</th>
        <th field="name" width="80" align="center">姓名</th>
        <th field="mobile" width="100" align="center">手机号</th>
        <th field="require" width="150" align="center">客户需求</th>
        <th field="dateStr" width="100" align="center">来访时间</th>
        <th field="empName" width="100" align="center">接待人</th>
        <%--        <th field="content" width="70" align="center"--%>
        <%--            formatter="formatHref">操作--%>
        <%--        </th>--%>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openCustomerAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加</a> <a
            href="javascript:openCustomerModifyDialog()"
            class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> <a
            href="javascript:deleteCustomer()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;关键词：&nbsp;<input type="text" id="keyword" size="20"
                               onkeydown="if(event.keyCode==13) searchCustomer()"/>&nbsp; <a
            href="javascript:searchCustomer()" class="easyui-linkbutton"
            iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 850px;height:555px;padding: 10px 20px; position: relative; z-index:1000;"
     closed="true" buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>客户姓名：</td>
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
                <td>客户需求：</td>
                <td>
                    <input type="text" id="require" name="require"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>接待人：</td>
                <td>
                    <input type="text" id="empId" name="empId"
                           class="easyui-validatebox" required="true"/>
                </td>
            </tr>
            <tr>
                <td>到店时间：</td>
                <td>
                    <input type="text" id="dateStr" name="dateStr"
                           class="easyui-validatebox" required="true" hidden="true"/>
                    <a href="javascript:saveDate()">
                        <div id="calendar" class="easyui-calendar"
                             style="width:180px;height:180px;">
                        </div>
                    </a>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveCustomer()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeCustomerDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>


<script type="text/javascript">
    var url;
    function searchCustomer() {
        $("#dg").datagrid('load', {
            "keyword": $("#keyword").val(),
        });
    }

    function deleteCustomer() {
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
                                "${pageContext.request.contextPath}/customer/delete.do",
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

    function openCustomerAddDialog() {
        $("#dlg").dialog("open").dialog("setTitle", "新增客户");
        url = "${pageContext.request.contextPath}/customer/add.do";
    }

    function saveCustomer() {
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

    function openCustomerModifyDialog() {
        var selectedRows = $("#dg").datagrid('getSelections');
        if (selectedRows.length != 1) {
            $.messager.alert("系统提示", "请选择一条要编辑的数据！");
            return;
        }
        var row = selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle", "修改信息");
        $('#fm').form('load', row);
        url = "${pageContext.request.contextPath}/customer/update.do?id="
            + row.id;
    }

    <%--function formatHref(val, row) {--%>
    <%--    return "<a href='${pageContext.request.contextPath}/article.html?id=" + row.id + "' target='_blank'>查看详情</a>";--%>
    <%--}--%>

    function resetValue() {
        $("#name").val("");
        $("#mobile").val("");
        $("#require").val("");
        $("#dateStr").val("");
        $("#empId").val("");
    }

    function closeCustomerDialog() {
        $("#dlg").dialog("close");
        resetValue();
    }
    function saveDate(){
        $('#calendar').calendar({
            onSelect: function(date){
                var month = date.getMonth() + 1;
                if(month < 10){
                    month = "0" + String(month);
                }
                var day = date.getDate();
                if(day < 10){
                    day = "0" + String(day);
                }
                $("#dateStr").val(date.getFullYear()+"-"+month+"-"+day);
                // alert(date.getFullYear()+":"+(date.getMonth()+1)+":"+date.getDate());
        }
        })
    }
</script>
</body>
</html>

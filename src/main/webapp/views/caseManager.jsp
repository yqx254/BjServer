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
        let clCnt = 1;
        let opCnt = 1;
        let currentId = 0;

        function searchCase() {
            $("#dg").datagrid('load', {
                "caseCode": $("#s_caseCode").val(),
                "client": $("#s_client").val(),
                "opponent": $("#s_opponent").val(),
                "createKW": $("#s_create").val()
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
            $("#dlg").dialog("open").dialog("setTitle", "新增");
            url = "${pageContext.request.contextPath}/case/add.do";
            $("#roleId").combobox("select","");
        }

        function saveCase() {
            $("#fm").form("submit", {
                url: url,
                dataType : "json",
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    const data = $.parseJSON(result);
                    if(data.success){
                        $.messager.alert("系统提示", "保存成功");
                        resetValue();
                        $("#dlg").dialog("close");
                        $("#dg").datagrid("reload");
                    }
                    else{
                        $.messager.alert("系统提示", data.msg);
                    }
                }
            });
        }
        function closeCase(){
            const check = $("#dataSaveCheck").is(':checked');
            let tips = "确定要结案吗?";
            let clear = 0;
            if(check){
                tips = "确定要结案，并移除当事人信息吗？"
               clear = 1;
            }
            $.messager.confirm("系统提示", tips, function (r) {
                if (r) {
                    $.post(url, {
                        id: currentId,
                        clear : clear
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "结案成功！");
                            $("#caseClsDlg").dialog("close");
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
            if (selectedRows.length !== 1) {
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
            url = "${pageContext.request.contextPath}/case/solve.do";
            currentId = idx;
        }
        function resetValue() {
            $("#cl").val("");
            $("#op").val("");
            $("#category").val("");
            $("#clIdx").val("");
            $("#opIdx").val("");
            $("#dealer").val("");
            $("#remarks").val("");
            let i = 1;
            for(;i <= clCnt; i ++){
                $("#cl-" + i).remove();
            }
            let j = 1;
            for(;j <= opCnt; j ++){
                $("#op-" + j).remove();
            }
        }

        function closeCaseDialog() {
            $("#caseClsDlg").dialog("close");
        }
        function closeAddDialog(){
            $("#dlg").dialog("close");
        }
        function rowFormatter(value, row, index) {
            if(row.status == 1){
                return '<a href="javascript:openCaseEditDialog(' + value +')" >编辑</a> ' + '   ' +
                    '<a href="javascript:openCaseCloseDialog(' + value + ')">结案</a> ';
            }
          }
        function addrows(){
            var clientTr;
            if(clCnt == 1){
                clientTr    = $("#client");
            }
            else{
                clientTr = $("#cl-" + (clCnt - 1));
            }
            var tr = "<tr id=\"cl-" + clCnt + "\">"+
                "               <td>委托人：</td>\n" +
                "                <td>\n" +
                "                    <input type=\"text\"  name=\"clientNameArr\"\n" +
                "                           class=\"easyui-validatebox\" required=\"true\"/>&nbsp;<font\n" +
                "                        color=\"red\">*</font>\n" +
                "<select name=\"clientIdtArr\" class=\"easyui-combobox\"  style=\"width:60px;\" editable=\"false\">"+
                "<option value=\"0\">原告</option>" +
                "<option value=\"1\">被告</option>" +
                "<option value=\"2\">原告人</option>" +
                "<option value=\"3\">被告人</option>" +
                "<option value=\"4\">第三人</option>" +
                "<option value=\"5\">顾问单位</option>"+
                "</select> " +
                "                </td>" +
                "</tr>";
            $(tr).insertAfter(clientTr);
            clCnt ++;
        }
        function deleterow(){
            $("#cl-" + (clCnt - 1)).remove();
            if(clCnt > 1){
                clCnt --;
            }
        }
        function addrows2(){
            var opponentTr;
            if(opCnt == 1){
                opponentTr = $("#opponent");
            }
            else{
                opponentTr = $("#op-" + (opCnt - 1));
            }

            var tr = "<tr id=\"op-" + opCnt + "\">"+
                "               <td>对方当事人：</td>\n" +
                "                <td>\n" +
                "                    <input type=\"text\"  name=\"opponentNameArr\"\n" +
                "                           class=\"easyui-validatebox\" required=\"true\"/>&nbsp;<font\n" +
                "                        color=\"red\">*</font>\n" +
                "<select name=\"opponentIdtArr\" class=\"easyui-combobox\"  style=\"width:60px;\" editable=\"false\">"+
                "<option value=\"0\">原告</option>" +
                "<option value=\"1\">被告</option>" +
                "<option value=\"2\">原告人</option>" +
                "<option value=\"3\">被告人</option>" +
                "<option value=\"4\">第三人</option>" +
                "<option value=\"5\">顾问单位</option>"+
                "</select> " +
                "                </td>" +
                "</tr>";
            $(tr).insertAfter(opponentTr);
            opCnt ++;
        }
        function deleterow2(){
            $("#op-" + (opCnt - 1)).remove();
            if(opCnt > 1){
                opCnt--;
            }
        }
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
        <table id="addTable" cellspacing="8px">
            <tr>
                <td>类型：</td>
                <td>
                    <select id="category" name="category" class="easyui-combobox" editable="false"  style="width:200px;">
                        <option value="0">民事</option>
                        <option value="1">刑事</option>
                        <option value="2">行政</option>
                        <option value="3">顾问</option>
                        <option value="4">其他</option>
                    </select>
                </td>
            </tr>
            <tr id="client">
                    <td>委托人：</td>
                    <td>
                        <input type="text"  name="clientNameArr" id="cl"
                               class="easyui-validatebox" required="true"/>&nbsp;<font
                            color="red">*</font>
                        <select id="clIdx" name="clientIdtArr" class="easyui-combobox"  editable="false" style="width:60px;" required="true">
                            <option value="0">原告</option>
                            <option value="1">被告</option>
                            <option value="2">原告人</option>
                            <option value="3">被告人</option>
                            <option value="4">第三人</option>
                            <option value="5">顾问单位</option>
                        </select>
                        <input type="button"  value="添加" onClick="addrows();">
                        <input type="button"  value="删除" onClick="deleterow();">
                    </td>
            </tr>
            <tr id="opponent">
                <td>对方当事人：</td>
                <td>
                    <input type="text"  name="opponentNameArr" id="op"
                           class="easyui-validatebox" />&nbsp;
                    <select id="opIdx" name="opponentIdtArr" class="easyui-combobox"  editable="false" style="width:60px;" required="true">
                        <option value="0">原告</option>
                        <option value="1">被告</option>
                        <option value="2">原告人</option>
                        <option value="3">被告人</option>
                        <option value="4">第三人</option>
                        <option value="5">顾问单位</option>
                    </select>
                    <input type="button"  value="添加" onClick="addrows2();">
                    <input type="button"  value="删除" onClick="deleterow2();">
                </td>
            </tr>
            <tr>
                <td>承办人：</td>
                <td>
                    <input type="text"  name="dealer"
                           class="easyui-validatebox" />
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td>
                    <input type="text"  name="remarks"
                           class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:saveCase()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeAddDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>
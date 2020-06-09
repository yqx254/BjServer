<%--
  Created by IntelliJ IDEA.
  User: ravix
  Date: 2020/6/8
  Time: 21:34
  To change this template use File | Settings | File Templates.
--%>
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
                "createKW": $("#s_create").val(),
                "startDate": $("#start_date").datebox('getValue'),
                "endDate" : $("#end_date").datebox('getValue')
            });
        }
        function resetList() {
            $("#s_caseCode").val("");
            $("#s_client").val("");
            $("#s_opponent").val("");
            $("#s_create").val("");
            $("#start_date").datebox('setValue',null);
            $("#end_date").datebox('setValue',null);
            $("#dg").datagrid('load', {});

        }
        function outputCase(){
            let u = "${pageContext.request.contextPath}/case/export.do?";
            u += "caseCode=" + $("#s_caseCode").val();
            u += "&client=" + $("#s_client").val();
            u += "&opponent=" + $("#s_opponent").val();
            u += "&createKW=" + $("#s_create").val();
            u += "&startDate=" + $("#start_date").datebox('getValue');
            u += "&endDate=" + $("#end_date").datebox('getValue');
            window.open(u);
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
            resetValue();
            url = "${pageContext.request.contextPath}/case/add.do";
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
                        $.messager.alert("系统提示", data.msg);
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

        function openCaseEditDialog(index) {
            resetValue();
            $.get(
                "${pageContext.request.contextPath}/case/get-detail.do?id=" + index,
                function(result){
                    if(result.success){
                        let mycase = result.caseInfo;
                        let client = mycase.clientNameArr;
                        if(client.length > 1){
                            clCnt = client.length;
                        }
                        let clientIdx = mycase.clientIdtArr;
                        let opponent = mycase.opponentNameArr;
                        if(opponent.length > 1){
                            opCnt = opponent.length;
                        }
                        let opponentIdx = mycase.opponentIdtArr;
                        for(var i = 0; i < client.length;i ++){
                            var clientTr;
                            if(i === 0){
                                $("#cl").val(client[0]);
                                $("#clIdx").combobox('select', clientIdx[0]);
                                continue;
                            }
                            else if(i == 1){
                                clientTr    = $("#client");
                            }
                            else{
                                clientTr = $("#cl-" + (i - 1));
                            }
                            var tr = "<tr id=\"cl-" + i + "\">"+
                                "               <td>委托人：</td>\n" +
                                "                <td>\n" +
                                "                    <input type=\"text\"  name=\"clientNameArr\"\n" +
                                "                           class=\"easyui-validatebox\" value=\"" + client[i] + "\" required=\"true\"/>&nbsp;<font\n" +
                                "                        color=\"red\">*</font>\n" +
                                "<select id=\"clSelect-" + i + "\" name=\"clientIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">";
                            if(clientIdx[i] == 0){
                                tr += "<option value=\"0\" selected=\"selected\">原告</option>" ;
                            }
                            else{
                                tr += "<option value=\"0\">原告</option>" ;
                            }
                            if(clientIdx[i] == 1){
                                tr += "<option value=\"1\" selected=\"selected\">被告</option>"
                            }
                            else{
                                tr += "<option value=\"1\">被告</option>";
                            }
                            if(clientIdx[i] == 2){
                                tr += "<option value=\"1\" selected=\"selected\">原告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">原告人</option>";
                            }
                            if(clientIdx[i] == 3){
                                tr += "<option value=\"1\" selected=\"selected\">被告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">被告人</option>";
                            }
                            if(clientIdx[i] == 4){
                                tr += "<option value=\"4\" selected=\"selected\">第三人</option>";
                            }
                            else{
                                tr += "<option value=\"4\">第三人</option>";
                            }
                            if(clientIdx[i] == 5){
                                tr += "<option value=\"5\" selected=\"selected\">顾问单位</option>";
                            }
                            else{
                                tr += "<option value=\"5\">顾问单位</option>";
                            }

                            tr += "</select> " +
                                "                </td>" +
                                "</tr>";
                            $(tr).insertAfter(clientTr);
                        }
                        for(var i = 0; i < opponent.length;i ++){
                            var opponentTr;
                            if(i === 0){
                                $("#op").val(opponent[0]);
                                $("#opIdx").combobox('select', opponentIdx[0]);
                                continue;
                            }
                            else if(i == 1){
                                opponentTr    = $("#opponent");
                            }
                            else{
                                opponentTr = $("#op-" + (i - 1));
                            }
                            var tr = "<tr id=\"op-" + i + "\">"+
                                "               <td>对方当事人：</td>\n" +
                                "                <td>\n" +
                                "                    <input type=\"text\"  name=\"opponentNameArr\"\n" +
                                "                           class=\"easyui-validatebox\" value=\"" + opponent[i] + "\"/>&nbsp;&nbsp;" +
                                "<select id=\"opSelect-" + i + "\"name=\"opponentIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">";
                            if(opponentIdx[i] == 0){
                                tr += "<option value=\"0\" selected=\"selected\">原告</option>" ;
                            }
                            else{
                                tr += "<option value=\"0\">原告</option>" ;
                            }
                            if(opponentIdx[i] == 1){
                                tr += "<option value=\"1\" selected=\"selected\">被告</option>"
                            }
                            else{
                                tr += "<option value=\"1\">被告</option>";
                            }
                            if(opponentIdx[i] == 2){
                                tr += "<option value=\"1\" selected=\"selected\">原告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">原告人</option>";
                            }
                            if(opponentIdx[i] == 3){
                                tr += "<option value=\"1\" selected=\"selected\">被告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">被告人</option>";
                            }
                            if(opponentIdx[i] == 4){
                                tr += "<option value=\"4\" selected=\"selected\">第三人</option>";
                            }
                            else{
                                tr += "<option value=\"4\">第三人</option>";
                            }
                            if(opponentIdx[i] == 5){
                                tr += "<option value=\"5\" selected=\"selected\">顾问单位</option>";
                            }
                            else{
                                tr += "<option value=\"5\">顾问单位</option>";
                            }
                            tr += "</select> " +
                                "                </td>" +
                                "</tr>";
                            $("#opSelect-" + i).combobox('select', opponentIdx[i]);
                            $(tr).insertAfter(opponentTr);
                        }
                        $("#dlg").dialog("open").dialog("setTitle", "编辑");
                        $("#category").combobox('select', mycase.category);
                        $("input[name=dealer]").val(mycase.dealer);
                        $("input[name=remarks]").val(mycase.remarks);
                        $("input[name=caseCode]").val(mycase.caseCode);
                    }
                    else{
                        $.messager.alert("系统提示", "查询信息失败，请重试");
                    }
                },"json");

            url = "${pageContext.request.contextPath}/case/add.do?id=" + index;
        }
        function openCaseDetailDialog(index) {
            resetValue();
            $.get(
                "${pageContext.request.contextPath}/case/get-detail.do?id=" + index,
                function(result){
                    if(result.success){
                        let mycase = result.caseInfo;
                        let client = mycase.clientNameArr;
                        if(client.length > 1){
                            clCnt = client.length;
                        }
                        let clientIdx = mycase.clientIdtArr;
                        let opponent = mycase.opponentNameArr;
                        if(opponent.length > 1){
                            opCnt = opponent.length;
                        }
                        let opponentIdx = mycase.opponentIdtArr;
                        for(var i = 0; i < client.length;i ++){
                            var clientTr;
                            if(i === 0){
                                $("#detailCl").val(client[0]);
                                $("#detailClIdx").combobox('select', clientIdx[0]);
                                continue;
                            }
                            else if(i == 1){
                                clientTr    = $("#detailClient");
                            }
                            else{
                                clientTr = $("#detailCl-" + (i - 1));
                            }
                            var tr = "<tr id=\"cl-" + i + "\">"+
                                "               <td>委托人：</td>\n" +
                                "                <td>\n" +
                                "                    <input type=\"text\"  name=\"clientNameArr\"\n readonly" +
                                "                           class=\"easyui-validatebox\" value=\"" + client[i] + "\" required=\"true\"/>&nbsp;<font\n" +
                                "                        color=\"red\">*</font>\n" +
                                "<select id=\"detailClSelect-" + i + "\" name=\"clientIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">";
                            if(clientIdx[i] == 0){
                                tr += "<option value=\"0\" selected=\"selected\" >原告</option>" ;
                            }
                            else{
                                tr += "<option value=\"0\">原告</option>" ;
                            }
                            if(clientIdx[i] == 1){
                                tr += "<option value=\"1\" selected=\"selected\">被告</option>"
                            }
                            else{
                                tr += "<option value=\"1\">被告</option>";
                            }
                            if(clientIdx[i] == 2){
                                tr += "<option value=\"1\" selected=\"selected\">原告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">原告人</option>";
                            }
                            if(clientIdx[i] == 3){
                                tr += "<option value=\"1\" selected=\"selected\">被告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">被告人</option>";
                            }
                            if(clientIdx[i] == 4){
                                tr += "<option value=\"4\" selected=\"selected\">第三人</option>";
                            }
                            else{
                                tr += "<option value=\"4\">第三人</option>";
                            }
                            if(clientIdx[i] == 5){
                                tr += "<option value=\"5\" selected=\"selected\">顾问单位</option>";
                            }
                            else{
                                tr += "<option value=\"5\">顾问单位</option>";
                            }

                            tr += "</select> " +
                                "                </td>" +
                                "</tr>";
                            $(tr).insertAfter(clientTr);
                        }
                        for(var i = 0; i < opponent.length;i ++){
                            var opponentTr;
                            if(i === 0){
                                $("#detailOp").val(opponent[0]);
                                $("#detailOpIdx").combobox('select', opponentIdx[0]);
                                continue;
                            }
                            else if(i == 1){
                                opponentTr    = $("#detailOpponent");
                            }
                            else{
                                opponentTr = $("#detailOp-" + (i - 1));
                            }
                            var tr = "<tr id=\"detailOp-" + i + "\">"+
                                "               <td>对方当事人：</td>\n" +
                                "                <td>\n" +
                                "                    <input type=\"text\"  name=\"opponentNameArr\"\n readonly" +
                                "                           class=\"easyui-validatebox\" value=\"" + opponent[i] + "\"/>&nbsp;&nbsp;" +
                                "<select id=\"detailOpSelect-" + i + "\" name=\"opponentIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">";
                            if(opponentIdx[i] == 0){
                                tr += "<option value=\"0\" selected=\"selected\">原告</option>" ;
                            }
                            else{
                                tr += "<option value=\"0\">原告</option>" ;
                            }
                            if(opponentIdx[i] == 1){
                                tr += "<option value=\"1\" selected=\"selected\">被告</option>"
                            }
                            else{
                                tr += "<option value=\"1\">被告</option>";
                            }
                            if(opponentIdx[i] == 2){
                                tr += "<option value=\"1\" selected=\"selected\">原告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">原告人</option>";
                            }
                            if(opponentIdx[i] == 3){
                                tr += "<option value=\"1\" selected=\"selected\">被告人</option>";
                            }
                            else{
                                tr += "<option value=\"1\">被告人</option>";
                            }
                            if(opponentIdx[i] == 4){
                                tr += "<option value=\"4\" selected=\"selected\">第三人</option>";
                            }
                            else{
                                tr += "<option value=\"4\">第三人</option>";
                            }
                            if(opponentIdx[i] == 5){
                                tr += "<option value=\"5\" selected=\"selected\">顾问单位</option>";
                            }
                            else{
                                tr += "<option value=\"5\">顾问单位</option>";
                            }
                            tr += "</select> " +
                                "                </td>" +
                                "</tr>";
                            $("#opSelect-" + i).combobox('select', opponentIdx[i]);
                            $(tr).insertAfter(opponentTr);
                        }
                        $("#detailDlg").dialog("open").dialog("setTitle", "详情");
                        $("#detailCategory").combobox('select', mycase.category);
                        $("input[name=dealer]").val(mycase.dealer);
                        $("input[name=remarks]").val(mycase.remarks);
                        $("input[name=caseCode]").val(mycase.caseCode);
                    }
                    else{
                        $.messager.alert("系统提示", "查询信息失败，请重试");
                    }
                },"json");
        }
        function openCaseCloseDialog(idx){
            $("#caseClsDlg").dialog("open").dialog("setTitle", "结案");
            url = "${pageContext.request.contextPath}/case/solve.do";
            currentId = idx;
        }
        function resetValue() {
            $("#cl").val("");
            $("#op").val("");
            $("#category").combobox('select', 0);
            $("#clIdx").combobox('select', 0);
            $("#opIdx").combobox('select',0);
            $('#forceInput').removeAttr('checked');
            $("input[name=dealer]").val("");
            $("input[name=remarks]").val("");
            $("input[name=caseCode]").val("");
            let i = 1;
            for(;i <= clCnt; i ++){
                $("#cl-" + i).remove();
            }
            let j = 1;
            for(;j <= opCnt; j ++){
                $("#op-" + j).remove();
            }
            clCnt = 1;
            opCnt = 1;
        }

        function closeCaseDialog() {
            $("#caseClsDlg").dialog("close");
        }
        function closeAddDialog(){
            $("#dlg").dialog("close");
        }
        function rowFormatter(value, row, index) {
            if(row.status ===1){
                return '<a href="javascript:openCaseEditDialog(' + value +')" >编辑</a> ' + '   ' +
                    '<a href="javascript:openCaseCloseDialog(' + value + ')">结案</a> ';
            }
            return '<a href="javascript:openCaseDetailDialog(' + value + ')">详情</a>';
        }
        function addrows(){
            var clientTr;
            if(clCnt === 1){
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
                "<select name=\"clientIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">"+
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
                "                           class=\"easyui-validatebox\" required=\"true\"/>&nbsp;&nbsp;" +
                "<select name=\"opponentIdtArr\" class=\"easyui-combobox\"  style=\"width:75px;\" editable=\"false\">"+
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
        function closeDetailDialog(){
            $("#detailDlg").dialog("close");
        }

        function myformatter(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
        }
        function myparser(s) {
            if (!s) return new Date();
            var ss = (s.split('-'));
            var y = parseInt(ss[0], 10);
            var m = parseInt(ss[1], 10);
            var d = parseInt(ss[2], 10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                return new Date(y, m - 1, d);
            } else {
                return new Date();
            }
        }
    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="案件信息" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true" pageSize="20"
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
        <th field="createdAtStr" width="100" align="center">录入时间</th>
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
        录入时间：<input class="easyui-datebox" id="start_date" data-options="formatter:myformatter,parser:myparser" />-
        <input class="easyui-datebox" id="end_date" data-options="formatter:myformatter,parser:myparser" />
        <a href="javascript:searchCase()" class="easyui-linkbutton"
           iconCls="icon-search" plain="true">搜索</a>
        <a href="javascript:resetList()" class="easyui-linkbutton"
           iconCls="icon-reload" plain="true">重置</a>
        <a href="javascript:outputCase()" class="easyui-linkbutton"
           iconCls="icon-print" plain="true">导出</a>
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
     style="width: 620px;height:620px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table id="addTable" cellspacing="8px">
            <tr>
                <td>案号：</td>
                <td>
                    <input type="text"  name="caseCode"  readonly
                           class="easyui-validatebox" />
                </td>
            </tr>
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
                    <select id="clIdx" name="clientIdtArr" class="easyui-combobox"  editable="false" style="width:75px;" required="true">
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
                           class="easyui-validatebox" />&nbsp;&nbsp;
                    <select id="opIdx" name="opponentIdtArr" class="easyui-combobox"  editable="false" style="width:75px;" required="true">
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
                    <input type="text"  name="dealer" value=""
                           class="easyui-validatebox" required="true" />
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td>
                    <input type="text"  name="remarks" value=""
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

<div id="detailDlg" class="easyui-dialog"
     style="width: 620px;height:620px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons2">
    <form  action="post">
        <table cellspacing="8px">
            <tr>
                <td>案号：</td>
                <td>
                    <input type="text" id="caseCode"  name="caseCode"  readonly
                           class="easyui-validatebox" />
                </td>
            </tr>
            <tr>
                <td>类型：</td>
                <td>
                    <select id="detailCategory" name="category" class="easyui-combobox" editable="false"  style="width:200px;" readonly>
                        <option value="0">民事</option>
                        <option value="1">刑事</option>
                        <option value="2">行政</option>
                        <option value="3">顾问</option>
                        <option value="4">其他</option>
                    </select>
                </td>
            </tr>
            <tr id="detailClient">
                <td>委托人：</td>
                <td>
                    <input type="text"  name="clientNameArr" id="detailCl" readonly
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                    <select id="detailClIdx" name="clientIdtArr" class="easyui-combobox" readonly  editable="false" style="width:75px;" required="true">
                        <option value="0">原告</option>
                        <option value="1">被告</option>
                        <option value="2">原告人</option>
                        <option value="3">被告人</option>
                        <option value="4">第三人</option>
                        <option value="5">顾问单位</option>
                    </select>
                </td>
            </tr>
            <tr id="detailOpponent">
                <td>对方当事人：</td>
                <td>
                    <input type="text"  name="opponentNameArr" id="detailOp" readonly
                           class="easyui-validatebox" />&nbsp;&nbsp;
                    <select id="detailOpIdx" name="opponentIdtArr" class="easyui-combobox"  editable="false"  readonly style="width:75px;" required="true">
                        <option value="0">原告</option>
                        <option value="1">被告</option>
                        <option value="2">原告人</option>
                        <option value="3">被告人</option>
                        <option value="4">第三人</option>
                        <option value="5">顾问单位</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>承办人：</td>
                <td>
                    <input type="text"  name="dealer" value="" readonly
                           class="easyui-validatebox" />
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td>
                    <input type="text"  name="remarks" value="" readonly
                           class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons2">
    <a href="javascript:closeDetailDialog()"
       class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>

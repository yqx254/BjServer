<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>ssm-maven系统主页</title>
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
        function addTab(url, text, iconCls) {
            var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/views/"
                    + url + "'></iframe>";
            $("#tabs").tabs("add", {
                title: text,
                iconCls: iconCls,
                closable: true,
                content: content
            });
        }
        function openTab(text, url, iconCls) {
            if ($("#tabs").tabs("exists", text)) {
                $("#tabs").tabs("close", text);
                addTab(url, text, iconCls);
                $("#tabs").tabs("select", text);
            } else {
                addTab(url, text, iconCls);
            }
        }

        function logout() {
            $.messager
                    .confirm(
                            "系统提示",
                            "您确定要退出系统吗",
                            function (r) {
                                if (r) {
                                    window.location.href = "${pageContext.request.contextPath}/user/logout.do";
                                }
                            });
        }
    </script>
    <jsp:include page="login_chk.jsp"></jsp:include>
<body class="easyui-layout">
<div region="north" style="height: 78px;background-color: #ffff">
    <table width="100%">
        <tr>
            <td width="50%"></td>
            <td valign="bottom"
                style="font-size: 20px;color:#8B8B8B;font-family: '楷体';"
                align="right" width="50%"><font size="3">&nbsp;&nbsp;<strong>当前用户：</strong>${currentUser.userName
                    }</font>【管理员】
            </td>
        </tr>
    </table>
</div>
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="首页" data-options="iconCls:'icon-home'">
            <div align="center" style="padding-top: 20px;"><a href="https://github.com/yqx254/BjServer/issues"
                                                              target="_blank"
                                                              style="font-size: 20px;">欢迎来这里给我提BUG</a></div>
            <div align="center" style="padding-top: 50px">
                <font color="grey" size="10">案件信息管理系统</font>
            </div>
        </div>
    </div>
</div>
<div region="west" style="width: 200px;height:500px;" title="导航菜单"
     split="true">
    <div class="easyui-accordion">
    <c:forEach items="${menu}" var="m" >
        <div title="${m.title}"
             data-options="selected:true,iconCls:'${m.icon}'"
             style="padding: 10px;height:10px;">
            <c:forEach items="${m.subMenu}" var= "m2">
            <a
                    href="javascript:openTab(' ${m2.title}','${m2.pageUrl}','${m2.icon}')"
                    class="easyui-linkbutton"
                    data-options="plain:true,iconCls:'icon-wenzhang'"
                    style="width: 150px;"> ${m2.title}</a>
            </c:forEach>
        </div>
    </c:forEach>
        <div title="个人中心" data-options="iconCls:'icon-item'"
             style="padding:10px;border:none;">
        <a href="javascript:logout()"
                            class="easyui-linkbutton"
                            data-options="plain:true,iconCls:'icon-exit'"
                            style="width: 150px;">
            安全退出${t}</a>
        </div>
    </div>
</div>
</body>
</html>
<script>
    var par =${t};
    console.log(par);//打印'随便写点东西'
</script>
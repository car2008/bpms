<%@ page import="com.capitalbiotech.bpms.Project" %>
<g:set var="today" value="${formatDate(format:'yyyy-MM-dd', date:new Date())}" />
<g:set var="projectCreateDate" value="${projectInstance?.projectCreateDate != null ? formatDate(date: projectInstance?.projectCreateDate, format: 'yyyy-MM-dd') : today}"/>
<g:set var="dueTime" value="${projectInstance?.dueTime != null ? formatDate(date: projectInstance?.dueTime, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="metaSendData" value="${projectInstance?.metaSendData != null ? formatDate(date: projectInstance?.metaSendData, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="analyStartDate" value="${projectInstance?.analyStartDate != null ? formatDate(date: projectInstance?.analyStartDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="analySendDate" value="${projectInstance?.analySendDate != null ? formatDate(date: projectInstance?.analySendDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="innerDueDate" value="${projectInstance?.innerDueDate != null ? formatDate(date: projectInstance?.innerDueDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="backupDate" value="${projectInstance?.backupDate != null ? formatDate(date: projectInstance?.backupDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="fileRealName" value="${fileRealName ==null ? "" : fileRealName}"></g:set>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="default.create.label" args="${[message(code: 'project.label')]}" /></title>

</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="default.create.label" args="${[message(code: 'project.label')]}" />
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9">
			<g:if test="${flash.message}">
				<div class="alert alert-info">
					${flash.message}
				</div>
			</g:if>
			<g:if test="${flash.error}">
				<div class="alert alert-error">
					${flash.error}
				</div>
			</g:if>
			<g:hasErrors bean="${projectInstance}">
				<div class="alert alert-error">
					<g:renderErrors bean="${projectInstance}" as="list" />
				</div>
			</g:hasErrors>
			<g:form action="save" class="form-horizontal" method="post" enctype="multipart/form-data">
				<g:render template="form" model="${[projectInstance: projectInstance]}"/>
				<div class="control-group">
					<div class="controls">
						<g:submitButton id="create" name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label')}" onclick="checkfile()"/>
					</div>
				</div>
			</g:form>
			</div>
			<!--/span-->
			<div class="span3" style="width:15%;">
				<g:render template="sidebar"/> 
			</div>
			<!--/span-->
		</div>
	</div>
	<script type="text/javascript">
		//window.onload=function(){
			// 每发一个请求，应该创建一个xhr对象 
	   		//var xhr = new XMLHttpRequest();
	   		//xhr.onreadystatechange = function() {
	   			// 响应完全返回, 并且响应成功了
	   		//	if(xhr.readyState == 4 && xhr.status == 200) {
	   		//		var text = xhr.responseText;
	   		//		text=text.replace("[", "");
	   		//		text=text.replace("]", "");
	   		//		//console.log(text);
	   		//		document.getElementById("title").value=parseInt(text)+1;
	   		//	}
	   		//};
	   		
	    	//xhr.open("GET", "findMaxId", true);//http://localhost:8080/bpms/project/findMaxId
	    	//xhr.send();
		//}
	</script>
</body>
</html>

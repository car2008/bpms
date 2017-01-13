<%@ page import="com.capitalbiotech.bpms.Project" %>
<g:set var="today" value="${formatDate(format:'yyyy-MM-dd', date:new Date())}" />
<g:set var="projectCreateDate" value="${projectInstance?.projectCreateDate != null ? formatDate(date: projectInstance?.projectCreateDate, format: 'yyyy-MM-dd') : today}"/>
<g:set var="dueTime" value="${projectInstance?.dueTime != null ? formatDate(date: projectInstance?.dueTime, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="metaSendData" value="${projectInstance?.metaSendData != null ? formatDate(date: projectInstance?.metaSendData, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="analyStartDate" value="${projectInstance?.analyStartDate != null ? formatDate(date: projectInstance?.analyStartDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="analySendDate" value="${projectInstance?.analySendDate != null ? formatDate(date: projectInstance?.analySendDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="innerDueDate" value="${projectInstance?.innerDueDate != null ? formatDate(date: projectInstance?.innerDueDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="backupDate" value="${projectInstance?.backupDate != null ? formatDate(date: projectInstance?.backupDate, format: 'yyyy-MM-dd') : ""}"/>
<g:set var="disabledValue" value="${projectInstance?.isControled == true && projectInstance?.isRemoted == true && flag==false ? 'disabled' : 'false'}" />
<g:set var="fileRealName" value="${fileRealName ==null ? "" : fileRealName}"></g:set>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="default.edit.label" args="${[message(code: 'project.label')]}" /></title>
<style>
	.contentBox{
		width:88%;
		float:left;
	}
	.sideBox{
		width:10%;
		max-height:120px;
		float:right;
	}
	.formBtn{
		position:absolute;
		width:100px;
		bottom:11%;
	}
	#updateBtn{
		right:17%;
	}
	#deleteBtn{
		right:12%;
	}
</style>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="default.edit.label" args="${[message(code: 'project.label')]}" />
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="contentBox">
				<g:if test="${disabledValue == 'disabled'}">
					<div class="alert alert-info">
						<g:message code="project.isfinished" />
					</div>
				</g:if>
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
				<g:form action="save" class="form-horizontal" method="post" enctype="multipart/form-data" style="position:relative;">
					<g:hiddenField name="id" value="${projectInstance?.id}" />
					<g:hiddenField name="version" value="${projectInstance?.version}" />
					<g:render template="form" model="${[projectInstance: projectInstance]}"/>
					<div class="control-group">
						<div class="controls" >
							<g:actionSubmit id="updateBtn" disabled="${disabledValue }" class="btn btn-primary formBtn" action="update"
								value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="checkfile()"/>
							<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_SELLER">
								<g:actionSubmit id="deleteBtn" class="btn btn-danger formBtn" action="delete"
									href="#deleteModal" data-toggle="modal"
									value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
							</sec:ifAnyGranted>
						</div>
					</div>
				</g:form>
			</div>
			<!--/span-->
			<div class="sideBox">
				<g:render template="sidebar"/> 
			</div>
			<!--/span-->
		</div>
	</div>
	
	<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_SELLER">
		<!-- Modal -->
		<div id="deleteModal" class="modal hide fade" tabindex="-1"
			role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="deleteModalLabel"><g:message code="warning.label" /></h3>
			</div>
			<div class="modal-body">
				<p><g:message code="project.deletion.warning"/></p>
			</div>
			<div class="modal-footer">
				<g:form action="delete" method="post" style="padding:0; margin:0">
					<g:hiddenField name="id" value="${projectInstance?.id}" />
					<button class="btn" data-dismiss="modal" aria-hidden="true"><g:message code="cancel.label" /></button>
					<button class="btn btn-danger"><g:message code="confirm.deletion.label" /></button>
				</g:form>
			</div>
		</div>
	</sec:ifAnyGranted>
	<script type="text/javascript">
	function transNumToDate(id){
		var dayNum;
		var valueStr;
		var value
		if(id=="dueTimeNum"){
			dayNum=document.getElementById("dueTimeNum");
			valueStr=dayNum.value;
		}
		if(id=="innerDueTimeNum"){
			dayNum=document.getElementById("innerDueTimeNum");
			valueStr=dayNum.value;
		}
		if(valueStr==null || ""==valueStr){
		}else if(!isNaN(valueStr)){
			value=parseInt(valueStr);
			if(value > 0 && value < 365){
				var xhr = new XMLHttpRequest();
		   		xhr.onreadystatechange = function() {
		   			// 响应完全返回, 并且响应成功了
		   			if(xhr.readyState == 4 && xhr.status == 200) {
		   				var text = xhr.responseText;
		   				text=text.replace("[", "");
		   				text=text.replace("]", "");
		   				//console.log(text);
		   				if(id=="dueTimeNum"){
		   					document.getElementById("dueTime").value=text;
		   				}
		   				if(id=="innerDueTimeNum"){
		   					document.getElementById("innerDueDate").value=text;
		   				}
		   			}
		   		};
		   		
		    	xhr.open("GET", "../transNumToDate/"+value, true);//http://localhost:8080/bpms/project/findMaxId
		    	xhr.send();
			}else{
				alert('请输入1-365之间的正整数 ');
			}
		}else{
			alert('请输入1-365之间的正整数 ');
		}
		
      }
	</script>
</body>
</html>

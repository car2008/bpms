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
			<g:form action="save" class="form-horizontal" method="post" enctype="multipart/form-data">
				<g:hiddenField name="id" value="${projectInstance?.id}" />
				<g:hiddenField name="version" value="${projectInstance?.version}" />
				<g:render template="form" model="${[projectInstance: projectInstance]}"/>
				<div class="control-group">
					<div class="controls" >
						<g:actionSubmit disabled="${disabledValue }" class="btn btn-primary" action="update"
							value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="checkfile()"/>
						<sec:ifAnyGranted roles="ROLE_ADMIN">
							<g:actionSubmit class="btn btn-danger" action="delete"
								href="#deleteModal" data-toggle="modal"
								value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
						</sec:ifAnyGranted>
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
	
	<sec:ifAnyGranted roles="ROLE_ADMIN">
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
</body>
</html>

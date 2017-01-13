<%@ page import="com.capitalbiotech.bpms.Worktime" %>
<g:set var="today" value="${formatDate(format:'yyyy-MM-dd', date:new Date())}" />
<g:set var="finishedDate" value="${worktimeInstance?.finishedDate != null ? formatDate(date: worktimeInstance?.finishedDate, format: 'yyyy-MM-dd') : today}"/>
<html>
<head>
<script src="${resource(dir:'js',file:'jquery.js')}"></script>
<script src="${resource(dir:'js',file:'bootstrap.js')}"></script>
<script src="${resource(dir:'js',file:'wysihtml5-0.3.0_rc2.js')}"></script>
<script src="${resource(dir:'js',file:'bootstrap-wysihtml5-0.0.2-zh.js')}"></script>
<script src="${resource(dir:'js',file:'chosen.jquery.js')}"></script>
<script src="${resource(dir:'js/', file:'bootstrap-datepicker.min.js')}"></script>
	<script src="${resource(dir:'js/', file:'bootstrap-datepicker.zh-CN.min.js')}"></script>
<link rel="shortcut icon" href="${resource(dir:'img',file:'favicon.ico')}" type="image/x-icon" />
<link href="${resource(dir:'css',file:'bootstrap.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'bootstrap-wysihtml5-0.0.2.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'chosen.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'bpms.css')}" rel="stylesheet">
<link href="${resource(dir:'css/', file:'bootstrap-datepicker3.min.css')}" rel="stylesheet" type="text/css" />
</head>
<body>
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
				<g:hiddenField name="projectInstanceId" value="${projectInstance?.id}" />
				<g:hiddenField name="projectInstanceVersion" value="${projectInstance?.version}" />
				<g:hiddenField name="id" value="${worktimeInstance?.id}" />
				<g:hiddenField name="version" value="${worktimeInstance?.version}" />
				<g:render template="form" model="${[projectInstance: projectInstance]}"/>
				<div class="control-group">
					<div class="controls">
						<g:actionSubmit class="btn btn-primary" action="update"
							value="${message(code: 'default.button.update.label', default: 'Update')}" />
						<g:actionSubmit class="btn btn-danger" action="delete"
							href="#deleteModal" data-toggle="modal"
							value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
					</div>
				</div>
			</g:form>
			</div>
		</div>
	</div>
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
					<g:hiddenField name="projectInstanceId" value="${projectInstance?.id}" />
					<g:hiddenField name="id" value="${worktimeInstance?.id}" />
					<button class="btn" data-dismiss="modal" aria-hidden="true"><g:message code="cancel.label" /></button>
					<button class="btn btn-danger"><g:message code="confirm.deletion.label" /></button>
				</g:form>
			</div>
		</div>
</body>
</html>

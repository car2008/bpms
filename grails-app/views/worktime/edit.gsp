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
			<g:form action="save" class="form-horizontal">
				<g:hiddenField name="projectInstanceId" value="${projectInstance?.id}" />
				<g:hiddenField name="projectInstanceVersion" value="${projectInstance?.version}" />
				<g:hiddenField name="id" value="${worktimeInstance?.id}" />
				<g:hiddenField name="version" value="${worktimeInstance?.version}" />
				<g:render template="form" model="${[projectInstance: projectInstance]}"/>
				<div class="control-group">
					<div class="controls">
						<g:actionSubmit class="btn btn-primary" action="update"
							value="${message(code: 'default.button.update.label', default: 'Update')}" />
						<sec:ifAnyGranted roles="ROLE_ADMIN">
							<g:actionSubmit class="btn btn-danger" action="delete"
								href="#deleteModal" data-toggle="modal"
								value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
						</sec:ifAnyGranted>
					</div>
				</div>
			</g:form>
			</div>
		</div>
	</div>
</body>
</html>

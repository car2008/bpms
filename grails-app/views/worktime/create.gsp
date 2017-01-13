<%@ page import="com.capitalbiotech.bpms.Worktime" %>
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
<title><g:message code="default.create.label" args="${[message(code: 'worktime.label')]}" /></title>
<script type="text/javascript">
	window.onload=function(){
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9">
			<g:form action="save" class="form-horizontal">
				<g:hiddenField name="title" value="${projectInstance.title}"/>
				<g:hiddenField name="information" value="${projectInstance.information}"/>
				<g:render template="form" model="${[projectInstance: projectInstance]}"/>
				<div class="control-group">
					<div class="controls">
						<g:submitButton id="create" name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label')}" />
					</div>
				</div>
			</g:form>
			</div>
		</div>
	</div>
</body>
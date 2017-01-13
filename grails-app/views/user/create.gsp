<%@ page import="com.capitalbiotech.bpms.User"%>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="default.create.label" args="${[message(code: 'user.label')]}" /></title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="default.create.label" args="${[message(code: 'user.label')]}" />
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
				<g:hasErrors bean="${userInstance}">
					<div class="alert alert-error">
						<g:renderErrors bean="${userInstance}" as="list" />
					</div>
				</g:hasErrors>
				<g:form action="save" class="form-horizontal">
					<div class="control-group ${hasErrors(bean: userInstance, field: 'username', 'error')}">
						<label class="control-label" for="username"><g:message code="user.username.label" /></label>
						<div class="controls">
							<g:textField name="username" value="${userInstance?.username}" />
							<span class="help-inline"><g:message code="required.label" /></span>
						</div>
					</div>
		
					<div class="control-group ${hasErrors(bean: userInstance, field: 'password', 'error')}">
						<label class="control-label" for="password"><g:message code="user.password.label" /></label>
						<div class="controls">
							<g:textField name="password" value="" />
							<span class="help-inline"><g:message code="required.label" /></span>
						</div>
					</div>
		
					<div class="control-group ${hasErrors(bean: userInstance, field: 'name', 'error')}">
						<label class="control-label" for="name"><g:message code="user.name.label" /></label>
							<div class="controls">
								<g:textField name="name" value="${userInstance?.name}" />
								<span class="help-inline"><g:message code="required.label" /></span>
						</div>
					</div>
		
					<div class="control-group ${hasErrors(bean: userInstance, field: 'email', 'error')}">
						<label class="control-label" for="email"><g:message code="user.email.label" /></label>
						<div class="controls">
								<g:textField name="email" value="${userInstance?.email}" />
								<span class="help-inline"><g:message code="required.label" /></span>
						</div>
					</div>
					
					<div class="control-group ${hasErrors(bean: userInstance, field: 'avatar', 'error')}">
						<label class="control-label" for="avatar"><g:message code="user.avatar.label" /></label>
						<div class="controls">
								<g:textField name="avatar" value="${userInstance?.avatar}" />
								<span class="help-inline"><g:message code="required.label" /></span>
						</div>
					</div>
		
					<div class="control-group ${hasErrors(bean: userInstance, field: 'roles', 'error')}">
						<label class="control-label" for="roles"><g:message code="user.roles.label" /></label>
						<div class="controls">
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_USER" checked disabled>
								<g:message code="role.authority.ROLE_USER.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_SPLITER">
								<g:message code="role.authority.ROLE_SPLITER.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_ANALYST">
								<g:message code="role.authority.ROLE_ANALYST.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_EXAMINER">
								<g:message code="role.authority.ROLE_EXAMINER.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_SUPERVISOR">
								<g:message code="role.authority.ROLE_SUPERVISOR.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_SELLER">
								<g:message code="role.authority.ROLE_SELLER.label" />
							</label>
							<br />
							<label class="checkbox inline"> <input type="checkbox"
								name="authority" value="ROLE_ADMIN">
								<g:message code="role.authority.ROLE_ADMIN.label" />
							</label>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<g:actionSubmit class="btn btn-primary" action="save"
								value="${message(code: 'default.button.create.label', default: 'Create')}" />
						</div>
					</div>
				</g:form>
			</div>
			<!--/span-->
			<div class="span3">
				<g:render template="sidebar" />
			</div>
			<!--/span-->
		</div>
	</div>
</body>
</html>

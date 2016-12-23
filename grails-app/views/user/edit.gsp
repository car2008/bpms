<%@ page import="com.capitalbiotech.bpms.User"%>
<html>
<head>
<meta name="layout" content="main" />
<title>
<g:if test="${self}">
<g:message code="edit.my.information.label" />
</g:if>
<g:else>
<g:message code="edit.information.label" />: ${userInstance?.username}
</g:else>
</title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:if test="${self}">
				<g:message code="edit.my.information.label" />
			</g:if>
			<g:else>
				<g:message code="edit.information.label" />: ${userInstance?.username}
			</g:else>
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
				<g:form method="post" class="form-horizontal">
					<g:hiddenField name="id" value="${userInstance?.id}" />
					<g:hiddenField name="version" value="${userInstance?.version}" />
					<sec:ifAnyGranted roles="ROLE_ADMIN">
						<div class="control-group ${hasErrors(bean: userInstance, field: 'username', 'error')}">
							<label class="control-label" for="username"><g:message code="user.username.label" default="Username" /></label>
							<div class="controls">
								<input name="username" type="text" value="${userInstance?.username}" />
							</div>
						</div>
					</sec:ifAnyGranted>
					<div class="control-group ${hasErrors(bean: userInstance, field: 'name', 'error')}">
						<label class="control-label" for="name"><g:message code="user.name.label" default="Display Name" /></label>
						<div class="controls">
							<input name="name" type="text" value="${userInstance?.name}" />
						</div>
					</div>
					<div class="control-group ${hasErrors(bean: userInstance, field: 'email', 'error')}">
						<label class="control-label" for="email"><g:message code="user.email.label" default="Email" /></label>
						<div class="controls">
							<input name="email" type="email"
								value="${userInstance?.email}" />
						</div>
					</div>
					<sec:ifAnyGranted roles="ROLE_ADMIN">
						<div class="control-group ${hasErrors(bean: userInstance, field: 'roles', 'error')}">
							<label class="control-label" for="roles"><g:message code="user.roles.label" default="Roles" /></label>
							<div class="controls">
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_USER" checked disabled>
									<g:message code="role.authority.ROLE_USER.label" />
								</label>
								<br />
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_SPLITER"
									${authorities?.contains('ROLE_SPLITER') ? 'checked' : ''}>
									<g:message code="role.authority.ROLE_SPLITER.label" />
								</label>
								<br />
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_ANALYST"
									${authorities?.contains('ROLE_ANALYST') ? 'checked' : ''}>
									<g:message code="role.authority.ROLE_ANALYST.label" />
								</label>
								<br />
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_SUPERVISOR"
									${authorities?.contains('ROLE_SUPERVISOR') ? 'checked' : ''}>
									<g:message code="role.authority.ROLE_SUPERVISOR.label" />
								</label>
								<br />
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_SELLER"
									${authorities?.contains('ROLE_SELLER') ? 'checked' : ''}>
									<g:message code="role.authority.ROLE_SELLER.label" />
								</label>
								<br />
								<label class="checkbox inline"> <input type="checkbox"
									name="authority" value="ROLE_ADMIN"
									${authorities?.contains('ROLE_ADMIN') ? 'checked' : ''}>
									<g:message code="role.authority.ROLE_ADMIN.label" />
								</label>
							</div>
						</div>
					</sec:ifAnyGranted>
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
			<!--/span-->
			<div class="span3">
				<g:render template="sidebar" />
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
				<p><g:message code="user.deletion.warning"/></p>
			</div>
			<div class="modal-footer">
				<g:form action="delete" method="post" style="padding:0; margin:0">
					<g:hiddenField name="id" value="${userInstance?.id}" />
					<button class="btn" data-dismiss="modal" aria-hidden="true"><g:message code="cancel.label" /></button>
					<button class="btn btn-danger"><g:message code="confirm.deletion.label" /></button>
				</g:form>
			</div>
		</div>
	</sec:ifAnyGranted>
</body>
</html>

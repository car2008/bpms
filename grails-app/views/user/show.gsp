<%@ page import="com.capitalbiotech.bpms.User"%>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="user.label" />: ${userInstance?.username}</title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="user.label" />: ${userInstance?.username}
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
				<dl class="dl-horizontal">
					<dt><g:message code="user.username.label" /></dt>
					<dd><bpms:maybeEmpty value="${userInstance?.username}" /></dd>
					<dt><g:message code="user.name.label" /></dt>
					<dd><bpms:maybeEmpty value="${userInstance?.name}" /></dd>
					<dt><g:message code="user.email.label" /></dt>
					<dd><bpms:maybeEmpty value="${userInstance?.email}" /></dd>
					<dt><g:message code="user.roles.label" /></dt>
					<dd>
						<g:each in="${userInstance?.getAuthorities()}" var="role" status="i">
							<g:if test="${i > 0}"><br /></g:if>
							<g:message code="role.authority.${role.authority}.label" />
						</g:each>
					</dd>
				</dl>
				<sec:ifAnyGranted roles="ROLE_ADMIN">
					<g:link class="btn btn-primary" action="edit"><g:message code="edit.information.label" /></g:link>
					<g:link class="btn" action="password" id="${userInstance?.id}"><g:message code="change.password.label" /></g:link>
				</sec:ifAnyGranted>
			</div>
			<!-- /span -->
			<div class="span3">
				<g:render template="sidebar" />
			</div>
			<!--/span-->
		</div>
	</div>
</body>
</html>

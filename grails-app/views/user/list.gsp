<%@ page import="com.capitalbiotech.bpms.User"%>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="default.list.label" args="${[message(code: 'user.label')]}" /></title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="default.list.label" args="${[message(code: 'user.label')]}" />
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
				<table class="table table-hover">
					<thead>
						<tr>
							<g:sortableColumn property="username" title="${message(code: 'user.username.label')}" />
							<g:sortableColumn property="name" title="${message(code: 'user.name.label')}" />
							<g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}" />
							<g:sortableColumn property="enabled" title="${message(code: 'user.roles.label')}" />
							<th>${message(code: 'operations.label')}</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${userInstanceList}" var="userInstance">
							<tr>
								<td>
									${userInstance?.username}
								</td>
								<td>
									${userInstance?.name}
								</td>
								<td>
									${userInstance?.email}
								</td>
								<td>
									<g:each in="${userInstance?.getAuthorities()}" var="role" status="i">
										<g:if test="${i > 0}"><br /></g:if>
										<g:message code="role.authority.${role.authority}.label" />
									</g:each>
								</td>
								<td>
									<g:link controller="user" action="edit" id="${userInstance.id}"><g:message code="edit.information.label" /></g:link>
									|
									<g:link controller="user" action="password" id="${userInstance.id}"><g:message code="change.password.label" /></g:link>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
				<div class="pagination">
					<bpms:paginate total="${userInstanceTotal}" />
				</div>
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

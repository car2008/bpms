<sec:ifAnyGranted roles="ROLE_ADMIN">
<div class="well sidebar-nav">
	<ul class="nav nav-list">
		<li class="nav-header"><g:message code="actions.label" /></li>
		<li><g:link action="preferences"><g:message code="system.preferences.label" /></g:link></li>
		<li><g:link action="list"><g:message code="default.list.label" args="${[message(code: 'user.label')]}"/></g:link></li>
		<li><g:link action="create"><g:message code="default.create.label" args="${[message(code: 'user.label')]}"/></g:link></li>
		
		<g:if test="${userInstance != null && userInstance?.id != null && userInstance?.id != sec.loggedInUserInfo(field:'id') as long}">
			<li class="nav-header"><g:message code="actions.for.this.label" args="${[message(code: 'user.label')]}" /></li>
			<li><g:link action="edit" id="${userInstance?.id}"><g:message code="edit.information.label" /></g:link></li>
			<li><g:link action="password" id="${userInstance?.id}"><g:message code="change.password.label" /></g:link></li>
		</g:if>
	</ul>
</div>
</sec:ifAnyGranted>
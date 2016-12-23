<div class="well sidebar-nav">
	<ul class="nav nav-list">
		<li class="nav-header"><g:message code="actions.label" /></li>
		<li><g:link action="list">
				<g:message code="default.list.label" args="${[message(code: 'project.label')]}" />
			</g:link>
		</li>
		<li><g:link action="create">
				<g:message code="default.create.label" args="${[message(code: 'project.label')]}" />
			</g:link>
		</li>
		<g:if test="${projectInstance != null && projectInstance?.id != null && flag==true}">
		<li class="nav-header" style="margin-top:0;"><g:message code="actions.for.this.label"  args="${[message(code: 'project.label')]}" /></li>
		<li><g:link action="edit" id="${projectInstance?.id}">
				<g:message code="default.button.edit.label" />
			</g:link>
		</li>
		</g:if>
	</ul>
</div>

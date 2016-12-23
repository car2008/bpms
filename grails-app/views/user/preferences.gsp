<%@ page import="com.capitalbiotech.bpms.Preference"%>
<html>
<head>
<meta name="layout" content="main" />
<title>
<g:message code="system.preferences.label" />
</title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="system.preferences.label" />
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
				<g:if test="${preferenceMap && preferenceMap.size() > 0}">
				<g:form method="post">
					<g:each in="${preferenceMap}" var="key, value">
					<div class="control-group">
						<label class="control-label" for="${key}">${key}</label>
						<div class="controls">
							<input type="text" class="input-xxlarge" name="${key}" value="${value}" />
						</div>
					</div>
					</g:each>
					<div class="control-group">
						<div class="controls">
							<g:actionSubmit class="btn btn-primary" action="updatePreferences"
								value="${message(code: 'default.button.update.label', default: 'Update')}" />
						</div>
					</div>
				</g:form>
				</g:if>
				<g:else>
				<p><g:message code="not.available.label" /></p>
				</g:else>
			</div>
			<div class="span3">
				<g:render template="sidebar" />
			</div>
			<!--/span-->
		</div>
	</div>
</body>
</html>

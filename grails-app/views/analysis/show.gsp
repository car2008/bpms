<%@ page import="com.capitalbiotech.bpms.Analysis"%>
<html>
<head>
<meta name="layout" content="main" />
<title><g:message code="analysis.label" />: ${analysisInstance?.title}</title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="analysis.label" />: ${analysisInstance?.title}
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
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
				<p>${analysisInstance.description}</p>
			</div>
			<!-- /span -->
		</div>
	</div>
</body>
</html>

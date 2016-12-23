<head>
<meta name='layout' content='main' />
<title><g:message code="login.label" /></title>
</head>

<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="login.please.label" />
		</div>
	</div>
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<g:if test='${flash.message}'>
					<div class='alert alert-info'>
						${flash.message}
					</div>
				</g:if>
				<g:if test='${flash.error}'>
					<div class='alert alert-error'>
						${flash.error}
					</div>
				</g:if>
				<form action="${postUrl}" method="POST" class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="j_username"><g:message code="user.username.label" /></label>
						<div class="controls">
							<input name="j_username" type="text" >
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="j_password"><g:message code="user.password.label" /></label>
						<div class="controls">
							<input name="j_password" type="password" >
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="j_password"><g:message code="remember.me.label" /></label>
						<div class="controls">
							<input name="${rememberMeParameter}" type="checkbox" <g:if test="${hasCookie || !hasCookie}">checked="checked"</g:if>>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button class="btn btn-primary" type="submit"><g:message code="login.label" /></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>

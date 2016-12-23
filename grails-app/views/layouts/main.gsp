<!DOCTYPE html>
<html lang="en">
<head>
<title><g:layoutTitle /> - <g:message code="app.name" /></title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="wanglei">

<!-- Fav and touch icons -->
<link rel="shortcut icon" href="${resource(dir:'img',file:'favicon.ico')}" type="image/x-icon" />
<link href="${resource(dir:'css',file:'bootstrap.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'dataTables.bootstrap.min.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'fixedColumns.bootstrap.min.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'bootstrap-wysihtml5-0.0.2.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'chosen.css')}" rel="stylesheet">
<link href="${resource(dir:'css',file:'bpms.css')}" rel="stylesheet">
<link href="${resource(dir:'css/', file:'bootstrap-datepicker3.min.css')}" rel="stylesheet" type="text/css" />

<script src="${resource(dir:'js',file:'jquery.js')}"></script>
<script src="${resource(dir:'js',file:'bootstrap.js')}"></script>
<script src="${resource(dir:'js',file:'wysihtml5-0.3.0_rc2.js')}"></script>
<script src="${resource(dir:'js',file:'bootstrap-wysihtml5-0.0.2-zh.js')}"></script>
<script src="${resource(dir:'js',file:'chosen.jquery.js')}"></script>
<script src="${resource(dir:'js/', file:'bootstrap-datepicker.min.js')}"></script>
<script src="${resource(dir:'js/', file:'bootstrap-datepicker.zh-CN.min.js')}"></script>

<script src="${resource(dir:'js/', file:'jquery.dataTables.min.js')}"></script>
<script src="${resource(dir:'js/', file:'dataTables.bootstrap.min.js')}"></script>
<script src="${resource(dir:'js/', file:'dataTables.fixedColumns.min.js')}"></script>
<g:layoutHead />

</head>
<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="#"><img src="${resource(dir:'img',file:message(code: 'img.logo_text_small'))}" /></a>
				<sec:ifLoggedIn>
					<bpms:setLoggedInUser var="loggedInUser" />
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li ${params.controller == 'project' ? ' class="active"' : ''}>
								<g:link controller="project" action="list">
									<g:message code="default.list.label" args="${[message(code: 'project.label')]}" />
								</g:link>
							</li>
							<li ${params.controller == 'worktime' ? ' class="active"' : ''}>
								<g:link controller="worktime" action="listall">
									<g:message code="default.list.label" args="${[message(code: 'worktime.label')]}" />
								</g:link>
							</li>
						</ul>
						<ul class="nav pull-right">
							<li class="dropdown">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#">
									${loggedInUser.name}
									<g:if test="${loggedInUser.unread > 0}">
										<span class="label label-important">${loggedInUser.unread}</span>
									</g:if>
									<b class="caret"></b>
									</a>
								<ul class="dropdown-menu">
									<li style="text-align: center; padding-top: 10px; padding-bottom: 10px;">
										<img src="${createLink(controller: 'user', action: 'avatar', id: loggedInUser?.id)}" class="img-circle" style="width: 48px; height: 48px">
									</li>
									<sec:ifAnyGranted roles="ROLE_ADMIN">
										<li><g:link controller="user" action='preferences'><g:message code="system.preferences.label" /></g:link></li>
										<li><g:link controller="user" action='list'><g:message code="default.list.label" args="${[message(code: 'user.label')]}"/></g:link></li>
										<li><g:link controller="user" action="create"><g:message code="default.create.label" args="${[message(code: 'user.label')]}"/></g:link></li>
										<li class="divider"></li>
									</sec:ifAnyGranted>
									<li><g:link controller="user" action='edit'><g:message code="edit.my.information.label" /></g:link></li>
									<li><g:link controller="user" action='password'><g:message code="change.my.password.label" /></g:link></li>
									<li class="divider"></li>
									<li><g:link controller="logout"><g:message code="logout.label" /></g:link></li>
								</ul>
							</li>
						</ul>
					</div>
				</sec:ifLoggedIn>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>
	
	<g:layoutBody />
		
	<div class="container-fluid">
		<div class="row-fluid">
			<hr>
			<p>
				<g:message code="copyright.label" />
			</p>
		</div>
	</div>
	<!-- /container -->

	<script type="text/javascript">
		var $buoop = {
			vs : {
				i : 8,
				f : 3.6,
				o : 10.6,
				s : 4,
				n : 9
			}
		}
		$buoop.ol = window.onload;
		window.onload = function() {
			try {
				if ($buoop.ol)
					$buoop.ol();
			} catch (e) {
			}
			var e = document.createElement("script");
			e.setAttribute("type", "text/javascript");
			e.setAttribute("src", "${resource(dir:'js',file:'update-browser.js')}");
			document.body.appendChild(e);
		}
	</script>
</body>
</html>
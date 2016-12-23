<%@ page import="com.capitalbiotech.bpms.Project;com.capitalbiotech.bpms.Util"%>
<html>
<head>
<meta name="layout" content="main" />
<title>
	<g:message code="project.label" />: ${projectInstance?.title}
</title>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="project.label" />: ${projectInstance?.title}
			<div class="pull-right">
				<g:message code="project.status.${projectInstance?.status}.label" />
			</div>
		</div>
	</div>
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span6" style="width:1000px;">
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
				<g:hasErrors bean="${messageInstance}">
					<div class="alert alert-error">
						<g:renderErrors bean="${messageInstance}" as="list" />
					</div>
				</g:hasErrors>
				<g:hiddenField name="flag" id="flag" value="${flag }" />
				<IFRAME  scrolling="yes" width="100%" height="400" frameBorder="0" id="frmright" name="frmright" src="../../worktime/list/${projectInstance?.id}"  allowTransparency="true"></IFRAME>
				
				<g:form controller="message" action="save" method="POST" >
					<g:hiddenField id="projectId" name="projectId" value="${projectInstance?.id}" />
					<textarea id="content" name="content" rows="5" class="input-block-level"></textarea>
					<button class="btn btn-primary" type="submit">
						<g:message code="default.create.label" args="${[message(code: 'message.label')]}" />
					</button>
				</g:form>
				
				<hr/>
				
				<g:set var="lastOwner" value="${null}" />
				<g:each in="${messageInstanceList}" status="i" var="messageInstance">
					<div class="media">
						<g:if test="${messageInstance?.owner?.id != lastOwner?.id}">
						<g:link controller="user" action="show" id="${messageInstance?.owner?.id}" class="pull-left">
							<img src="${createLink(controller: 'user', action: 'avatar', id: messageInstance?.owner?.id)}" class="img-circle" style="width: 48px; height: 48px">
						</g:link>
						</g:if>
						<g:else>
							<div class="pull-left" style="width:48px; height:48px;"></div>
						</g:else>
						<div class="media-body">
							<small><g:link controller="user" action="show" id="${messageInstance?.owner?.id}">${messageInstance?.owner?.name}</g:link></small>
							<br/>
							<small><g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${messageInstance?.dateCreated}" /></small>
							<br/>
							<small>${messageInstance?.content}</small>
						</div>
					</div>
					<g:set var="lastOwner" value="${messageInstance?.owner}" />
				</g:each>
			</div>
			
			<!-- /span -->
			<div class="span3" style="width:200px;padding-left: 30px; border-left: 1px dashed #cccccc">
				<g:each in="${trackInstanceList}" status="i" var="trackInstance">
						<small><g:link controller="user" action="show" id="${trackInstance?.operator?.id}">${trackInstance?.operator?.name}</g:link></small>
						<br/>
						<small><g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${trackInstance?.dateCreated}" /></small>
						<br/>
						<small><g:message code="project.status.${trackInstance.status}.label" /></small>
						<br/><br/>
				</g:each>
			</div>
			<!--/span-->
			<div class="span3" style="width:200px;">
				<g:render template="sidebar" />
				
				<g:if test="${projectInstance != null && projectInstance?.id != null && flag==true || projectInstance?.fileName != null}">
					<div class="well sidebar-nav">
						<ul class="nav nav-list">
							<li class="nav-header"><g:message code="fileName.label" /></li>
								<g:if test="${projectInstance != null && projectInstance?.fileName != null}">
									<li>
										<g:link controller="project" action="download" id="${projectInstance?.id}" >${fileRealName }</g:link>  
									</li>
								</g:if>
								<g:else>
									<li>
										<g:link controller="project" action="edit" id="${projectInstance?.id}" >尚未上传文件</g:link>  
									</li>
								</g:else>
						</ul>
					</div>
				</g:if>
				
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header"><g:message code="project.label" /></li>
						<li>
							<span>
								<g:message code="project.supervisors.label" />:
								<g:each in="${projectInstance?.supervisors}" var="supervisor">${supervisor.name}</g:each>
							</span>
						</li>
						<li>
							<span>
								<g:message code="project.analysts.label" />:
								<g:each in="${projectInstance?.analysts}" var="analyst">${analyst.name}</g:each>
							</span>
						</li>
						<li>
							<span>
								<g:message code="project.sellers.label" />:
								<g:each in="${projectInstance?.sellers}" var="seller">${seller.name}</g:each>
							</span>
						</li>
						<li>
							<span>
								<g:message code="project.level.label" />:
								<bpms:projectLevelLabel level="${projectInstance?.level}" />
							</span>
						</li>
						<li>
							<span>
								<g:message code="project.dueTime.label" />:
								<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.dueTime}" />
							</span>
						</li>
					</ul>
				</div>
				
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header"><g:message code="platform.label" /></li>
						<g:each in="${projectInstance?.platforms}" var="platformInstance">
							<li>
									${platformInstance.title}
							</li>
						</g:each>
						<li class="nav-header"><g:message code="experiment.label" /></li>
						<g:each in="${projectInstance?.experiments}" var="experimentInstance">
							<li>
									${experimentInstance.title}
							</li>
						</g:each>
					</ul>
				</div>
				
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li class="nav-header"><g:message code="analysis.label" /></li>
						<g:each in="${projectInstance?.analyses}" var="analysisInstance">
							<li>
									${analysisInstance.title}
							</li>
						</g:each>
					</ul>
				</div>
			</div>
			<!--/span-->
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$('#content').wysihtml5({'font-styles': false});
			//alert($('iframe.wysihtml5-sandbox').css('style'));
		});
	</script>
</body>
</html>
		
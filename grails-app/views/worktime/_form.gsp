<%@ page import="com.capitalbiotech.bpms.Worktime" %>
<g:set var="today" value="${formatDate(format:'yyyy-MM-dd', date:new Date())}" />
<g:set var="finishedDate" value="${worktimeInstance?.finishedDate != null ? formatDate(date: worktimeInstance?.finishedDate, format: 'yyyy-MM-dd') : today}"/>
<g:set var="projectId" value="${projectInstance.id}" />
<style>
	body{
		padding-top:0px;
		padding-bottom:0px;
	}
	input.formControl{
		width:220px;
		height:30px;
	}
	textarea.formTextarea{
		width:800px;
		height:180px;
	}
</style>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'contract', 'error')}" style="display:none;">
	<label class="control-label" for="contract"><g:message code="project.contract.label" /></label>
	<div class="controls">
		<input type="text" id="contract" class="formControl" name="contract" readonly="readonly" value="${projectInstance?.contract}"/>
		<span class="help-inline"></span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'platforms', 'error')}">
	<label class="control-label" for="platforms"><g:message code="platform.label" /></label>
	<div class="controls">
		<select id="platforms" name="platforms" multiple data-placeholder=" ">
			<g:each in="${platformInstanceList}" var="platformInstance">
				<option value="${platformInstance?.id}" ${projectInstance?.platforms?.collect{it.id}?.contains(platformInstance.id) ? 'selected' : ''} >${platformInstance?.title}</option>
			</g:each>
		</select>
		<span class="help-inline">自动填写</span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'workcontents', 'error')}">
	<label class="control-label" for="workcontents"><g:message code="workcontent.label" /></label>
	<div class="controls">
		<select id="workcontents" name="workcontents" data-placeholder=" ">
			<g:each in="${workcontentInstanceList}" var="workcontentInstance">
				<option value="${workcontentInstance?.id}" ${worktimeInstance?.workcontents?.collect{it.id}?.contains(workcontentInstance.id) ? 'selected' : ''}>${workcontentInstance?.title}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'completers', 'error')}">
	<label class="control-label" for="completers"><g:message code="worktime.completers.label" /></label>
	<div class="controls">
		<select id="completers" name="completers" multiple data-placeholder=" ">
			<g:each in="${analystInstanceList}" var="analystInstance">
				<option value="${analystInstance?.id}" ${worktimeInstance?.completers?.collect{it.id}?.contains(analystInstance.id) ? 'selected' : ''}  ${disabledValue}>${analystInstance?.name}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'workWay', 'error')}">
	<label class="control-label" for="workWay"><g:message code="worktime.workWay.label" /></label>
	<div class="controls">
		<select id="workWay" name="workWay">
			<g:each in="${Worktime.constraints.workWay.inList}" var="workWay">
				<option value="${workWay}" ${worktimeInstance?.workWay == workWay ? 'selected' : ''} ${disabledValue}>
					<g:message code="worktime.way.${workWay}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'finishedDate', 'error')}">
	<label class="control-label" for="finishedDate"><g:message
				code="worktime.finishedDate.label" /></label>
	<div class="controls">
		<input type="text" name="finishedDate" class="formControl" id="finishedDate" data-date="${finishedDate}" data-date-format="yyyy-mm-dd" value="${finishedDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'manHour', 'error')}">
	<label class="control-label" for="manHour"><g:message code="worktime.manHour.label" /></label>
	<div class="controls">
		<input type="text" name="manHour" class="formControl" id="manHour" value="${worktimeInstance?.manHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'machineHour', 'error')}">
	<label class="control-label" for="machineHour"><g:message code="worktime.machineHour.label" /></label>
	<div class="controls">
		<input type="text" name="machineHour" class="formControl" id="machineHour" value="${worktimeInstance?.machineHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'comment2', 'error')}">
	<label class="control-label" for="comment2"><g:message code="worktime.comment2.label" /></label>
	<div class="controls">
		<textarea  name="comment2" id="comment2"  class="formTextarea" style= "resize:none; ">${worktimeInstance?.comment2}</textarea>
	</div>
</div>
<script type="text/javascript">
$(function(){
	$('#finishedDate').datepicker({
	    format:         "yyyy-mm-dd",
	    clearBtn:       true,
	    language:       "zh-CN",
	    autoclose:      true,
	    todayHighlight: true,
	});
	$("#platforms").chosen({});
	$("#workcontents").chosen({});
	$("#completers").chosen({});
});
</script>
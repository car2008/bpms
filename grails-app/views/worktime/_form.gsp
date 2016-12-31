<%@ page import="com.capitalbiotech.bpms.Worktime" %>
<g:set var="projectId" value="${projectInstance.id}" />
<div class="control-group ${hasErrors(bean: projectInstance, field: 'contract', 'error')}">
	<label class="control-label" for="contract"><g:message code="project.contract.label" /></label>
	<div class="controls">
		<input type="text" id="contract" name="contract" readonly="readonly" value="${projectInstance?.contract}"/>
		<span class="help-inline"><g:message code="required.label" /></span>
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
		<input type="text" name="finishedDate" id="finishedDate" data-date="${finishedDate}" data-date-format="yyyy-mm-dd" value="${finishedDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'manHour', 'error')}">
	<label class="control-label" for="manHour"><g:message code="worktime.manHour.label" /></label>
	<div class="controls">
		<input type="text" name="manHour" id="manHour" value="${worktimeInstance?.manHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'machineHour', 'error')}">
	<label class="control-label" for="machineHour"><g:message code="worktime.machineHour.label" /></label>
	<div class="controls">
		<input type="text" name="machineHour" id="machineHour" value="${worktimeInstance?.machineHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: worktimeInstance, field: 'comment2', 'error')}">
	<label class="control-label" for="comment2"><g:message code="worktime.comment2.label" /></label>
	<div class="controls">
		<textarea rows="5" cols="20" name="comment2" id="comment2" style= "resize:none; ">${worktimeInstance?.comment2}</textarea>
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
});
</script>
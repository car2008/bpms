<%@ page import="com.capitalbiotech.bpms.Project" %>
<g:set var="today" value="${new Date()}" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="layout" content="main" />
<title>
	<g:message code="default.list.label" args="${[message(code: 'project.label')]}" />
</title>
<style type="text/css">
#Layer1 {
    height: 470px;
    width: 450px;
    border: 5px solid #999;
    margin-right: auto;
    margin-left: auto;
    z-index: 50;
    display: none;
    position: fixed;
    top:20%;
    left:30%; 
    right:auto; 
    bottom:auto; 
    background-color: #FFF;
}
#Layer1 #win_top {
    height: 30px;
    width: 450px;
    border-bottom-width: 1px;
    border-bottom-style: solid;
    border-bottom-color: #999;
    line-height: 30px;
    color: #666;
    font-family: "微软雅黑", Verdana, sans-serif, "宋体";
    font-weight: bold;
    text-indent: 1em;
}
#Layer1 #win_top a {
    float: right;
    margin-right: 5px;
}
#shade {
    background-color:#000;
    position:absolute;
    z-index:49;
    display:none;
    width:100%;
    height:100%;
    opacity:0.6;
    filter: alpha(opacity=60);
    -moz-opacity: 0.6;
    margin: 0px;
    left: 0px;
    top: 0px;
    right: 0px;
    bottom: 0px;
}
#Layer1 .content {
    margin-top: 5px;
    margin-right: 30px;
    margin-left: 30px;
}
#tableToExcel td{
	width: 300px;
    vertical-align:middle;/*设置垂直居中*/
}
#secondRow td input{
	width:100%;
	height:28px;
	padding:0 0;
}
#secondRow td select{
	width:100%;
	padding:0 0;	
}
#showduetimetable th,td{
	padding-top:3%;
	 padding-right: auto;
    padding-left: 15px;
	background-color:#F5F5F5;
	font-family:"Times New Roman",Georgia,Serif;
	 line-height:25px;
	 font-size:10px;
}
</style>
</head>
<body>
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="default.list.label" args="${[message(code: 'project.label')]}" />
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9" style="width:84%;min-height:600px;overflow:auto;">
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
				
				<ul class="nav nav-pills">
					<g:if test="${params.action == 'list'}">
						<li class="active">
							<a href="javascript:;">和我相关的项目 / ${myProjectInstanceTotal}</a>
						</li>
						<li>
							<g:link action="listall">全部项目 / ${allProjectInstanceTotal}</g:link>
						</li>
						<li>
							<a href="javascript:;">搜索结果 / ${searchProjectInstanceTotal}</a>
						</li>
					</g:if>
					<g:if test="${params.action == 'listall'}">
						<li>
							<g:link action="list">和我相关的项目 / ${myProjectInstanceTotal}</g:link>
						</li>
						<li class="active">
							<a href="javascript:;">全部项目 / ${allProjectInstanceTotal}</a>
						</li>
						<li>
							<a href="javascript:;">搜索结果 / ${searchProjectInstanceTotal}</a>
						</li>
					</g:if>
					<g:if test="${params.action == 'searchProject' || params.action == 'searchProjectByColumn'|| params.action == 'searchProjectByDayNum'}">
						<li>
							<g:link action="list">和我相关的项目 / ${myProjectInstanceTotal}</g:link>
						</li>
						<li >
							<g:link action="listall">全部项目 / ${allProjectInstanceTotal}</g:link>
						</li>
						<li class="active">
							<a href="javascript:;">搜索结果 / ${searchProjectInstanceTotal}</a>
						</li>
					</g:if>
				</ul>
					<table id="example" class="table table-hover nowrap" style="width:5000px;">
						<thead>
							<tr>
								<th >
									<%--<g:checkBox name="allChecked" id="ckAll" onclick="selectAll()"/>--%>
									<g:checkBox name="allChecked" id="ckAll" onclick="selectAll()" />全选
									<g:checkBox name="invertChecked" id="ckInvert" onclick="selectInvert()" />反选
								</th>
								<%-- <g:sortableColumn property="title" title="${message(code: 'project.title.label')}"/> --%>
								<g:sortableColumn property="projectCreateDate"
									title="${message(code: 'project.projectCreateDate.label')}"  />
								<th  >${message(code: 'project.contract.label')} </th>
								<th  >${message(code: 'project.information.label')}</th>
								<th  >${message(code: 'project.level.label')} </th>
								<th  >${message(code: 'project.status.label')} </th>
								<th  >${message(code: 'project.platforms.label')}</th>
								<th  >${message(code: 'project.experiments.label')}</th>
								<th  >${message(code: 'project.species.label')}</th>
								<th  >${message(code: 'project.samplesize.label')}</th>
								<th  >${message(code: 'project.k3number.label')}</th>
								<th  >${message(code: 'project.batch.label')}</th>
								<th  >${message(code: 'project.analyses.label')}</th>
								<th  >${message(code: 'project.salesman.label')}</th>
								<th  >${message(code: 'project.supervisors.label')}</th>
								<th  >${message(code: 'project.analysts.label')}</th>
								<th  >${message(code: 'project.sellers.label')}</th>
								<th  >${message(code: 'project.analyStartDate.label')}</th>
								<th  >${message(code: 'project.analySendDate.label')}</th>
								<th  >${message(code: 'project.analySendWay.label')}</th>
								<th  >${message(code: 'project.manHour.label')}</th>
								<th  >${message(code: 'project.machineHour.label')}</th>
								<th  >${message(code: 'project.dueTime.label')}</th>
								<th  >${message(code: 'project.innerDueDate.label')}</th>
								<th  >${message(code: 'project.overdueReason.label')}</th>
								<th  >${message(code: 'project.isRemoted.label')}</th>
								<th  >${message(code: 'project.backupDate.label')}</th>
								<th  >${message(code: 'project.backupLocation.label')}</th>
								<th  >${message(code: 'project.isControled.label')}</th>
								<th  >${message(code: 'project.comment1.label')}</th>
								<th  >${message(code: 'project.libraryBuildWay.label')}</th>
								<th  >${message(code: 'project.readLength.label')}</th>
								<th  >${message(code: 'project.readsNum.label')}</th>
								<th  >${message(code: 'project.dataSize.label')}</th>
								<th  >${message(code: 'project.spliters.label')}</th>
								<th  >${message(code: 'project.metaSendData.label')}</th>
								<th  >${message(code: 'project.metaSendWay.label')}</th>	
							</tr>
							<g:form id="searchProjectByColumn" name="searchProjectByColumn"  action="searchProjectByColumn" style="margin-bottom:0;">
							<tr id="secondRow">
								<td></td>
						        <td>
						        	<span><input type="text" name="beginSearchDate" id="beginSearchDate" value="${beginSearchDate}" style="width:75px;" onchange="searchDate(this.id)"/></span>
								  ~ <span><input type="text" name="endSearchDate" id="endSearchDate" value="${endSearchDate}" style="width:75px;" onchange="searchDate(this.id)"/></span>
						        </td>
						        <td><input id="q2" name="q2" type="text" class="form-control"  data-index="3" value="${params.q2}" onchange="search(this.id)"/></td>
						        <td><input id="q3" name="q3" type="text" class="form-control"  data-index="4" value="${params.q3}" onchange="search(this.id)"/></td>
						        <td>
						        	<select id="q4" name="q4" data-index="5" style="width:54px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<option value="LEVEL_LOW" ${"LEVEL_LOW".equals(params.q4) ? 'selected' : ''}  >
											<g:message code="project.level.LEVEL_LOW.label" />
										</option>
										<option value="LEVEL_NORMAL" ${"LEVEL_NORMAL".equals(params.q4) ? 'selected' : ''}  >
											<g:message code="project.level.LEVEL_NORMAL.label" />
										</option>
										<option value="LEVEL_HIGH" ${"LEVEL_HIGH".equals(params.q4) ? 'selected' : ''} >
											<g:message code="project.level.LEVEL_HIGH.label" />
										</option>
						    		</select>
					    		</td>
						        <td>
						        	<select id="q5" name="q5" data-index="6" style="width:96px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<option value="STATUS_WAIT" ${"STATUS_WAIT".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_WAIT.label" />
										</option>
										<option value="STATUS_DATA_ACCEPTED" ${"STATUS_DATA_ACCEPTED".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_DATA_ACCEPTED.label" />
										</option>
										<option value="STATUS_STARTED" ${"STATUS_STARTED".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_STARTED.label" />
										</option>
										<option value="STATUS_COMPLETED" ${"STATUS_COMPLETED".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_COMPLETED.label" />
										</option>
										<option value="STATUS_RELEASED" ${"STATUS_RELEASED".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_RELEASED.label" />
										</option>
										<option value="STATUS_ARCHIVED" ${"STATUS_ARCHIVED".equals(params.q5) ? 'selected' : ''} >
											<g:message code="project.status.STATUS_ARCHIVED.label" />
										</option>
						    		</select>
					    		</td>
						    	<td>
						    		<select id="q6" name="q6" data-index="7" style="width:196px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${platformInstanceList}" var="platformInstance">
											<option value="${platformInstance?.code}" ${platformInstance?.code==params.q6 ? 'selected' : ''}  >${platformInstance?.title}</option>
										</g:each>
						    		</select>
					    		</td>
						    	<td>
						    		<select id="q7" name="q7" data-index="8" style="width:92px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${experimentInstanceList}" var="experimentInstance">
											<option value="${experimentInstance?.code}" ${experimentInstance?.code==params.q7 ? 'selected' : ''}>${experimentInstance?.title}</option>
										</g:each>
						    		</select>
						    	</td>
						    	<td>
						    		<input id="q8" name="q8" type="text" class="form-control"  data-index="9" value="${params.q8}" onchange="search(this.id)"/>
						    	</td>
						    	<td></td>
						    	<td>
						    		<input id="q9" name="q9" type="text" class="form-control"  data-index="11" value="${params.q9}" onchange="search(this.id)"/>
						    	</td>
						    	<td>
						    	</td>
						    	<td >
						    		<select id="q10" name="q10" data-index="13" style="width:122px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${analysisInstanceList}" var="analysisInstance">
											<option value="${analysisInstance?.code}" ${analysisInstance?.code==params.q10 ? 'selected' : ''}>${analysisInstance?.title}</option>
										</g:each>
						    		</select>
						    	</td>
						    	<td>
						    		<input id="q11" name="q11" type="text" class="form-control"  data-index="9" value="${params.q11}" onchange="search(this.id)"/>
						    	</td>
						    	<td>
						    		<select id="q12" name="q12" data-index="13" style="width:122px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${supervisorInstanceList}" var="supervisorInstance">
											<option value="${supervisorInstance?.username}" ${supervisorInstance?.username==params.q12 ? 'selected' : ''}>${supervisorInstance?.name}</option>
										</g:each>
						    		</select>
						    	</td>
						    	<td>
						    		<select id="q13" name="q13" data-index="13" style="width:122px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${analystInstanceList}" var="analystInstance">
											<option value="${analystInstance?.username}" ${analystInstance?.username==params.q13 ? 'selected' : ''}>${analystInstance?.name}</option>
										</g:each>
						    		</select>
						    	</td>
						    	<td>
						    		<select id="q14" name="q14" data-index="13" style="width:122px;" onchange="search(this.id)">
						    			<option value="" selected></option>
						    			<g:each in="${sellerInstanceList}" var="sellerInstance">
											<option value="${sellerInstance?.username}" ${sellerInstance?.username==params.q14 ? 'selected' : ''}>${sellerInstance?.name}</option>
										</g:each>
						    		</select>
						    	</td>
						    	<td>
						    	</td>
						    	<td>
						    	</td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td>
						    	</td>
						    	<td>
						    	</td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	<td></td>
						    	
						    </tr>
						    </g:form>
						</thead>
						<tbody>
							<g:form id="form" name="myForm"  action="exportToExcel" >
							<g:each in="${projectInstanceList}" var="projectInstance">
							<g:if test="${(projectInstance?.analySendDate > projectInstance?.innerDueDate && projectInstance?.innerDueDate) || (today > projectInstance?.innerDueDate && projectInstance?.analySendDate==null)}">
							     <tr id="projectTr" bgcolor="F4B4AF">
							</g:if>
							<g:else>
							    <tr id="projectTr" bgcolor="${projectInstance?.isControled == true && projectInstance?.isRemoted == true ? '#C1C1C1' : ''}">
							</g:else>
									<td  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<g:checkBox id="check" name="sub" value="${projectInstance.id}" checked="false" onclick="change()"/>
										<small>
											<g:if test="${noticeInstanceMap[projectInstance.id] != null && noticeInstanceMap[projectInstance.id] > 0}">
												<span class="badge badge-important">
													${noticeInstanceMap[projectInstance.id]}条
												</span>
											</g:if>
											<g:if test="${remaindingDayMap[projectInstance.id] != null && ""!= remaindingDayMap[projectInstance.id]}">
												<g:if test="${remaindingDayMap[projectInstance.id] > 5 }">
													<span class="badge badge-info">
														${remaindingDayMap[projectInstance.id]}天
													</span>
												</g:if>
												<g:else>
													<span class="badge badge-important">
														${remaindingDayMap[projectInstance.id]}天
													</span>
												</g:else>
											</g:if>
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.projectCreateDate}" />
										</small>
									</td>
									<td  >
										<small>
											<g:link action="show" id="${projectInstance.id}">
												${projectInstance?.contract}
											</g:link>
										</small>
									</td>
									<td  >
										<small>
											<g:link action="show" id="${projectInstance.id}">
												${projectInstance?.information}
											</g:link>
										</small>
									</td>
									<td  >
										<small>
											<bpms:projectLevelLabel level="${projectInstance?.level}" />
										</small>
									</td>
									<td  >
										<small>
											<g:message code="project.status.${projectInstance?.status}.label" />
										</small>
									</td>
									
									<td  >
										<small>
											<g:each in="${projectInstance?.platforms}" var="platformInstance" status="platformIndex">
												<g:if test="${platformIndex > 0}">
													<br />
												</g:if>
												${platformInstance.title}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.experiments}" var="experimentInstance" status="experimentIndex">
												<g:if test="${experimentIndex > 0}">
													<br />
												</g:if>
												${experimentInstance.title}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.species}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.samplesize}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.k3number}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.batch}
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.analyses}" var="analysisInstance" status="analysisIndex">
												<g:if test="${analysisIndex > 0}">
													<br />
												</g:if>
												${analysisInstance.title}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.salesman}
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.supervisors}" var="supervisor">
												${supervisor.name}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.analysts}" var="analyst">
												${analyst.name}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.sellers}" var="seller">
												${seller.name}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.analyStartDate}" />
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.analySendDate}" />
										</small>
									</td>
									<td  >
										<small>
												<g:message code="project.way.${projectInstance?.analySendWay}.label" />
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.manHour}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.machineHour}
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.dueTime}" />
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.innerDueDate}" />
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.overdueReason}
										</small>
									</td>
									<td  >
										<small>
												<g:message code="project.isRemoted.${projectInstance?.isRemoted}.label" />
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.backupDate}" />
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.backupLocation}
										</small>
									</td>
									<td  >
										<small>
												<g:message code="project.isControled.${projectInstance?.isControled}.label" />
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.comment1}
										</small>
									</td>
									<td  >
										<small>
												<g:message code="project.way.${projectInstance?.libraryBuildWay}.label" />
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.readLength}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.readsNum}
										</small>
									</td>
									<td  >
										<small>
												${projectInstance?.dataSize}
										</small>
									</td>
									<td  >
										<small>
											<g:each in="${projectInstance?.spliters}" var="spliter">
												${spliter.name}
											</g:each>
										</small>
									</td>
									<td  >
										<small>
											<g:formatDate format="yyyy-MM-dd" date="${projectInstance?.metaSendData}" />
										</small>
									</td>
									<td  >
										<small>
												<g:message code="project.way.${projectInstance?.metaSendWay}.label" />
										</small>
									</td>
								</tr>
							</g:each>
							<g:submitButton id="sub" name="submit" value="导出excel" />
							&nbsp;&nbsp;<input type="button" id="openwin" name="openwin" value="选择导出列" onclick="shade.style.display='block';Layer1.style.display='block'"/>
							&nbsp;&nbsp;<input type="submit" id="exportAll" name="exportAll" value="导出所有项目" />
							<sec:ifAnyGranted roles="ROLE_ADMIN">
								&nbsp;&nbsp;<input type="button" id="backup" name="backup" value="备份数据" onclick="backupSql()"/>
								&nbsp;&nbsp;<input type="button" id="restore" name="restore" value="重置数据" onclick="restoreSql()"/>
							</sec:ifAnyGranted>
							<g:if test="${itemNum == '0' ? false : true}">
							<input style="border:0;BACKGROUND-COLOR: transparent;" readOnly name="itemNum" value="已选择【${itemNum}】列" />
							</g:if>
							<g:hiddenField name="projectItemList" value="${projectItemList}" />
							</g:form>
						</tbody>
					</table>
					<div class="pagination">
						<bpms:paginate total="${projectInstanceTotal}" params="${params}" />
					</div>
			</div>
			<div class="span3" style="width:11%;">
				<g:render template="sidebar"/> 
			</div>
			<div class="span3" style="width:11%;background-color:#F5F5F5">
				<table class="showduetimetable" >
		<thead>
			<tr>
				<th>
					<g:if test="${flag==true}">
						<g:message code="showduetime.all.label" />
					</g:if>
					<g:else>
						<g:message code="showduetime.my.label" />
					</g:else>
				</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>
					<font style="color:green">已完成项目</font>:
				</td>
				<td>
					${myProjectDueDateMap["FINISHED_DUEDATE"]}个
				</td>
			</tr>
			<tr>
				<td>
					<font style="color:B94A48">过期的项目</font>:
				</td>
				<td>
					${myProjectDueDateMap["OVER_DUEDATE"]}个
				</td>
			</tr>
			<tr>
				<td>
					<font style="color:red">未完成项目</font>:
				</td>
				<td>
					${myProjectDueDateMap["UNFINISHED_DUEDATE"]}个
				</td>
			</tr>
			<tr>
				<td>
					<g:form url='[controller: "project", action: "searchProjectByDayNum"]' id="searchableDue" name="searchableDue" method="get" style="margin-bottom:0;">
						<input type="text" id="daynum" name="daynum" style="width:30px;border:0px;text-align:center" value="${daynum}"></input>天内到期:
					</g:form>
				</td>
				<td>
					<input type="text" id="projectcount" name="projectcount" value="${projectCount}" style="width:20px;height:20px;border:0px;background-color:F5F5F5;text-align:center" readonly></input>个
				</td>
			</tr>
		</tbody>
	</table>
			</div>
		</div>
		<div id="shade"></div>
		<div id="Layer1">
		  <div id="win_top">请选择要导出的列:<a href="#" onClick="shade.style.display='none';Layer1.style.display='none'">关闭</a></div>
		  <br />
		  <div class="content">
		  	<g:form id="form1" name="myForm1"  action="selectProjectItem" >
		  	<g:hiddenField name="offset" value="${offset}" />
		  	<g:hiddenField name="order" value="${order}" />
		  	<g:hiddenField name="max" value="${max}" />
		  	<g:hiddenField name="sort" value="${sort}" />
		  	<g:hiddenField name="lastAction" value="${params.action}" />
		  	<g:hiddenField name="q2" value="${params.q2}" />
		  	<g:hiddenField name="q3" value="${params.q3}" />
		  	<g:hiddenField name="q4" value="${params.q4}" />
		  	<g:hiddenField name="q5" value="${params.q5}" />
		  	<g:hiddenField name="q6" value="${params.q6}" />
		  	<g:hiddenField name="q7" value="${params.q7}" />
		  	<g:hiddenField name="q8" value="${params.q8}" />
		  	<g:hiddenField name="q9" value="${params.q9}" />
		  	<g:hiddenField name="q10" value="${params.q10}" />
		  	<g:hiddenField name="q11" value="${params.q11}" />
		  	<g:hiddenField name="q12" value="${params.q12}" />
		  	<g:hiddenField name="q13" value="${params.q13}" />
		  	<g:hiddenField name="q14" value="${params.q14}" />
		  	<table id="tableToExcel"  border="1">
		  		<tr>
				    <td><g:checkBox name="sub1" checked="false" value="title"/>&nbsp;项目编号</td>
				    <td><g:checkBox name="sub1" checked="false" value="analysts"/>&nbsp;数据分析员</td>
				    <td><g:checkBox name="sub1" checked="false" value="libraryBuildWay"/>&nbsp;建库方式</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="projectCreateDate"/>&nbsp;登记日期</td>
				    <td><g:checkBox name="sub1" checked="false" value="sellers"/>&nbsp;审核员</td>
				    <td><g:checkBox name="sub1" checked="false" value="readLength"/>&nbsp;测序读长</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="contract"/>&nbsp;合同号</td>
				    <td><g:checkBox name="sub1" checked="false" value="analyStartDate"/>&nbsp;分析开始日期</td>
				    <td><g:checkBox name="sub1" checked="false" value="readsNum"/>&nbsp;reads/样本</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="information"/>&nbsp;项目信息</td>
				    <td><g:checkBox name="sub1" checked="false" value="analySendDate"/>&nbsp;分析给出日期</td>
				    <td><g:checkBox name="sub1" checked="false" value="dataSize"/>&nbsp;数据量</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="level"/>&nbsp;优先级</td>
				    <td><g:checkBox name="sub1" checked="false" value="analySendWay"/>&nbsp;分析给出方式</td>
				    <td><g:checkBox name="sub1" checked="false" value="spliters"/>&nbsp;CASAVA拆分员</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="status"/>&nbsp;状态</td>
				    <td><g:checkBox name="sub1" checked="false" value="manHour"/>&nbsp;工时</td>
				    <td><g:checkBox name="sub1" checked="false" value="metaSendData"/>&nbsp;原始数据发送时间</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="platforms"/>&nbsp;检测平台</td>
				    <td><g:checkBox name="sub1" checked="false" value="machineHour"/>&nbsp;机时</td>
				    <td><g:checkBox name="sub1" checked="false" value="metaSendWay"/>&nbsp;原始数据给出方式</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="experiments"/>&nbsp;检测类型</td>
				    <td><g:checkBox name="sub1" checked="false" value="dueTime"/>&nbsp;到期时间</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="species"/>&nbsp;物种</td>
				    <td><g:checkBox name="sub1" checked="false" value="innerDueDate"/>&nbsp;内部到期时间</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="samplesize"/>&nbsp;芯片/样本数</td>
				    <td><g:checkBox name="sub1" checked="false" value="overdueReason"/>&nbsp;过期原因</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="k3number"/>&nbsp;k3编号</td>
				    <td><g:checkBox name="sub1" checked="false" value="isRemoted"/>&nbsp;是否远程备份</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="batch"/>&nbsp;批次</td>
				    <td><g:checkBox name="sub1" checked="false" value="backupDate"/>&nbsp;备份日期</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="analyses"/>&nbsp;工作性质</td>
				    <td><g:checkBox name="sub1" checked="false" value="backupLocation"/>&nbsp;备份位置</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="salesman"/>&nbsp;销售员</td>
				    <td><g:checkBox name="sub1" checked="false" value="isControled"/>&nbsp;是否管控完毕</td>
				</tr>
				<tr>
				    <td><g:checkBox name="sub1" checked="false" value="supervisors"/>&nbsp;技术支持</td>
				    <td><g:checkBox name="sub1" checked="false" value="comment1"/>&nbsp;备注1</td>
				</tr>
		  	</table>
		  		<br />
		  		<input type="submit" name="title" id="title" value="确定" />
		  	</g:form>
		  </div>
		</div>
	</div>
	
	<script>
	    window.onload=function(){
		   // alert(${itemNum})
		   // alert(${projectItemList})
		   // alert(123)
		   // alert(myjson.dataList[0].information)
		}
	    function searchDate(id){
	    	if(id=="endSearchDate"){
				var beginSearchDate=document.getElementById("beginSearchDate").value
				if(""==beginSearchDate){
				}else{
					search(id)
				}
			}
	    	if(id=="beginSearchDate"){
				var beginSearchDate=document.getElementById("endSearchDate").value
				if(""==beginSearchDate){
				}else{
					search(id)
				}
			}
		}
		function search(id){
			document.getElementById("searchProjectByColumn").submit()
		}
		function restoreSql(){
			// 每发一个请求，应该创建一个xhr对象 
	   		var xhr = new XMLHttpRequest();
	   		xhr.onreadystatechange = function() {
	   			// 响应完全返回, 并且响应成功了
	   			if(xhr.readyState == 4 && xhr.status == 200) {
	   				//var text = xhr.responseText;
	   				//text=text.replace("[", "");
	   				//text=text.replace("]", "");
	   				///console.log("123");
	   				//document.getElementById("title").value=parseInt(text)+1;
	   			}
	   		};
	   		
	    	//xhr.open("GET", "../backup/restore", true);//允许重置数据时候用   http://localhost:8080/bpms/project/findMaxId
	    	xhr.open("GET", "#", true);//暂时不让重置数据 http://localhost:8080/bpms/project/findMaxId
	    	xhr.send();
		}
		function backupSql(){
			// 每发一个请求，应该创建一个xhr对象 
	   		var xhr = new XMLHttpRequest();
	   		xhr.onreadystatechange = function() {
	   			// 响应完全返回, 并且响应成功了
	   			if(xhr.readyState == 4 && xhr.status == 200) {
	   				//var text = xhr.responseText;
	   				//text=text.replace("[", "");
	   				//text=text.replace("]", "");
	   				//console.log("123");
	   				//document.getElementById("title").value=parseInt(text)+1;
	   			}
	   		};
	   		
	    	xhr.open("GET", "../backup/backup", true);//http://localhost:8080/bpms/project/findMaxId
	    	xhr.send();
		}
		<%--复选框的全选与反选--%>
		function selectInvert(){
			document.getElementById("ckAll").checked =0;
			var checkboxs=document.getElementsByName("sub");
			 for (var i=0;i<checkboxs.length;i++) {
			  var e=checkboxs[i];
			  e.checked=!e.checked;
			 }
			 selectCount();
		}
		function selectAll(){
			document.getElementById("ckInvert").checked =0;
			var checkboxs=document.getElementsByName("sub");
			if(document.getElementById("ckAll").checked){
				   for(var i=0;i<checkboxs.length;i++)
				   {
					   checkboxs[i].checked = 1;
				   } 
			}else{
				  for(var j=0;j<checkboxs.length;j++)
				  {
					  checkboxs[j].checked = 0;
				  }
			}
		}
		function changetoAll(){
			document.getElementById("ckAll").checked =1;
	
		}
		function changetoNoAll(){
			document.getElementById("ckAll").checked =0;
		}
		function change(){
			var invertChecked= document.getElementById("ckInvert").checked; 
			if(invertChecked="true"){
				document.getElementById("ckInvert").checked =0;
			}
			selectCount();
		}
		function selectCount(){
			var checkboxs=document.getElementsByName("sub");
			var init=0;
		    for(var i=0;i<checkboxs.length;i++){
				if(checkboxs[i].checked){
					init=init+1;
				}
			}
			var offset= ${params.offset}+0;
			var bo=${params.max}<=${projectInstanceTotal}-offset ? true : false;
			if(bo==true){
				if(init==${params.max}){
					changetoAll();
				}else{
					changetoNoAll();
				}
			}else{
				if(init==${projectInstanceTotal}-offset){
					changetoAll();
				}else{
					changetoNoAll();
				}
			}
		}
		<%--复选框的全选与反选
		选择导出项目与列--%>
		$(function(){
			$('#beginSearchDate').datepicker({
			    format:         "yyyy-mm-dd",
			    clearBtn:       true,
			    language:       "zh-CN",
			    autoclose:      true,
			    todayHighlight: true,
			});
			$('#endSearchDate').datepicker({
			    format:         "yyyy-mm-dd",
			    clearBtn:       true,
			    language:       "zh-CN",
			    autoclose:      true,
			    todayHighlight: true,
			});
			
		});
		document.getElementById("daynum").onblur=function(){
			var val = document.getElementById('daynum');
			if(!isNaN(val.value)){
				var value = parseInt(document.getElementById('daynum').value);
	            if(value > 0 && value < 21){
					document.getElementById("searchableDue").submit()
	            }else{
	                alert('请输入1-20之间的正整数 ');
					document.getElementById('daynum').value="";
	            }
			}else{
			   alert('请输入1-20之间的正整数 ');
			}
		}
	</script>
</body>
</html>

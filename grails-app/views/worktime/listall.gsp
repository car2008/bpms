<%@ page import="com.capitalbiotech.bpms.Worktime"%>
<html>
<head>
<meta name="layout" content="main" />
<style type="text/css">
#worktimeTable {
}
#worktimeTable tr{
	border-bottom:1px solid #999999;
}
.pagination ul li.disabled{ 
   display:none; 
} 

.pagination a{ 
   border:1px solid #dddddd; 
   padding:6px 5px 6px 5px; 
   line-height:30px; 
}
#worktimeTable th,td{ 
   white-space: nowrap; 
}
</style>
<script type="text/javascript">
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
	
	$("#workcontents").chosen({});
	$("#completer").chosen({});
});
</script>
<title><g:message code="default.list.label" args="${[message(code: 'worktime.label')]}" /></title>
</head>
<body>
<div class="row-fluid">
	<div class="span12 bpms-header">
		<g:message code="default.list.label" args="${[message(code: 'worktime.label')]}" />
	</div>
</div>
<div class="worktimeList" style="margin-left:6%;width:85%;min-height:600px;overflow:auto;">
	<table id="worktimeTable" class="worktimeTable" cellPadding=15px cellSpacing=0px>
		<thead>
			<tr>
				<th>
					<g:checkBox name="allChecked" id="ckAll" onclick="selectAll()" />全选
					<g:checkBox name="invertChecked" id="ckInvert" onclick="selectInvert()" />反选
				</th>
				<th>${message(code: 'worktime.finishedDate.label')}</th>
				<th>${message(code: 'project.contract.label')}</th>
				<th>${message(code: 'project.samplesize.label')}</th>
				<th>${message(code: 'project.batch.label')}</th>
				<th>${message(code: 'project.platforms.label')}</th>
				<th>${message(code: 'worktime.workcontents.label')}</th>
				<th>${message(code: 'worktime.completer.label')}</th>
				<th>${message(code: 'worktime.workWay.label')}</th>
				<th>${message(code: 'worktime.manHour.label')}</th>
				<th>${message(code: 'worktime.machineHour.label')}</th>
				<th>${message(code: 'worktime.comment2.label')}</th>
			</tr>
			<g:form id="searchWorktimeByColumn" name="searchWorktimeByColumn"  action="searchWorktimeByColumn" style="margin-bottom:0;">
				<tr id="secondRow">
					<td style="width:50px;"></td>
					<td style="width:200px;">
						<span><input type="text" name="beginSearchDate" id="beginSearchDate" value="${beginSearchDate}" style="width:75px;" onchange="searchDate(this.id)"/></span>
					  ~ <span><input type="text" name="endSearchDate" id="endSearchDate" value="${endSearchDate}" style="width:75px;" onchange="searchDate(this.id)"/></span>
					</td>
					<td>
			        	<input id="q2" name="q2" type="text" class="form-control"  value="${params.q2}" onchange="search(this.id)"/>
			        </td>
			        <td style="width:55px;"></td>
			        <td></td>
			        <td>
			        	<select id="q4" name="q4" data-index="7" style="width:180px;" onchange="search(this.id)">
			    			<option value="" selected></option>
			    			<g:each in="${platformInstanceList}" var="platformInstance">
								<option value="${platformInstance?.code}" ${platformInstance?.code==params.q4 ? 'selected' : ''}  >${platformInstance?.title}</option>
							</g:each>
			    		</select>
			        	
		    		</td>
			        <td>
			        	<select id="q5" name="q5" data-index="13" style="width:122px;" onchange="search(this.id)">
						    <option value="" selected></option>
			    			<g:each in="${workcontentInstanceList}" var="workcontentInstance">
								<option value="${workcontentInstance?.code}" ${workcontentInstance?.code==params.q5 ? 'selected' : ''}>${workcontentInstance?.title}</option>
							</g:each>
						</select>
		    		</td>
			    	<td>
			    		<select id="q6" name="q6" data-index="13" style="width:80px;" onchange="search(this.id)">
			    			<option value="" selected></option>
			    			<g:each in="${analystInstanceList}" var="analystInstance">
								<option value="${analystInstance?.username}" ${analystInstance?.username==params.q6 ? 'selected' : ''}>${analystInstance?.name}</option>
							</g:each>
			    		</select>
		    		</td>
		    		<td>
		    			<select id="q7" name="q7" data-index="6" style="width:65px;" onchange="search(this.id)">
			    			<option value="" selected></option>
			    			<option value="WAY_USUAL" ${"WAY_USUAL".equals(params.q7) ? 'selected' : ''}  >
								<g:message code="worktime.way.WAY_USUAL.label" />
							</option>
							<option value="WAY_MAIL" ${"WAY_MAIL".equals(params.q7) ? 'selected' : ''}  >
								<g:message code="worktime.way.WAY_MAIL.label" />
							</option>
							<option value="WAY_OUT" ${"WAY_OUT".equals(params.q7) ? 'selected' : ''} >
								<g:message code="worktime.way.WAY_OUT.label" />
							</option>
							<option value="WAY_PHONE" ${"WAY_PHONE".equals(params.q7) ? 'selected' : ''} >
								<g:message code="worktime.way.WAY_PHONE.label" />
							</option>
			    		</select>
		    		</td>
		    		<td></td>
		    		<td></td>
		    		<td></td>
				</tr>
			</g:form>
		</thead>
		<tbody>
			<g:form id="form" name="myForm"  action="exportToExcel" >
			
					<g:each in="${worktimeInstanceList}" var="worktimeInstance">
					    <tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<small>
									<g:checkBox id="check" name="sub" value="${worktimeInstance.id}" checked="false" onclick="change()"/>
								</small>
							</td>
							<td>
								<small>
										<g:formatDate format="yyyy-MM-dd" date="${worktimeInstance?.finishedDate}" />
								</small>
							</td>
							<td>
								<small>
										<g:link controller="project" action="show" id="${worktimeInstance?.project.id}">
												${worktimeInstance?.contract}
										</g:link>
								</small>
							</td>
							<td>
								<small>
										${worktimeInstance?.project.samplesize}
								</small>
							</td>
							<td>
								<small>
										${worktimeInstance?.project.batch}
								</small>
							</td>
							<td>
								<small>
										<g:each in="${worktimeInstance.platforms}" var="platformInstance" status="platformIndex">
											<g:if test="${platformIndex > 0}">
												<br />
											</g:if>
											${platformInstance.title}
										</g:each>
								</small>
							</td>					    
						 	<td>
								<small>
									<g:each in="${worktimeInstance?.workcontents}" var="workcontent">
										${workcontent.title}
									</g:each>
								</small>
							</td>
							<td>
								<small>
									<g:each in="${worktimeInstance?.completers}" var="completer">
										${completer.name}
									</g:each>
								</small>
							</td>
							<td>
								<small>
										<g:message code="worktime.way.${worktimeInstance?.workWay}.label" />
								</small>
							</td>
							<td>
								<small>
										${worktimeInstance?.manHour}
								</small>
							</td>
							<td>
								<small>
										${worktimeInstance?.machineHour}
								</small>
							</td>
							<td>
								<small>
										${worktimeInstance?.comment2}
								</small>
							</td>
						</tr>
					</g:each>
			<g:submitButton id="submit" name="submit" value="导出excel" />
			&nbsp;&nbsp;<input type="submit" id="exportAll" name="exportAll" value="导出所有工时" />
			&nbsp;&nbsp;<span><input style="width:110px;border:0;BACKGROUND-COLOR: transparent;" readOnly name="itemNum" value="共【${worktimeInstanceTotal}】条记录" /></span>
			<g:if test="${worktimeInstanceTotalIdList}">
				<span><g:link action="exportToExcel" params="[worktimeInstanceTotalIdList: worktimeInstanceTotalIdList]" >导出全部搜索结果</g:link></span>
			</g:if>
			</g:form>
		</tbody>
	</table>
</div>
<div class="pagination" style="margin-left:8%;">
	<bpms:paginate total="${worktimeInstanceTotal}" params="${params}" />
</div>
	<script>
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
			document.getElementById("searchWorktimeByColumn").submit()
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
		<%--复选框的全选与反选--%>
	</script>
</body>
</html>
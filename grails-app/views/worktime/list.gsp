<%@ page import="com.capitalbiotech.bpms.Worktime"%>
<html>
<head>
<style type="text/css">
#worktimeTable {
    vertical-align:middle;/*设置垂直居中*/
    width:100%;
    margin-right: auto;
    margin-left: auto;
}
#worktimeTable td{ 
   border:1px solid #999999; 
}
#worktimeTable th{
	border:1px solid #999999;
	white-space: nowrap; 
}
.wrap{ 
   display: block; 
   word-break:break-all; 
}

</style>
<title><g:message code="default.list.label" args="${[message(code: 'worktime.label')]}" /></title>
</head>
<body>
	<table id="worktimeTable" class="worktimeTable" cellPadding=15px cellSpacing=0px >
		<thead>
			<tr>
				<th>
					<%--<g:checkBox name="allChecked" id="ckAll" onclick="selectAll()"/>--%>
					<g:checkBox name="allChecked" id="ckAll" onclick="selectAll()" />全选
					<g:checkBox name="invertChecked" id="ckInvert" onclick="selectInvert()" />反选
				</th>
				<th>${message(code: 'worktime.workcontents.label')}</th>
				<th>${message(code: 'worktime.completer.label')}</th>
				<th>${message(code: 'worktime.finishedDate.label')}</th>
				<th>${message(code: 'worktime.workWay.label')}</th>
				<th>${message(code: 'worktime.manHour.label')}</th>
				<th>${message(code: 'worktime.machineHour.label')}</th>
				<th>${message(code: 'worktime.comment2.label')}</th>
				<th>${message(code: 'operations.label')}</th>
			</tr>
		</thead>
		<tbody>
			<g:form id="form" name="myForm"  action="exportToExcel" >
				<g:each in="${worktimeInstanceList}" var="worktimeInstance">
				    <tr>
						<td>&nbsp;&nbsp;
							<small>
								<g:checkBox id="check" name="sub" value="${worktimeInstance.id}" checked="false" onclick="change()"/>
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
									<g:formatDate format="yyyy-MM-dd" date="${worktimeInstance?.finishedDate}" />
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
							<small class="wrap">
									${worktimeInstance?.comment2}
							</small>
						</td>
						<td>
							<g:if test="${flag == true ||worktimeInstance?.completers.contains(currentUser) }">
								<span>
								
										<g:link action="edit" params="[ id : worktimeInstance.id]">
											<g:message code="default.button.edit.label" />
										</g:link>
								</span>
							</g:if>
						</td>
					</tr>
				</g:each>
					
			<g:submitButton id="sub" name="submit" value="导出excel" />
			<span id="worktimeSpan">
				<g:link action="create" params="[ projectId : projectInstance.id]" >
					<g:message code="default.create.label" args="${[message(code: 'worktime.label')]}" />
				</g:link>
			</span>
			</g:form>
		</tbody>
	</table>
	<script type="text/javascript">
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
<%@ page import="com.capitalbiotech.bpms.Project" %>
<g:set var="daynum" value="${daynum}"/>
<html>
<head>
</head>
<style type="text/css">
	ul{ padding:0; list-style:none; }
</style>
<body style="background-color:F5F5F5;">
	<div class="row-fluid">
		<div class="span12 bpms-header">
			<g:message code="showduetime.label" />
		</div>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3" style="width:200px;">
				<div class="well sidebar-nav">
					<ul class="nav nav-list">
						<li>
							你的项目
							<g:form url='[controller: "project", action: "showduetime"]' id="searchableDue" name="searchableDue" method="get" style="margin-bottom:0;">
								<input type="text" id="daynum" name="daynum" style="width:30px;border:0px;text-align:center" value="${daynum}"></input>
								天内到期的有
								<input type="text" id="projectcount" name="projectcount" value="${projectCount}" style="width:20px;height:20px;border:0px;background-color:F5F5F5;text-align:center" readonly></input>
								个
							</g:form>
						</li>
						<li>
							<g:if test="${myProjectDueDateMap["UNFINISHED_DUEDATE"] != null && ""!= myProjectDueDateMap["UNFINISHED_DUEDATE"]}">
										未完成的有${myProjectDueDateMap["UNFINISHED_DUEDATE"]}个
							</g:if>
						</li>
						<li>
							<g:if test="${myProjectDueDateMap["OVER_DUEDATE"] != null && ""!= myProjectDueDateMap["OVER_DUEDATE"]}">
										过期的有${myProjectDueDateMap["OVER_DUEDATE"]}个
							</g:if>
						</li>
						<li>
							<g:if test="${myProjectDueDateMap["FINISHED_DUEDATE"] != null && ""!= myProjectDueDateMap["FINISHED_DUEDATE"]}">
										已完成的有${myProjectDueDateMap["FINISHED_DUEDATE"]}个
							</g:if>
						</li>
						<li>
							<g:form url='[controller: "project", action: "list"]' id="searchableDueProject" name="searchableDueProject" method="get" style="margin-bottom:0;">
								<g:hiddenField name="projectInstanceIdStr" value="${projectInstanceIdStr}" />
								<g:hiddenField name="projectCount" value="${projectCount}" />
								<g:hiddenField name="q" value="" />
							</g:form>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		document.getElementById("daynum").onblur=function(){
			var val = document.getElementById('daynum');
			if(!isNaN(val.value)){
				var value = parseInt(document.getElementById('daynum').value);
	            if(value > 0 && value < 31){
					document.getElementById("searchableDue").submit()
	            }else{
	                alert('请输入1-30之间的正整数 ');
					document.getElementById('daynum').value="";
	            }
			}else{
			   alert('请输入1-30之间的正整数 ');
			}
		}
		//document.getElementById("projectcount").onblur=function(){
			// 每发一个请求，应该创建一个xhr对象 
		//		var xhr = new XMLHttpRequest();
		//		xhr.onreadystatechange = function() {
					// 响应完全返回, 并且响应成功了
		//			if(xhr.readyState == 4 && xhr.status == 200) {
		//				var text = xhr.responseText;
		//				text=text.replace("[", "");
		//				text=text.replace("]", "");
		//				console.log(text);
		///				document.getElementById("projectcount").value=7;
		//			}
		//		};
		//		
		//	xhr.open("GET", "showduetime", true);//http://localhost:8080/bpms/project/findMaxId
		//	xhr.send();
		//}
	</script>
</body>
</html>		
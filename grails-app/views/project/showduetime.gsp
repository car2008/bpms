<%@ page import="com.capitalbiotech.bpms.Project" %>
<g:set var="daynum" value="${daynum}"/>
<html>
<head>
</head>
<style type="text/css">
	th,td{
	 font-family:"Times New Roman",Georgia,Serif;
	 line-height:25px;
	 font-size:15px;
}
</style>
<body style="background-color:F5F5F5;">
	<table>
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
					<g:form url='[controller: "project", action: "showduetime"]' id="searchableDue" name="searchableDue" method="get" style="margin-bottom:0;">
						<input type="text" id="daynum" name="daynum" style="width:30px;border:0px;text-align:center" value="${daynum}"></input>天内到期:
					</g:form>
				</td>
				<td>
					<input type="text" id="projectcount" name="projectcount" value="${projectCount}" style="width:20px;height:20px;border:0px;background-color:F5F5F5;text-align:center" readonly></input>个
				</td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript">
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
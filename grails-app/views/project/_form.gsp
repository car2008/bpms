<%@ page import="com.capitalbiotech.bpms.Project" %>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'contract', 'error')}">
	<label class="control-label" for="contract"><g:message code="project.contract.label" /></label>
	<div class="controls">
		<input type="text" id="contract" name="contract" ${disabledValue} value="${projectInstance?.contract}" onchange="changeInfo()" />
		<span class="help-inline"><g:message code="required.label" /></span><span id="addContractSpan" style="color:orange;text-align:center"> </span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'customerUnit', 'error')}">
	<label class="control-label" for="customerUnit"><g:message code="project.customerUnit.label" /></label>
	<div class="controls">
		<input type="text" id="customerUnit" name="customerUnit" ${disabledValue} value="${projectInstance?.customerUnit}" onchange="changeInfo()"/>
		<span class="help-inline"><g:message code="required.label" /></span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'customerName', 'error')}">
	<label class="control-label" for="customerName"><g:message code="project.customerName.label" /></label>
	<div class="controls">
		<input type="text" id="customerName" name="customerName" ${disabledValue} value="${projectInstance?.customerName}" onchange="changeInfo()"/>
		<span class="help-inline"><g:message code="required.label" /></span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'information', 'error')}">
	<label class="control-label" for="information"><g:message code="project.information.label" /></label>
	<div class="controls">
		<input type="text" size="400" id="information" name="information" readonly="readonly" value="${projectInstance?.information}" /><span class="help-inline"><g:message code="format.required.label" /></span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'projectCreateDate', 'error')}">
	<label class="control-label" for="projectCreateDate"><g:message
				code="project.projectCreateDate.label" /></label>
	<div class="controls">
		<input type="text" name="projectCreateDate" id="projectCreateDate" ${disabledValue} data-date="${projectCreateDate}" data-date-format="yyyy-mm-dd" value="${projectCreateDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'dueTime', 'error')}">
	<label class="control-label" for="dueTime"><g:message
				code="project.dueTime.label" /></label>
	<div class="controls">
		<input type="text" name="dueTime" id="dueTime" ${disabledValue} data-date="${dueTime}" data-date-format="yyyy-mm-dd" value="${dueTime}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'innerDueDate', 'error')}">
	<label class="control-label" for="innerDueDate"><g:message
				code="project.innerDueDate.label" /></label>
	<div class="controls">
		<input type="text" name="innerDueDate" id="innerDueDate" ${disabledValue} data-date="${innerDueDate}" data-date-format="yyyy-mm-dd" value="${innerDueDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'level', 'error')}">
	<label class="control-label" for="level"><g:message code="project.level.label" /></label>
	<div class="controls">
		<select id="level" name="level">
			<g:each in="${Project.constraints.level.inList}" var="level">
				<option value="${level}" ${projectInstance?.level == level ? 'selected' : ''} ${disabledValue} >
					<g:message code="project.level.${level}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'status', 'error')}">
	<label class="control-label" for="status"><g:message code="project.status.label" /></label>
	<div class="controls">
		<select id="status" name="status">
			<g:each in="${projectInstance.followingStatus}" var="status">
				<option value="${status}" ${projectInstance?.status == status ? 'selected' : ''} ${disabledValue} >
					<g:message code="project.status.${status}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'platforms', 'error')}">
	<label class="control-label" for="platforms"><g:message code="platform.label" /></label>
	<div class="controls">
		<select id="platforms" name="platforms" multiple data-placeholder=" ">
			<g:each in="${platformInstanceList}" var="platformInstance">
				<option value="${platformInstance?.id}" ${projectInstance?.platforms?.collect{it.id}?.contains(platformInstance.id) ? 'selected' : ''}  ${disabledValue}>${platformInstance?.title}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'experiments', 'error')}">
	<label class="control-label" for="experiments"><g:message code="experiment.label" /></label>
	<div class="controls">
		<select id="experiments" name="experiments" multiple data-placeholder=" ">
			<g:each in="${experimentInstanceList}" var="experimentInstance">
				<option value="${experimentInstance?.id}" ${projectInstance?.experiments?.collect{it.id}?.contains(experimentInstance.id) ? 'selected' : ''}  ${disabledValue}>${experimentInstance?.title}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'species', 'error')}">
	<label class="control-label" for="species"><g:message code="project.species.label" /></label>
	<div class="controls">
		<input type="text" name="species" id="species" ${disabledValue} value="${projectInstance?.species}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'samplesize', 'error')}">
	<label class="control-label" for="samplesize"><g:message code="project.samplesize.label" /></label>
	<div class="controls">
		<input type="text" name="samplesize" id="samplesize" ${disabledValue} value="${projectInstance?.samplesize}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'k3number', 'error')}">
	<label class="control-label" for="k3number"><g:message code="project.k3number.label" /></label>
	<div class="controls">
		<input type="text" name="k3number" id="k3number" ${disabledValue} value="${projectInstance?.k3number}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'batch', 'error')}">
	<label class="control-label" for="batch"><g:message code="project.batch.label" /></label>
	<div class="controls">
		<input type="text" name="batch" id="batch" ${disabledValue} value="${projectInstance?.batch}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'analyses', 'error')}">
	<label class="control-label" for="analyses"><g:message code="analysis.label" /></label>
	<div class="controls">
		<select id="analyses" name="analyses" multiple data-placeholder=" ">
			<g:each in="${analysisInstanceList}" var="analysisInstance">
				<option value="${analysisInstance?.id}" ${projectInstance?.analyses?.collect{it.id}?.contains(analysisInstance.id) ? 'selected' : ''}  ${disabledValue}>${analysisInstance?.title}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'salesman', 'error')}">
	<label class="control-label" for="salesman"><g:message code="project.salesman.label" /></label>
	<div class="controls">
		<input type="text" name="salesman" id="salesman" ${disabledValue} value="${projectInstance?.salesman}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'supervisors', 'error')}">
	<label class="control-label" for="supervisors"><g:message code="project.supervisors.label" /></label>
	<div class="controls">
		<select id="supervisors" name="supervisors" multiple data-placeholder=" ">
			<g:each in="${supervisorInstanceList}" var="supervisorInstance">
				<option value="${supervisorInstance?.id}" ${projectInstance?.supervisors?.collect{it.id}?.contains(supervisorInstance.id) ? 'selected' : ''}  ${disabledValue}>${supervisorInstance?.name}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'analysts', 'error')}">
	<label class="control-label" for="analysts"><g:message code="project.analysts.label" /></label>
	<div class="controls">
		<select id="analysts" name="analysts" multiple data-placeholder=" ">
			<g:each in="${analystInstanceList}" var="analystInstance">
				<option value="${analystInstance?.id}" ${projectInstance?.analysts?.collect{it.id}?.contains(analystInstance.id) ? 'selected' : ''}  ${disabledValue}>${analystInstance?.name}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'analyStartDate', 'error')}">
	<label class="control-label" for="analyStartDate"><g:message
				code="project.analyStartDate.label" /></label>
	<div class="controls">
		<input type="text" name="analyStartDate" id="analyStartDate" ${disabledValue} data-date="${analyStartDate}" data-date-format="yyyy-mm-dd" value="${analyStartDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'metaSendData', 'error')}">
	<label class="control-label" for="metaSendData"><g:message
				code="project.metaSendData.label" /></label>
	<div class="controls">
		<input type="text" name="metaSendData" id="metaSendData" ${disabledValue} data-date="${metaSendData}" data-date-format="yyyy-mm-dd" value="${metaSendData}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'metaSendWay', 'error')}">
	<label class="control-label" for="metaSendWay"><g:message code="project.metaSendWay.label" /></label>
	<div class="controls">
		<select id="metaSendWay" name="metaSendWay" >
			<g:each in="${Project.constraints.metaSendWay.inList}" var="metaSendWay">
				<option value="${metaSendWay}" ${projectInstance?.metaSendWay == metaSendWay ? 'selected' : ''} ${disabledValue}>
					<g:message code="project.way.${metaSendWay}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'libraryBuildWay', 'error')}">
	<label class="control-label" for="libraryBuildWay"><g:message code="project.libraryBuildWay.label" /></label>
	<div class="controls">
		<select id="libraryBuildWay" name="libraryBuildWay" >
			<g:each in="${Project.constraints.libraryBuildWay.inList}" var="libraryBuildWay">
				<option value="${libraryBuildWay}" ${projectInstance?.libraryBuildWay == libraryBuildWay ? 'selected' : ''} ${disabledValue}>
					<g:message code="project.way.${libraryBuildWay}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'readLength', 'error')}">
	<label class="control-label" for="readLength"><g:message code="project.readLength.label" /></label>
	<div class="controls">
		<input type="text" name="readLength" id="readLength" ${disabledValue} value="${projectInstance?.readLength}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'readsNum', 'error')}">
	<label class="control-label" for="readsNum"><g:message code="project.readsNum.label" /></label>
	<div class="controls">
		<input type="text" name="readsNum" id="readsNum" ${disabledValue} value="${projectInstance?.readsNum}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'dataSize', 'error')}">
	<label class="control-label" for="dataSize"><g:message code="project.dataSize.label" /></label>
	<div class="controls">
		<input type="text" name="dataSize" id="dataSize" ${disabledValue} value="${projectInstance?.dataSize}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'spliters', 'error')}">
	<label class="control-label" for="spliters"><g:message code="project.spliters.label" /></label>
	<div class="controls">
		<select id="spliters" name="spliters" multiple data-placeholder=" ">
			<g:each in="${spliterInstanceList}" var="spliterInstance">
				<option value="${spliterInstance?.id}" ${projectInstance?.spliters?.collect{it.id}?.contains(spliterInstance.id) ? 'selected' : ''}  ${disabledValue}>${spliterInstance?.name}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'analySendDate', 'error')}">
	<label class="control-label" for="analySendDate"><g:message
				code="project.analySendDate.label" /></label>
	<div class="controls">
		<input type="text" name="analySendDate" id="analySendDate" ${disabledValue} data-date="${analySendDate}" data-date-format="yyyy-mm-dd" value="${analySendDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'analySendWay', 'error')}">
	<label class="control-label" for="analySendWay"><g:message code="project.analySendWay.label" /></label>
	<div class="controls">
		<select id="analySendWay" name="analySendWay" >
			<g:each in="${Project.constraints.analySendWay.inList}" var="analySendWay">
				<option value="${analySendWay}" ${projectInstance?.analySendWay == analySendWay ? 'selected' : ''} ${disabledValue}>
					<g:message code="project.way.${analySendWay}.label" />
				</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'fileName', 'error')}">
	<label class="control-label" for="fileName"><g:message code="project.fileName.label" /></label>
	<div class="controls">
		<span><input type="text" name="fileRealName" id="fileRealName" readonly value="${fileRealName}" style="background-color:transparent;"/></span><span><input type="file" name="myFile" id="myFile" ${disabledValue} onchange="selectFile(this)"/></span>
		<span><g:hiddenField name="fileName" id="fileName" value="${projectInstance?.fileName}" /></span>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'manHour', 'error')}">
	<label class="control-label" for="manHour"><g:message code="project.manHour.label" /></label>
	<div class="controls">
		<input type="text" name="manHour" id="manHour" ${disabledValue} value="${projectInstance?.manHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'machineHour', 'error')}">
	<label class="control-label" for="machineHour"><g:message code="project.machineHour.label" /></label>
	<div class="controls">
		<input type="text" name="machineHour" id="machineHour" ${disabledValue} value="${projectInstance?.machineHour}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'overdueReason', 'error')}">
	<label class="control-label" for="overdueReason"><g:message code="project.overdueReason.label" /></label>
	<div class="controls">
		<input type="text" name="overdueReason" id="overdueReason" ${disabledValue} value="${projectInstance?.overdueReason}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'backupDate', 'error')}">
	<label class="control-label" for="backupDate"><g:message
				code="project.backupDate.label" /></label>
	<div class="controls">
		<input type="text" name="backupDate" id="backupDate" ${disabledValue} data-date="${backupDate}" data-date-format="yyyy-mm-dd" value="${backupDate}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'backupLocation', 'error')}">
	<label class="control-label" for="backupLocation"><g:message code="project.backupLocation.label" /></label>
	<div class="controls">
		<input type="text" name="backupLocation" id="backupLocation" ${disabledValue} value="${projectInstance?.backupLocation}" />
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'isRemoted', 'error')}">
	<label class="control-label" for="isRemoted"><g:message code="project.isRemoted.label" /></label>
	<div class="controls">
		<input type="radio" name="isRemoted" id="isRemoted" ${disabledValue} value="true"  ${projectInstance?.isRemoted == true ? 'checked' : ''} />是
		<input type="radio" name="isRemoted" id="isRemoted" ${disabledValue} value="false" ${projectInstance?.isRemoted == false ? 'checked' : ''} />否
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'comment1', 'error')}">
	<label class="control-label" for="comment1"><g:message code="project.comment1.label" /></label>
	<div class="controls">
		<textarea rows="5" cols="20" name="comment1" id="comment1" ${disabledValue} style= "resize:none;">${projectInstance?.comment1}</textarea>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'sellers', 'error')}">
	<label class="control-label" for="sellers"><g:message code="project.sellers.label" /></label>
	<div class="controls">
		<select id="sellers" name="sellers" multiple data-placeholder=" ">
			<g:each in="${sellerInstanceList}" var="sellerInstance">
				<option value="${sellerInstance?.id}" ${projectInstance?.sellers?.collect{it.id}?.contains(sellerInstance.id) ? 'selected' : ''}  ${disabledValue}>${sellerInstance?.name}</option>
			</g:each>
		</select>
	</div>
</div>
<div class="control-group ${hasErrors(bean: projectInstance, field: 'isControled', 'error')}">
	<label class="control-label" for="isControled"><g:message code="project.isControled.label" /></label>
	<div class="controls">
		<input type="radio" name="isControled" id="isControled" ${disabledValue} value="true" ${projectInstance?.isControled == true ? 'checked' : ''} />是
		<input type="radio" name="isControled" id="isControled" ${disabledValue} value="false" ${projectInstance?.isControled == false ? 'checked' : ''} />否
	</div>
</div>
<script type="text/javascript">	
	$(function(){
		$('#dueTime').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});
		
		$('#projectCreateDate').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});

		$('#innerDueDate').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});

		$('#metaSendData').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});

		$('#analyStartDate').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});

		$('#analySendDate').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});

		$('#backupDate').datepicker({
		    format:         "yyyy-mm-dd",
		    clearBtn:       true,
		    language:       "zh-CN",
		    autoclose:      true,
		    todayHighlight: true,
		});
		
		$("#level").chosen({});
		$("#status").chosen({});
		$("#platforms").chosen({});
		$("#experiments").chosen({});
		$("#workcontents").chosen({});
		$("#analyses").chosen({});
		$("#supervisors").chosen({});
		$("#analysts").chosen({});
		$("#sellers").chosen({});
		$("#metaSendWay").chosen({});
		$("#analySendWay").chosen({});
		$("#spliters").chosen({});
	});
	function changeInfo(){
		var information=document.getElementById('information');
		var informationValue=information.value;
		information.value="";
		var information=document.getElementById('information');
		var contract=document.getElementById('contract');
		var contractValue=contract.value;
		var customerUnit=document.getElementById('customerUnit');
		var customerUnitValue=customerUnit.value;
		var customerName=document.getElementById('customerName');
		var customerNameValue=customerName.value;
		var informationValue=information.value;
		if(contractValue){
			if(informationValue){
				<%--var infoarr=informationValue.split("-");
				for(var i = 0; i < infoarr.length; i++) {
					 alert(infoarr[i]);
				}
				if(!infoarr[1]=contractValue){
					var time= new Date().getTime();
					information.value=time+"-"+document.getElementById('contract').value+"-";
				}--%>
			}else{
				//information.value=document.getElementById('title').value+"_"+document.getElementById('contract').value+"_"+document.getElementById('customerUnit').value+"_"+document.getElementById('customerName').value;
				information.value=document.getElementById('contract').value+"_"+document.getElementById('customerUnit').value+"_"+document.getElementById('customerName').value;
			}
		}
	}
	document.getElementById("contract").onblur=function(){
			var tr = document.getElementById("addContractTr");
			if(tr){
				tr.parentNode.removeChild(tr);
			}
			var v = document.getElementById("contract").value;
			if(v){
	   			v  =  encodeURIComponent(v);
	   			var a="contract=" + v;
	   			//console.log(a);
	   			//var data = [];
				//data.push(a);
				//console.log(data[0]);
	   			// 每发一个请求，应该创建一个xhr对象 
		   		var xhr = new XMLHttpRequest();
		   		xhr.onreadystatechange = function() {
		   			// 响应完全返回, 并且响应成功了
		   			if(xhr.readyState == 4 && xhr.status == 200) {  
		   				var text = xhr.responseText;
		   				var t=stripscript(text);
			   			var a = "null";//定义一个变量，赋值null
			   			//console.log(a===text);//判断a是否等于null,返回true
			   			if(a===text){
					   	}else{
				   			addSaveNewData(t);
					    //document.getElementById("result").textContent = text;
						}
		   			}
		   		};
		    	xhr.open("POST", "findContract", true);//http://localhost:8080/bpms/project/
		    	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		    	xhr.send(a);
			}
		}
		function stripscript(s) {
		    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
		        var rs = "";
		    for (var i = 0; i < s.length; i++) {
		        rs = rs + s.substr(i, 1).replace(pattern, '');
		    }
		    return rs;
		}
		function addSaveNewData(data){  
		    var po = data.split(" ");
		    var tr = $("<tr></tr>").attr("bgcolor","#FFFFFF").attr("id","addContractTr");
	        tr.append($("<td></td>").html("与此合同号相同的项目编号有:"));  
		    for(var i=0;i<po.length;i++){  
		        var element = po[i];
		        //console.log(element)  
		        //tr.append($("<td></td>").html("<a href='/bpms/project/show/'"+attr+"' style='color:blue;cursor:hand' onclick=\"javascript:openW('"+attr+"')\" >'"+attr+"'&nbsp;&nbsp;"+"</a>"));   //查看链接  
		        tr.append($("<td></td>").html("<a href='show/"+element+"' style='color:blue;cursor:hand' >'"+element+"'&nbsp;&nbsp;"+"</a>"));   //查看链接  
			    // tr.append($("<td></td>").html("<g:link action='show' id='"+attr+"'>'"+attr+"'&nbsp;&nbsp;"+"</g:link>"));
		        tr.appendTo($("#addContractSpan"));                     
		    }  
		}
		function selectFile(input) {
		    var fileName = input.value;
		    if(fileName.length > 1 && fileName ) {       
		        var ldot = fileName.lastIndexOf(".");
		        var type = fileName.substring(ldot + 1);
		        if("doc"==type || "docx"==type || "pdf"==type || "tar"==type || "rar"==type || "gz"==type || "zip"==type) {
		            
		        }else{
		        	alert("请选择正确的格式上传：word、pdf或者压缩文件");
		            //清除当前所选文件
		            input.outerHTML=input.outerHTML.replace(/(value=\").+\"/i,"$1\"");
			    }       
		    }
		}
		/**判断上传文件的大小**/
		var maxsize = 10*1024*1024;//10M  
	    var errMsg = "上传的附件文件不能超过10M！！！";  
	    var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过10M，建议使用FireFox、Chrome浏览器。";  
	    var  browserCfg = {};  
	    var ua = window.navigator.userAgent;  
	    if (ua.indexOf("MSIE")>=1){  
	        browserCfg.ie = true;  
	    }else if(ua.indexOf("Firefox")>=1){  
	        browserCfg.firefox = true;  
	    }else if(ua.indexOf("Chrome")>=1){  
	        browserCfg.chrome = true;  
	    }  
	    function checkfile(){  
	        try{  
	            var obj_file = document.getElementById("myFile");  
	            if(obj_file.value==""){  
	            //    alert("请先选择上传文件");  
	            //    return;  
	            }else{
	            	var filesize = 0;  
		            if(browserCfg.firefox || browserCfg.chrome ){  
		                filesize = obj_file.files[0].size;  
		            }else{  
		                alert(tipMsg);  
		           		return false;  
		            }  
		            if(filesize==-1){  
		                alert(tipMsg);  
		                return false;  
		            }else if(filesize>maxsize){  
		                alert(errMsg); 
		                return false;  
		            }
		        }  
	              
	        }catch(e){  
	            alert(e);  
	        }  
	    } 
</script>
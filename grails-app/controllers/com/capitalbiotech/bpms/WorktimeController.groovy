package com.capitalbiotech.bpms

import grails.plugins.springsecurity.Secured

import java.text.SimpleDateFormat

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Secured(['ROLE_USER'])
class WorktimeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def springSecurityService
    
    def index = {
        redirect(action: "list", params: params)
    }
	
	def listall = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		if (!params.offset) {
			params.offset = 0
		}
		if (!params.order) {
			params.order = 'desc'
		}
		if (!params.sort) {
			params.sort = 'dateCreated'
		}

		def currentUser = springSecurityService.currentUser
		def worktimeInstanceList = Worktime.list(params)
		
		def worktimeInstanceTotal = Worktime.count()
		def allWorktimeInstanceTotal = worktimeInstanceTotal
		
		def analystRole = Role.findByAuthority("ROLE_ANALYST")
		def analystInstanceList = UserRole.findAllByRole(analystRole).user
		
		def platformInstanceList = Platform.list()
		def workcontentInstanceList = Workcontent.list()
		
		//def beginSearchDateList=Worktime.executeQuery("SELECT min(finishedDate) FROM Worktime")
		//def endSearchDateList=Worktime.executeQuery("SELECT max(finishedDate) FROM Worktime")
			
		[   worktimeInstanceList:worktimeInstanceList,
			worktimeInstanceTotal:worktimeInstanceTotal,
			analystInstanceList:analystInstanceList,
			platformInstanceList:platformInstanceList,
			workcontentInstanceList:workcontentInstanceList
			//beginSearchDate:beginSearchDateList.get(0),
			//endSearchDate:endSearchDateList.get(0),
			]
	}
	
	def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        if (!params.offset) {
            params.offset = 0
        }
        if (!params.order) {
            params.order = 'DESC'
        }
        if (!params.sort) {
            params.sort = 'dateCreated'
        }
		def currentUser = springSecurityService.currentUser
		def	projectInstance=Project.findById(params.id)
		def currentAuthoritiesString=currentUser.getAuthoritiesString()
		def flag=false
		if(currentAuthoritiesString.contains("ROLE_ADMIN")){
			flag=true
		}
		
		//flag=getFlag(projectInstance,currentUser)      //判断是否是本人的项目，否则不能创建，修改工时
		//def worktimeInstanceList=Worktime.findByProject(projectInstance)
		def worktimeInstanceList=projectInstance.worktimes
        [projectInstance:projectInstance,
		worktimeInstanceList: worktimeInstanceList,
	    offset:params.offset,
		order:params.order,
		sort:params.sort,
		flag:flag,
		currentUser:currentUser
		]
    }
	
    def save = {
		if (!Util.isEmpty(params.finishedDate)) {
			params.finishedDate = Util.parseSimpleDate(params.finishedDate)
		}
        def worktimeInstance = new Worktime(params)
		
        def projectInstance = Project.findByTitle(params.title)
        worktimeInstance.project = projectInstance
		
		if(!params.completers){
			worktimeInstance.addToCompleters(springSecurityService.currentUser)
		}
		
        if (!worktimeInstance.hasErrors() && worktimeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), ''])}"
            redirect(controller: "worktime", action: "list", id: projectInstance.id)
        }else {
            flash.error = renderErrors(bean: worktimeInstance, as: "list")
            redirect(controller: "worktime", action: "list", id: projectInstance.id, model: [worktimeInstance: worktimeInstance])
        }
    }

    def edit = {
		def workcontentInstanceList = Workcontent.list()
		def platformInstanceList = Platform.list()
		def analystRole = Role.findByAuthority("ROLE_ANALYST")
		def analystInstanceList = UserRole.findAllByRole(analystRole).user
        def worktimeInstance = Worktime.get(params.id)
        if (!worktimeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list")
        }else {
			def projectInstance=worktimeInstance.project
			def workcontentInstance=worktimeInstance.workcontents
			def platformInstance=worktimeInstance.platforms
            return [platformInstance:platformInstance,platformInstanceList:platformInstanceList,workcontentInstanceList:workcontentInstanceList,worktimeInstance: worktimeInstance,projectInstance:projectInstance,workcontentInstance:workcontentInstance,analystInstanceList:analystInstanceList]
        }
    }

    def update = {
        def worktimeInstance = Worktime.get(params.id)
		def analystRole = Role.findByAuthority("ROLE_ANALYST")
		def analystInstanceList = UserRole.findAllByRole(analystRole).user
        if (worktimeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (worktimeInstance.version > version) {
                    
                    worktimeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'message.label', default: 'Message')] as Object[], "Another user has updated this Message while you were editing")
                    render(view: "edit",model: [analystInstanceList: analystInstanceList])
                    return
                }
            }
			if (!Util.isEmpty(params.finishedDate)) {
				params.finishedDate = Util.parseSimpleDate(params.finishedDate)
			}
			worktimeInstance.workcontents.clear()
			worktimeInstance.platforms.clear()
			worktimeInstance.completers.clear()
            worktimeInstance.properties = params
			def projectInstance=worktimeInstance.project
            if (!worktimeInstance.hasErrors() && worktimeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'message.label', default: 'Message')])}"
                redirect(action: "list", id: projectInstance.id)
            }
            else {
                render(view: "edit",model: [analystInstanceList: analystInstanceList])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list")
        }
    }

	def create = {
		def projectInstance=Project.findByTitle(params.projectId)
		def worktimeInstance = new Worktime()
		worktimeInstance.properties = params
		
		def workcontentInstanceList = Workcontent.list()
		def platformInstanceList = Platform.list()
		
		def analystRole = Role.findByAuthority("ROLE_ANALYST")
		def analystInstanceList = UserRole.findAllByRole(analystRole).user
		
		return [platformInstanceList:platformInstanceList,worktimeInstance: worktimeInstance,workcontentInstanceList:workcontentInstanceList,projectInstance:projectInstance,analystInstanceList:analystInstanceList,]
		
	}

	
    def delete = {
        def worktimeInstance = Worktime.get(params.id)
        if (worktimeInstance) {
            try {
                worktimeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
                redirect(action: "list",id:params.projectInstanceId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
                redirect(action: "list",id:params.projectInstanceId)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list",id:params.projectInstanceId)
        }
    }
	
	def searchByDate={
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		if (!params.offset) {
			params.offset = 0
		}
		if (!params.order) {
			params.order = 'desc'
		}
		if (!params.sort) {
			params.sort = 'dateCreated'
		}
		
		def worktimeInstanceList = Worktime.executeQuery("SELECT worktime FROM Worktime worktime WHERE finished_date BETWEEN '"+params.beginSearchDate+"' AND '"+params.endSearchDate+"'ORDER BY worktime.${params.sort} ${params.order}",[offset: params.offset,max:params.max])
		def worktimeInstanceTotalIdList= Worktime.executeQuery("SELECT worktime.id FROM Worktime worktime WHERE finished_date BETWEEN '"+params.beginSearchDate+"' AND '"+params.endSearchDate+"'ORDER BY worktime.${params.sort} ${params.order}")
		def worktimeInstanceTotal=worktimeInstanceTotalIdList.size()
		
		render view: 'listall', model: [worktimeInstanceList:worktimeInstanceList,
			worktimeInstanceTotal:worktimeInstanceTotal,
			beginSearchDate:params.beginSearchDate,
			endSearchDate:params.endSearchDate,
			worktimeInstanceTotalIdList:worktimeInstanceTotalIdList,
			]
	}
	
	def searchWorktimeByColumn={
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		if (!params.offset) {
			params.offset = 0
		}
		if (!params.order) {
			params.order = 'desc'
		}
		if (!params.sort) {
			params.sort = 'dateCreated'
		}
		def beginSearchDate=params.beginSearchDate
		def endSearchDate=params.endSearchDate
		def q2 = params.q2?.trim()
		def q4 = params.q4?.trim()
		def q5 = params.q5?.trim()
		def q6 = params.q6?.trim()
		def q7 = params.q7?.trim()
		def stringBuf=new StringBuffer()
		
		def worktimeInstanceList=new ArrayList()
		def worktimeInstanceTotalList=new ArrayList()
		def worktimeInstanceTotalIdList=new ArrayList()
		def worktimeInstanceTotal
		Map paramMap1=new HashMap()
		
		stringBuf.append("SELECT DISTINCT worktime FROM Worktime worktime ")
		if(q4){
			stringBuf.append("LEFT JOIN worktime.platforms platform ")
		}
		if(q5){
			stringBuf.append("LEFT JOIN worktime.workcontents workcontent ")
		}
		if(q6){
			stringBuf.append("LEFT JOIN worktime.completers completer ")
		}
		if(beginSearchDate && endSearchDate||(q2||q4||q5||q6||q7)){
			stringBuf.append("WHERE ")
		}
		if(beginSearchDate && endSearchDate){
			stringBuf.append("finished_date BETWEEN '"+beginSearchDate+"' AND '"+endSearchDate+"' ")
		}
		stringBuf.append(q2?"AND contract like '%"+q2+"%' ":"")
		if(q4){
			def platform=Platform.findByCode(q4)
			stringBuf.append("AND platform = :platform ")
			paramMap1.put("platform", platform)
		}
		if(q5){
			def workcontent=Workcontent.findByCode(q5)
			stringBuf.append("AND workcontent = :workcontent ")
			paramMap1.put("workcontent", workcontent)
		}
		if(q6){
			def completer=User.findByUsername(q6)
			stringBuf.append("AND completer = :completer ")
			paramMap1.put("completer", completer)
		}
		stringBuf.append(q7?"AND work_way like '%"+q7+"%' ":"")
		
		def s1=stringBuf.toString()
		if(s1.contains("WHERE AND")){
			s1=s1.replaceFirst("AND","")
		}
		if(s1.endsWith("WHERE ")){
			s1=s1.replaceFirst("WHERE","")
		}
		//println s1
		stringBuf.append("ORDER BY worktime.${params.sort} ${params.order} ")
		def s2=stringBuf.toString()
		if(s2.contains("WHERE AND")){
			s2=s2.replaceFirst("AND","")
		}
		//println s2
		if(paramMap1){
			worktimeInstanceTotalList = Worktime.executeQuery(s1,paramMap1)
			//println worktimeInstanceTotalList
			paramMap1.put("offset",params.offset)
			paramMap1.put("max",params.max)
			worktimeInstanceList = Worktime.executeQuery(s2,paramMap1)
		}else{
			worktimeInstanceTotalList = Worktime.executeQuery(s1)
			paramMap1.put("offset",params.offset)
			paramMap1.put("max",params.max)
			worktimeInstanceList = Worktime.executeQuery(s2,paramMap1)
		}
		worktimeInstanceTotalList.each{ worktimeInstance ->
			worktimeInstanceTotalIdList.add(worktimeInstance.id)
		}
		//def worktimeInstanceList = Worktime.executeQuery("SELECT worktime FROM Worktime worktime WHERE finished_date BETWEEN '"+params.beginSearchDate+"' AND '"+params.endSearchDate+"'ORDER BY worktime.${params.sort} ${params.order}",[offset: params.offset,max:params.max])
		//def worktimeInstanceTotalIdList= Worktime.executeQuery("SELECT worktime.id FROM Worktime worktime WHERE finished_date BETWEEN '"+params.beginSearchDate+"' AND '"+params.endSearchDate+"'ORDER BY worktime.${params.sort} ${params.order}")
		worktimeInstanceTotal=worktimeInstanceTotalList.size()
		
		def analystRole = Role.findByAuthority("ROLE_ANALYST")
		def analystInstanceList = UserRole.findAllByRole(analystRole).user
		
		def platformInstanceList = Platform.list()
		def workcontentInstanceList = Workcontent.list()
		
		render view: 'listall', model: [worktimeInstanceList:worktimeInstanceList,
			worktimeInstanceTotal:worktimeInstanceTotal,
			worktimeInstanceTotalIdList:worktimeInstanceTotalIdList,
			q2:params.q2,
			q3:params.q3,
			q4:params.q4,
			q5:params.q5,
			q6:params.q6,
			q7:params.q7,
			beginSearchDate:beginSearchDate,
			endSearchDate:endSearchDate,
			analystInstanceList:analystInstanceList,
			platformInstanceList:platformInstanceList,
			workcontentInstanceList:workcontentInstanceList
			]
	}
	
	def exportToExcel(){
		session.loadOver=false
		response.setContentType("text/csv");
		response.setContentType("application/csv;charset=gbk");
		response.setHeader("Content-Disposition", "attachment;FileName=test.csv");
		OutputStream out = null;
		try {
			//out = new FileOutputStream(new File("d:\\text.csv"));
			out=response.getOutputStream();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer head = new StringBuffer();
		//head.append("序号,版本,合同,创建日期,截止时间,最后处理日期,级别,状态,标题,优先级,状态,到期时间,实验,技术支持,数据分析员,销售")
		head.append("合同号,样本数,批次,检测平台,工作内容,完成人员,日期,工作方式,工时(h),机时(h),备注")
		head.append("\n");
		out.write(head.toString().getBytes("gbk"));
		out.flush();
		def buf=new StringBuffer();
		def worktimeIdList
		def flag=false
		def projectCount=0
		def samplesizeCount=0
		def manHourCount=0
		def machineHourCount=0
		
		if(params.exportAll){                              //判断是否是导出全部工时，若是则worktimeIdList为全部项目id
			worktimeIdList=Worktime.executeQuery("SELECT id FROM Worktime worktime")
		}else if(params.worktimeInstanceTotalIdList){
			worktimeIdList=params.worktimeInstanceTotalIdList
			flag = worktimeIdList instanceof String     //判断是否只选择了一行，要不然会把string分解
		}else{
			worktimeIdList=params.sub
			flag = worktimeIdList instanceof String     //判断是否只选择了一行，要不然会把string分解
		}
		if(flag){
			projectCount=1
			String str = worktimeIdList
			def strArr=getWorktimeList(str)
			buf.append(strArr[0]);
			buf.append("总项目数:,"+projectCount+",总样本数:,"+strArr[1]+",总工时:,"+strArr[2]+"h,"+"总机时:,"+strArr[3]+"h")
		}else{
			Iterator iter = worktimeIdList.iterator()
			while(iter.hasNext()){
				projectCount+=1
				String str = (String) iter.next()
				def strArr=getWorktimeList(str)
				buf.append(strArr[0])
				samplesizeCount=samplesizeCount+Integer.valueOf(strArr[1]).intValue()
				manHourCount=manHourCount+Integer.valueOf(strArr[2]).intValue()
				machineHourCount=machineHourCount+Integer.valueOf(strArr[3]).intValue()
			}
			buf.append("总项目数:,"+projectCount+",总样本数:,"+samplesizeCount+",总工时:,"+manHourCount+"h,"+"总机时:,"+machineHourCount+"h")
		}
		
		
		out.write(buf.toString().getBytes("gbk"));
		out.flush();
		out.close();
		session.loadOver=true;
	}
	//对分解后的id的list进行处理打印
	public String[] getWorktimeList(String str){
		def strArr=new String[4]
		def project
		def worktimeInstance
		def workWayMessage
		def finishedDate=""
		def finishedDateStr=""
		def workWay =""
		def samplesizeSum=0
		def manHourSum=0
		def machineHourSum=0
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd")
		
		def worktimeId=Integer.valueOf(str).intValue()
		worktimeInstance=Worktime.findById(worktimeId)
		project=worktimeInstance.project
		
		if(worktimeInstance.finishedDate){
			finishedDate=worktimeInstance.finishedDate.getTime()
			finishedDateStr="'"+format.format(new Date(finishedDate))
		}
		
		workWay = "worktime.way."+worktimeInstance.workWay+".label"
		workWayMessage = "${message(code: workWay)}"
		
		
		/*def dueTime=project.dueTime.getTime()
		def dateCreated=project.dateCreated.getTime()
		def lastUpdated=project.lastUpdated.getTime()
		def analyStartDate=project.analyStartDate.getTime()
		def analySendDate=project.analySendDate.getTime()
		def innerDueDate=project.innerDueDate.getTime()
		def backupDate=project.backupDate.getTime()
		def metaSendData=project.metaSendData.getTime()
		
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		
		def dueTimeStr="'"+format.format(new Date(dueTime))
		def dateCreatedStr="'"+format.format(new Date(dateCreated))
		def lastUpdatedStr="'"+format.format(new Date(lastUpdated))
		def analyStartDateStr="'"+format.format(new Date(analyStartDate))
		def analySendDateStr="'"+format.format(new Date(analySendDate))
		def innerDueDateStr="'"+format.format(new Date(innerDueDate))
		def backupDateStr="'"+format.format(new Date(backupDate))
		def metaSendDataStr="'"+format.format(new Date(metaSendData))*/
		
		def dueTimeStr=""
		if(project.dueTime){
			dueTimeStr="'"+format.format(new Date(project.dueTime.getTime()))
		}
		def dateCreatedStr=""
		if(project.dateCreated){
			dateCreatedStr="'"+format.format(new Date(project.dateCreated.getTime()))
		}
		def lastUpdatedStr=""
		if(project.lastUpdated){
			lastUpdatedStr="'"+format.format(new Date(project.lastUpdated.getTime()))
		}
		def analyStartDateStr=""
		if(project.analyStartDate){
			analyStartDateStr="'"+format.format(new Date(project.analyStartDate.getTime()))
		}
		def analySendDateStr=""
		if(project.analySendDate){
			analySendDateStr="'"+format.format(new Date(project.analySendDate.getTime()))
		}
		def innerDueDateStr=""
		if(project.innerDueDate){
			innerDueDateStr="'"+format.format(new Date(project.innerDueDate.getTime()))
		}
		def backupDateStr=""
		if(project.backupDate){
			backupDateStr="'"+format.format(new Date(project.backupDate.getTime()))
		}
		def metaSendDataStr=""
		if(project.metaSendData){
			metaSendDataStr="'"+format.format(new Date(project.metaSendData.getTime()))
		}
		
		
		def bf=new StringBuffer();
		bf.append("\"")
		bf.append(project.contract?project.contract+"\"":""+"\"")
		bf.append(project.samplesize?",\""+project.samplesize+"\"":",\""+""+"\"")
		bf.append(project.batch?",\""+project.batch+"\"":",\""+""+"\"")
		
		/*Iterator iterExperiment = project.platforms.iterator()
		bf.append(",\"")
		def index=0
		while(iterExperiment.hasNext()){
			def strExperiment = (Platform) iterExperiment.next()
			if(index>0){
			   bf.append("/"+strExperiment.title)
			}else{
			   index++
				bf.append(strExperiment.title)
			}
		}
		bf.append("\"")*/
	
		bf.append(",\"")
		if(project.platforms){
			Iterator iterExperiment = project.platforms.iterator()
			def search=0
			while(iterExperiment.hasNext()){
				def strExperiment = (Platform) iterExperiment.next()
				if(search>0){
				   bf.append("/"+strExperiment.title)
				}else{
				   search++
					bf.append(strExperiment.title)
				}
			}
		}
		bf.append("\"")
		
		bf.append(",\"")
		if(worktimeInstance.workcontents){
			Iterator iterExperiment = worktimeInstance.workcontents.iterator()
			def search=0
			while(iterExperiment.hasNext()){
				def strExperiment = (Workcontent) iterExperiment.next()
				if(search>0){
				   bf.append("/"+strExperiment.title)
				}else{
				   search++
					bf.append(strExperiment.title)
				}
			}
		}
		bf.append("\"")
		
		bf.append(",\"")
		if(worktimeInstance.completers){
			Iterator iterSeller = worktimeInstance.completers.iterator()
			def index3=0
			while(iterSeller.hasNext()){
				def strExperiment = (User) iterSeller.next()
				if(index3>0){
				   bf.append("/"+strExperiment.name)
				}else{
				   index3++
					bf.append(strExperiment.name)
				}
			}
		}
		bf.append("\"")
			
		bf.append(",\""+finishedDateStr+"\"")
		bf.append(",\""+workWayMessage+"\"")
		bf.append(worktimeInstance.manHour?",\""+worktimeInstance.manHour+"\"":",\""+""+"\"")
		bf.append(worktimeInstance.machineHour?",\""+worktimeInstance.machineHour+"\"":",\""+""+"\"")
		bf.append(worktimeInstance.comment2?",\""+worktimeInstance.comment2+"\"":",\""+""+"\"")
		samplesizeSum=samplesizeSum+(project.samplesize==null || project.samplesize==""? 0:project.samplesize)
		manHourSum=manHourSum+(worktimeInstance.manHour==null || worktimeInstance.manHour==""? 0:worktimeInstance.manHour)
		machineHourSum=machineHourSum+(worktimeInstance.machineHour==null || worktimeInstance.machineHour==""? 0:worktimeInstance.machineHour)
		bf.append("\n");
		
		strArr[0]=bf.toString()
		def a=new BigDecimal(samplesizeSum);
		int b=a.intValue();
		strArr[1]=b+""
		
		def a1=new BigDecimal(manHourSum);
		int b1=a1.intValue();
		strArr[2]=b1+""
		
		def a2=new BigDecimal(machineHourSum);
		int b2=a2.intValue();
		strArr[3]=b2+""
	    return strArr
	}
}

package com.capitalbiotech.bpms

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import java.text.SimpleDateFormat
import java.util.Set;

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import com.capitalbiotech.bpms.SendEmailAsynchronously

@Secured(['ROLE_USER'])
class ProjectController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def springSecurityService

	def index = {
		redirect(action: "list", params: params)
	}
	
    def listall = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        
		if (!params.order) {
            params.order = 'desc'
        }
        if (!params.sort) {
            params.sort = 'dateCreated'
        }

        def currentUser = springSecurityService.currentUser
        def projectInstanceList = Project.list(params)

        def noticeInstanceMap = [:]
        def noticeInstanceList = Notice.findAllByUserAndProjectInList(springSecurityService.currentUser, projectInstanceList)
        noticeInstanceList?.each { noticeInstance ->
            noticeInstanceMap[noticeInstance.project.id] = noticeInstance.unread
        }

        def projectInstanceTotal = Project.count()
        def allProjectInstanceTotal = projectInstanceTotal
        def myProjectInstanceTotal = Project.executeQuery("SELECT COUNT(DISTINCT project), project FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user",
            [user: currentUser])[0][0]
		
		def remaindingDayMap = [:]
		def projectItemList=params.projectItemList
		def offset=params.offset
		def order=params.order
		def sort=params.sort
		def itemNum='0'
		if(!params.searchProjectInstanceTotal){
			params.searchProjectInstanceTotal='0'
		}

        render view: 'list', model: [projectInstanceList: projectInstanceList,
            projectInstanceTotal: projectInstanceTotal,
            myProjectInstanceTotal: myProjectInstanceTotal,
            allProjectInstanceTotal: allProjectInstanceTotal,
            noticeInstanceMap: noticeInstanceMap,
			projectItemList:projectItemList,
			offset:offset,
			order:order,
			sort:sort,
			itemNum:itemNum,
			searchProjectInstanceTotal:params.searchProjectInstanceTotal,
			remaindingDayMap:remaindingDayMap
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
		if(!params.daynum){
			params.daynum = "5"
		}

        def currentUser = springSecurityService.currentUser
        def projectInstanceList = Project.executeQuery("SELECT DISTINCT(project) FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user ORDER BY project.${params.sort} ${params.order}",
            [user: currentUser], [offset: params.offset, max:params.max])

        def noticeInstanceMap = [:]
        def noticeInstanceList = Notice.findAllByUserAndProjectInList(springSecurityService.currentUser, projectInstanceList)
        noticeInstanceList?.each { noticeInstance ->
            noticeInstanceMap[noticeInstance.project.id] = noticeInstance.unread
        }
		
        def projectInstanceTotal = Project.executeQuery("SELECT COUNT(DISTINCT project), project FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user",
            [user: currentUser])[0][0]
        def myProjectInstanceTotal = projectInstanceTotal
        def allProjectInstanceTotal = Project.count()
		
		def remaindingDayMap = [:]
		projectInstanceList?.each { projectInstance ->
			if(!projectInstance.innerDueDate || "".equals(projectInstance.innerDueDate) ){
				remaindingDayMap[projectInstance.id] = ""
			}else{
				def innerDueDate=projectInstance.innerDueDate
				println innerDueDate
				def currentDate=Util.parseSimpleDate(Util.getCurrentDateString())
				def remaindingDay=(innerDueDate.getTime()-currentDate.getTime())/86400000
				if(remaindingDay<0){
					
				}else{
					remaindingDayMap[projectInstance.id] = remaindingDay
				}
			}
		}
		
		def projectItemList=params.projectItemList
		def offset=params.offset
		def order=params.order
		def sort=params.sort
		def itemNum='0'
		if(params.itemNum){
			itemNum=params.itemNum
		}
		if(!params.searchProjectInstanceTotal){
			params.searchProjectInstanceTotal='0'
		}

        [projectInstanceList: projectInstanceList,
            projectInstanceTotal: projectInstanceTotal,
            myProjectInstanceTotal: myProjectInstanceTotal,
            allProjectInstanceTotal: allProjectInstanceTotal,
            noticeInstanceMap: noticeInstanceMap,
			projectItemList:projectItemList,
			offset:offset,
			order:order,
			sort:sort,
			itemNum:itemNum,
			searchProjectInstanceTotal:params.searchProjectInstanceTotal,
			remaindingDayMap:remaindingDayMap
			]
    }
	
	def searchProject={
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
		if(!params.itemNum){
			params.itemNum='0'
		}
		def q = params.q?.trim()
		if (!params.q ||q=="" || q.contains("k3")) {
			redirect(action: params.lastAction, params: [itemNum:params.itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:q,parseException: true])
		}else{
			try {
				def projectInstanceList
				def projectInstanceTotal
				//if(!params.projectInstanceTotal){
					projectInstanceList = Project.executeQuery("SELECT project FROM Project project WHERE contract like '%"+q+"%' OR customerUnit like '%"+q+"%' OR customerName like '%"+q+"%' OR species like '%"+q+"%' OR k3number like '%"+q+"%' OR information like '%"+q+"%' OR salesman like '%"+q+"%'"+"ORDER BY project.${params.sort} ${params.order}",
						[offset: params.offset, max:params.max])
					def projectInstanceTotalList = Project.executeQuery("SELECT project FROM Project project WHERE contract like '%"+q+"%' OR customerUnit like '%"+q+"%' OR customerName like '%"+q+"%' OR species like '%"+q+"%' OR k3number like '%"+q+"%' OR information like '%"+q+"%' OR salesman like '%"+q+"%'"+"ORDER BY project.${params.sort} ${params.order}")
					projectInstanceTotal = projectInstanceTotalList.size()
				/*}else{
					projectInstanceTotal = params.projectInstanceTotal
					def projectInstanceIdStr = params.projectInstanceIdStr
					if(projectInstanceIdStr.contains("_")){
						def idList=projectInstanceIdStr.split("_")
						idList?.each { projectId->
							def pId=Integer.parseInt(projectId)
							def projectInstance=Project.findById(pId)
							projectInstanceList.add(projectInstance)
						}
					}else{
						def pId=Integer.parseInt(projectInstanceIdStr)
						def projectInstance=Project.findById(pId)
						projectInstanceList.add(projectInstance)
					}
				}*/
				
				if(!projectInstanceList ){
					redirect(action: params.lastAction, params: [itemNum:params.itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:q,searchResult: false,parseException: false])
				}else{
					def noticeInstanceMap = [:]
					def noticeInstanceList = Notice.findAllByUserAndProjectInList(springSecurityService.currentUser, projectInstanceList)
					noticeInstanceList?.each { noticeInstance ->
						noticeInstanceMap[noticeInstance.project.id] = noticeInstance.unread
					}
					
					def currentUser = springSecurityService.currentUser
					
					def remaindingDayMap = [:]
					projectInstanceList?.each { projectInstance ->
						if(!projectInstance.innerDueDate || "".equals(projectInstance.innerDueDate) ){
							remaindingDayMap[projectInstance.id] = ""
						}else{
							def innerDueDate=projectInstance.innerDueDate
							println innerDueDate
							def currentDate=Util.parseSimpleDate(Util.getCurrentDateString())
							def remaindingDay=(innerDueDate.getTime()-currentDate.getTime())/86400000
							if(remaindingDay<0){
								
							}else{
								remaindingDayMap[projectInstance.id] = remaindingDay
							}
						}
					}
					
					def searchProjectInstanceTotal=projectInstanceTotal
					def myProjectInstanceTotal = Project.executeQuery("SELECT COUNT(DISTINCT project), project FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user",
					[user: currentUser])[0][0]
					def allProjectInstanceTotal = Project.count()
					def projectItemList=params.projectItemList
					def offset=params.offset
					def order=params.order
					def sort=params.sort
					def itemNum='0'
					if(params.itemNum){
						itemNum=params.itemNum
					}
					
					render view: 'list', model: [projectInstanceList: projectInstanceList,
						projectInstanceTotal: projectInstanceTotal,
						myProjectInstanceTotal: myProjectInstanceTotal,
						allProjectInstanceTotal: allProjectInstanceTotal,
						noticeInstanceMap: noticeInstanceMap,
						projectItemList:projectItemList,
						offset:offset,
						order:order,
						sort:sort,
						itemNum:itemNum,
						searchResult: true,
						searchProjectInstanceTotal:searchProjectInstanceTotal,
						remaindingDayMap:remaindingDayMap
						]
				}
				
			} catch (Exception ex) {
				redirect(action: params.lastAction, params: [itemNum:params.itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:params.q,parseException: true])
			}
		}
	}
	
	def showduetime={
		if(!params.daynum){
			params.daynum = "5"
		}
		def daynum = Integer.parseInt(params.daynum)
		try {
			def currentUser = springSecurityService.currentUser
			def currentTime=Util.getCurrentDateString()
			def searchTime1=System.currentTimeMillis()+daynum*24*60*60*1000
			def searchTime=Util.dateParser1.format(new Date(searchTime1))
			def projectCount=0
			def projectInstanceList=new ArrayList<Project>()
			def projectInstanceIdStr=new StringBuffer()
			println currentTime+"--"+searchTime
			/*def projectInstanceList1 = Project.executeQuery("SELECT project FROM Project project  WHERE inner_due_date BETWEEN :currentTime AND :searchTime",
				[currentTime:currentTime,searchTime:searchTime])
			println projectInstanceList1*/
			
			def myProjectInstanceList = Project.executeQuery("SELECT project FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user ",
				[user: currentUser])
			def myProjectDueDateMap=new HashMap<String,Integer>()
			myProjectDueDateMap.put("UNFINISHED_DUEDATE", 0)
			myProjectDueDateMap.put("OVER_DUEDATE", 0)
			myProjectDueDateMap.put("FINISHED_DUEDATE", 0)
			//def a=myProjectDueDateMap.get("UNFINISHED_DUEDATE")
			//println a
			//def b=myProjectDueDateMap.get("OVER_DUEDATE")
			//def c=myProjectDueDateMap.get("FINISHED_DUEDATE")
			myProjectInstanceList?.each { projectInstance ->
				if(projectInstance.innerDueDate && projectInstance.analySendDate && (projectInstance.analySendDate.getTime() > projectInstance.innerDueDate.getTime()) ){
					myProjectDueDateMap["OVER_DUEDATE"]+=1
				}else if(projectInstance.innerDueDate && !projectInstance.analySendDate && (System.currentTimeMillis() > projectInstance.innerDueDate.getTime()) ){
					myProjectDueDateMap["OVER_DUEDATE"]+=1
				}else if(projectInstance.analySendDate==null){
					myProjectDueDateMap["UNFINISHED_DUEDATE"]+=1
				}else if(projectInstance.analySendDate){
					myProjectDueDateMap["FINISHED_DUEDATE"]+=1
				}
				if(projectInstance.innerDueDate){
					def innerduetime=projectInstance.innerDueDate.getTime()
					def curtime=Util.parseSimpleDate(currentTime)
					def seatime=Util.parseSimpleDate(searchTime)
					if(innerduetime>=curtime.getTime() && innerduetime<=seatime.getTime()){
						projectCount=projectCount+1
						projectInstanceList.add(projectInstance)
					}
				}
			}
			/*if(a>0){
				myProjectDueDateMap.remove("UNFINISHED_DUEDATE")
				myProjectDueDateMap.put("UNFINISHED_DUEDATE", a)
			}if(b>0){
				myProjectDueDateMap.remove("OVER_DUEDATE")
				myProjectDueDateMap.put("OVER_DUEDATE", b)
			}
			if(c>0){
				myProjectDueDateMap.remove("FINISHED_DUEDATE")
				myProjectDueDateMap.put("FINISHED_DUEDATE", c)
			}*/
			//println myProjectDueDateMap["UNFINISHED_DUEDATE"]
			/*projectInstanceList1?.each { projectInstance ->
				def mailList = getUserMailLists(projectInstance,currentUser)
				if(mailList.contains(currentUser.email)){//是否含有当前用户
					if(projectCount==0){
						projectInstanceIdStr.append(projectInstance.id)
					}else{
						projectInstanceIdStr.append("_").append(projectInstance.id)
					}
					projectCount=projectCount+1
					projectInstanceList.add(projectInstance)
				}
			}*/
			if(!projectInstanceList){
				//render projectInstanceTotal
				return [daynum:daynum,projectCount:projectCount,myProjectDueDateMap:myProjectDueDateMap]
				//redirect(action: "list", params: [itemNum:params.itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,daynum:daynum,projectCount:projectCount])
			}else{
				//render projectCount
				return [projectInstanceList: projectInstanceList,
					searchResult: true,
					projectCount:projectCount,
					daynum:daynum,
					projectInstanceIdStr:projectInstanceIdStr.toString(),
					myProjectDueDateMap:myProjectDueDateMap
					]
			}
		} catch (Exception ex) {
		    println 789
			//redirect(action: params.lastAction, params: [itemNum:params.itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:params.q,parseException: true])
		}
	}
	
    def create = {
        def projectInstance = new Project()
        projectInstance.properties = params

        return [projectInstance: projectInstance] + projectResource()
    }

    def save = {
		if (!Util.isEmpty(params.projectCreateDate)) {
			params.projectCreateDate = Util.parseSimpleDate(params.projectCreateDate)
		}
		
        if (!Util.isEmpty(params.dueTime)) {
            params.dueTime = Util.parseSimpleDate(params.dueTime)
        }
		
		if (!Util.isEmpty(params.innerDueDate)) {
			params.innerDueDate = Util.parseSimpleDate(params.innerDueDate)
		}
		
		if (!Util.isEmpty(params.metaSendData)) {
			params.metaSendData = Util.parseSimpleDate(params.metaSendData)
		}

		if (!Util.isEmpty(params.analyStartDate)) {
			params.analyStartDate = Util.parseSimpleDate(params.analyStartDate)
		}
		
		if (!Util.isEmpty(params.analySendDate)) {
			params.analySendDate = Util.parseSimpleDate(params.analySendDate)
		}

		if (!Util.isEmpty(params.backupDate)) {
			params.backupDate = Util.parseSimpleDate(params.backupDate)
		}
		//在保存数据时才写入title(项目编号)，与id一致，同时把title拼接到information前
		def maxId = Project.executeQuery("SELECT max(id) FROM Project project")
		def maxIdString=maxId.toString().replace("[","")
		maxIdString=maxIdString.replace("]","")
		params.title=Integer.valueOf(maxIdString).value+1
		params.information=params.title+"_"+params.information
		
        def projectInstance = new Project(params)
		def currentUser = springSecurityService.currentUser
		
		def f = request.getFile('myFile')
		if(!f.empty && f.size>1024*1024*10){
			flash.message = "上传文件太大，不能超过10M"
			render(view: "create", model: [projectInstance: projectInstance] + projectResource())
			
		}else{
			String fileUrl=upload()               //调用上传的方法，返回一个储存文件的路径。
			projectInstance.fileName=fileUrl      //存储文件路径
			if (!projectInstance.hasErrors() && projectInstance.save(flush: true)) {
				def mailList = getUserMailLists(projectInstance,currentUser)
				if(mailList.contains(currentUser.email)){//去除当前用户
					mailList.remove(currentUser.email)
				}
				def userEmailAndRole=getUserMailAndRoleMaps(projectInstance,currentUser)
				def nameList = new ArrayList<String>()
				mailList?.each { mail ->
					def toUser=User.findByEmail(mail)
					nameList.add(toUser.name)
					def sendMail=new SendEmailAsynchronously(mail,Util.getCurrentTimeString()+"提醒-->新建项目："+projectInstance.title,"你好，<b>"+toUser.name+"</b>，<br/>&nbsp;&nbsp;&nbsp;&nbsp;刚刚<b>"+currentUser.name+"</b>新建了一个项目，项目信息为：<b>"+projectInstance.information+"</b> ；你是该项目的<b>"+userEmailAndRole.get(mail).toString()+"</b>，请及时处理： <a href=http://192.168.2.72:8080/bpms/project/show/"+projectInstance.title+">去处理</a>")
					sendMail.sendMail()
				}
				if(nameList){
					def sendMail=new SendEmailAsynchronously(currentUser.email,Util.getCurrentTimeString()+"提醒-->新建项目："+projectInstance.title,"你好，<b>"+currentUser.name+"</b>，<br/>&nbsp;&nbsp;&nbsp;&nbsp;刚刚你新建了一个项目，项目信息为：<b>"+projectInstance.information+"</b> ；并发送邮件提醒了<b>"+nameList.toString()+"</b>：<a href=http://192.168.2.72:8080/bpms/project/show/"+projectInstance.title+">去查看</a>")
					sendMail.sendMail()
				}
				def track = new Track(project: projectInstance, operator: currentUser, status: projectInstance.status)
				track.save(flush: true)
	
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), ''])}"
				redirect(action: "show", id: projectInstance.id)
			}
			else {
				render(view: "create", model: [projectInstance: projectInstance] + projectResource())
			}
		}
    }
	
    def show = {
        def projectInstance = Project.get(params.id)
		def currentUser = springSecurityService.currentUser
		/*用于判断是否是本人的项目，有无修改权限，可是要进行数据库查询，效率低，改用worktimecontroller里的静态方法getFlag()
		 * def myProjectInstanceList = Project.executeQuery("SELECT DISTINCT(project) FROM Project project LEFT JOIN project.supervisors supervisor LEFT JOIN project.analysts analyst LEFT JOIN project.sellers seller LEFT JOIN project.spliters spliter WHERE supervisor = :user OR analyst = :user OR seller = :user OR spliter = :user",
			[user: currentUser])
		   def myUserRoleList = UserRole.executeQuery("SELECT DISTINCT(userrole) FROM UserRole userrole WHERE userrole.user = :user ",[user: currentUser])
		*/
		def flag=false
		
        if (!projectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }else{
			flag=getFlag(projectInstance,currentUser)   //调用getFlag静态方法进行判断是否是本人的项目或者是否为管理员
            def messageInstanceList = projectInstance.messages
            def trackInstanceList = projectInstance.tracks
			def worktimeInstanceList=projectInstance.worktimes
			
			def fileName=projectInstance.fileName
			def fileRealName
			if(fileName){
				fileRealName= fileName.substring(fileName.indexOf("_")+1);
			}

            def noticeInstance = Notice.findByProjectAndUser(projectInstance, springSecurityService.currentUser)
            if (noticeInstance) {
                noticeInstance.unread = 0
                noticeInstance.save(flush: true)
            }

            [projectInstance: projectInstance, 
				messageInstanceList: messageInstanceList, 
				trackInstanceList: trackInstanceList,
				worktimeInstanceList:worktimeInstanceList,
				fileRealName:fileRealName,
				flag:flag]
        }
    }

    def edit = {
        def projectInstance = Project.get(params.id)
		
		def currentUser = springSecurityService.currentUser
		/*def myUserRoleList = UserRole.executeQuery("SELECT DISTINCT(userrole) FROM UserRole userrole WHERE userrole.user = :user ",[user: currentUser])*/
		def flag=false
		flag=getFlag(projectInstance,currentUser)
		
		def fileName=projectInstance.fileName
		def fileRealName
		if(fileName){
			fileRealName= fileName.substring(fileName.indexOf("_")+1);
		}
		
        if (!projectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [projectInstance: projectInstance,fileRealName:fileRealName,flag:flag] + projectResource()
        }
    }
	
	def findContract = {
		def projectTitle = Project.executeQuery("SELECT title FROM Project project WHERE contract ='"+params.contract+"'")
		if (!projectTitle) {
			render "null"
		}
		else{
			render projectTitle
		}
	}
	
	def findMaxId = {
		def maxId = Project.executeQuery("SELECT max(id) FROM Project project")
		if (!maxId) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
			redirect(action: "list")
		}
		else{
			render maxId
		}
	}
	
    def update = {
        def projectInstance = Project.get(params.id)
		def currentUser = springSecurityService.currentUser
        if (projectInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (projectInstance.version > version) {
                    projectInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [
                        message(code: 'project.label', default: 'Project')] as Object[], "Another user has updated this Project while you were editing")
                    render(view: "edit", model: [projectInstance: projectInstance])
                    return
                }
            }
			if (!Util.isEmpty(params.projectCreateDate)) {
				params.projectCreateDate = Util.parseSimpleDate(params.projectCreateDate)
			}
            if (!Util.isEmpty(params.dueTime)) {
                params.dueTime = Util.parseSimpleDate(params.dueTime)
            }
			if (!Util.isEmpty(params.metaSendData)) {
				params.metaSendData = Util.parseSimpleDate(params.metaSendData)
			}
			if (!Util.isEmpty(params.analyStartDate)) {
				params.analyStartDate = Util.parseSimpleDate(params.analyStartDate)
			}
			if (!Util.isEmpty(params.analySendDate)) {
				params.analySendDate = Util.parseSimpleDate(params.analySendDate)
			}
			if (!Util.isEmpty(params.innerDueDate)) {
				params.innerDueDate = Util.parseSimpleDate(params.innerDueDate)
			}
			if (!Util.isEmpty(params.backupDate)) {
				params.backupDate = Util.parseSimpleDate(params.backupDate)
			}
			def mailListbefor = getUserMailLists(projectInstance,currentUser)
			if(mailListbefor.contains(currentUser.email)){//去除当前用户
				mailListbefor.remove(currentUser.email)
			}
			def userEmailAndRolebefor = getUserMailAndRoleMaps(projectInstance,currentUser)
            projectInstance.analyses.clear()
            projectInstance.experiments.clear()
			projectInstance.platforms.clear()
            projectInstance.supervisors.clear()
            projectInstance.analysts.clear()
            projectInstance.sellers.clear()
			projectInstance.spliters.clear()

            def lastStatus = projectInstance.status
            projectInstance.properties = params
			
			def f = request.getFile('myFile')
			if(!f.empty && f.size>1024*1024*10){
				flash.message = "上传文件太大，不能超过10M"
				render(view: "edit", model: [projectInstance: projectInstance] + projectResource())
				
			}else{
				if(!f.empty){        //只要选中了文件，就会上传，防止同名的文件做修改后再被选中不上传
						def webRootDir = servletContext.getRealPath("/")
						def rootFilePath = new File(webRootDir, "WEB-INF/upload")
						if(!rootFilePath.exists()){
							rootFilePath.mkdirs();
						}
						if(deleteFile(params.fileName,rootFilePath.toString())){
							def fileUrl=upload()               //调用上传的方法，返回一个储存文件的路径。
							projectInstance.fileName=fileUrl      //存储文件路径
						}
				}
				
				if (!projectInstance.hasErrors() && projectInstance.save(flush: true)) {
					if(projectInstance.isControled && projectInstance.isRemoted){
						def worktimeInstance=new Worktime()
						worktimeInstance.project = projectInstance
						def workcontentInstance =Workcontent.findByCode(projectInstance.routineAnalysis)
						worktimeInstance.workcontents=workcontentInstance
						worktimeInstance.completer=springSecurityService.currentUser
						worktimeInstance.workWay="WAY_USUAL"
						worktimeInstance.manHour=projectInstance.manHour
						worktimeInstance.machineHour=projectInstance.machineHour
						worktimeInstance.comment2=projectInstance.comment1
						worktimeInstance.finishedDate=projectInstance.analySendDate
						worktimeInstance.save(flush: true)
					}
					//更新项目后，如果新增了相关人员，会发邮件提醒
					def mailList = getUserMailLists(projectInstance,currentUser)
					if(mailList.contains(currentUser.email)){//去除当前用户
						mailList.remove(currentUser.email)
					}
					def userEmailAndRole=getUserMailAndRoleMaps(projectInstance,currentUser)
					def nameList = new ArrayList<String>()
					mailList?.each { mail ->
						def toUser=User.findByEmail(mail)
						def roleBefor=userEmailAndRolebefor.get(mail)
						def role=userEmailAndRole.get(mail)
						if( "".equals(mailListbefor) || mailListbefor==null || !mailListbefor.contains(mail) || !roleBefor.toString().equals(role.toString())){
							nameList.add(toUser.name)
							def sendMail=new SendEmailAsynchronously(mail,Util.getCurrentTimeString()+"提醒-->更新项目："+projectInstance.title,"你好，<b>"+toUser.name+"</b>，<br/>&nbsp;&nbsp;&nbsp;&nbsp;刚刚<b>"+currentUser.name+"</b>更新了项目：<b>"+projectInstance.information+"</b> ；你是该项目的<b>"+userEmailAndRole.get(mail).toString()+"</b>，请及时处理： <a href=http://192.168.2.72:8080/bpms/project/show/"+projectInstance.title+">去处理</a>")
							sendMail.sendMail()
						}
					}
					if(nameList){
						def sendMail=new SendEmailAsynchronously(currentUser.email,Util.getCurrentTimeString()+"提醒-->更新项目："+projectInstance.title,"你好，<b>"+currentUser.name+"</b>，<br/>&nbsp;&nbsp;&nbsp;&nbsp;刚刚你更新了项目：<b>"+projectInstance.information+"</b> ；并发送邮件提醒了<b>"+nameList.toString()+"</b>：<a href=http://192.168.2.72:8080/bpms/project/show/"+projectInstance.title+">去查看</a>")
						sendMail.sendMail()
					}
					
					if (lastStatus != projectInstance.status) {
						def track = new Track(project: projectInstance, operator: springSecurityService.currentUser, status: projectInstance.status)
						track.save(flush: true)
					}
					
					flash.message = "${message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), ''])}"
					redirect(action: "show", id: projectInstance.id)
				}
				else {
					render(view: "edit", model: [projectInstance: projectInstance] + projectResource())
				}
			}
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def projectInstance = Project.get(params.id)
        if (projectInstance) {
            try {
                projectInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), ''])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'project.label', default: 'Project'), ''])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
    }

    def projectResource() {
        def supervisorRole = Role.findByAuthority("ROLE_SUPERVISOR")
        def supervisorInstanceList = UserRole.findAllByRole(supervisorRole).user

        def analystRole = Role.findByAuthority("ROLE_ANALYST")
        def analystInstanceList = UserRole.findAllByRole(analystRole).user

        def sellerRole = Role.findByAuthority("ROLE_SELLER")
        def sellerInstanceList = UserRole.findAllByRole(sellerRole).user
		
		def spliterRole = Role.findByAuthority("ROLE_SPLITER")
		def spliterInstanceList = UserRole.findAllByRole(spliterRole).user

        def experimentInstanceList = Experiment.list()
		def platformInstanceList = Platform.list()
        def analysisInstanceList = Analysis.list()

        return [supervisorInstanceList: supervisorInstanceList,
            analystInstanceList: analystInstanceList,
            sellerInstanceList: sellerInstanceList,
			spliterInstanceList:spliterInstanceList,
			platformInstanceList: platformInstanceList,
            experimentInstanceList: experimentInstanceList,
            analysisInstanceList: analysisInstanceList]
    }
	def selectProjectItem(){//列选择
		def projectItemList=params.sub1
		def itemNum=0
		if(projectItemList){
			boolean flag = projectItemList instanceof String //当选择的列只有一列时为String类型，多列为对象，所以要加判断
			def itemList=""
			if(!flag){
				Iterator iterItem = projectItemList.iterator()
				while(iterItem.hasNext()){
				 String str = (String) iterItem.next()
				 itemList=itemList+str+"_"                  //params重定向只能是传递String类型
				 itemNum++
				}
			}else{
				itemNum=1
				itemList=projectItemList
			}
			redirect(action: params.lastAction, params: [projectItemList: itemList,itemNum:itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:params.q])
		}else{
			redirect(action: params.lastAction, params: [itemNum:itemNum,order:params.order,max:params.max,sort:params.sort,offset:params.offset,q:params.q])
		}
	}
	def exportToExcel(){                                //选择性导出列或项目
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
		
		def projectItem=params.projectItemList            //String类型的projectItem
		def head=getProjectHead(projectItem)              //得到表头
		if(projectItem==null || projectItem=="" || "".equals(projectItem)){		  //如果为空则对projectItem赋值，下面会用到
			projectItem="title contract level projectCreateDate lastUpdated dueTime supervisors analysts sellers status messages experiments"+
			"analyses salesman customerUnit customerName platforms information species samplesize k3number batch metaSendData analyStartDate"+
			"analySendDate innerDueDate backupDate metaSendWay analySendWay manHour machineHour comment1 overdueReason backupLocation isRemoted isControled"+
			"worktimes libraryBuildWay readLength readsNum dataSize spliters"
		}
		
		out.write(head.toString().getBytes("gbk"));
		out.flush();
		
		def buf=new StringBuffer();
		
		if(params.exportAll || "".equals(params.sub)){                              //判断是否是导出全部项目，若是则projectIdList为全部项目id
			def projectIdList=Project.executeQuery("SELECT id FROM Project project")
			Iterator iter = projectIdList.iterator()
			while(iter.hasNext()){
			 def str = (String) iter.next()
			 def bf=getProjectList(str,projectItem)
			 buf.append(bf.toString());
			}
		}else{                                             //若否则projectIdList为页面传递过来的id
			def projectIdList=params.sub
			boolean fla = projectIdList instanceof String     //判断是否只选择了一行项目，要不然会把string分解
			if(fla){
				def str=projectIdList
				def bf=getProjectList(str,projectItem)        //得到项目列表
				buf.append(bf.toString());
			}else{
				Iterator iter = projectIdList.iterator()
				while(iter.hasNext()){
				 def str = (String) iter.next()
				 def bf=getProjectList(str,projectItem)
				 buf.append(bf.toString());
				}
			}
		}
		
		out.write(buf.toString().getBytes("gbk"));
		out.flush();
		out.close();
		session.loadOver=true;
	}
	
	public StringBuffer getProjectHead(String projectItem){   //得到表头信息
		def itemHashMap=new HashMap<String,String>()
		itemHashMap.put("title","项目编号")
		itemHashMap.put("projectCreateDate","项目登记日期")
		itemHashMap.put("contract","合同号")
		itemHashMap.put("information","项目信息")
		itemHashMap.put("level","优先级")
		itemHashMap.put("status","状态")
		itemHashMap.put("platforms","检测平台")
		itemHashMap.put("experiments","检测类型")
		itemHashMap.put("species","物种")
		itemHashMap.put("samplesize","芯片数/样本数")
		itemHashMap.put("k3number","k3编号")
		itemHashMap.put("batch","批次")
		itemHashMap.put("analyses","工作性质")
		itemHashMap.put("salesman","销售员")
		itemHashMap.put("supervisors","技术支持")
		itemHashMap.put("analysts","数据分析员")
		itemHashMap.put("sellers","审核员")
		itemHashMap.put("analyStartDate","数据分析开始日期")
		itemHashMap.put("analySendDate","数据分析给出日期")
		itemHashMap.put("analySendWay","数据分析给出方式")
		itemHashMap.put("manHour","工时(h)")
		itemHashMap.put("machineHour","机时(h)")
		itemHashMap.put("dueTime","到期时间")
		itemHashMap.put("innerDueDate","内部周期到期时间")
		itemHashMap.put("overdueReason","过期原因")
		itemHashMap.put("isRemoted","是否远程备份")
		itemHashMap.put("backupDate","数据备份日期")
		itemHashMap.put("backupLocation","备份位置")
		itemHashMap.put("isControled","是否管控完毕")
		itemHashMap.put("comment1","备注1")
		itemHashMap.put("libraryBuildWay","建库方式")
		itemHashMap.put("readLength","测序读长(bp)")
		itemHashMap.put("readsNum","reads/样本")
		itemHashMap.put("dataSize","数据量(G)/样本")
		itemHashMap.put("spliters","CASAVA拆分员")
		itemHashMap.put("metaSendData","原始数据给出日期")
		itemHashMap.put("metaSendWay","原始数据给出方式")
		
		StringBuffer head = new StringBuffer();
		
		if(projectItem){
			head.append("\"")
			head.append(projectItem.contains("title") ? itemHashMap.get("title")+"\"" : "")
			head.append(projectItem.contains("projectCreateDate") ? ",\""+itemHashMap.get("projectCreateDate")+"\"" : "")
			head.append(projectItem.contains("contract") ? ",\""+itemHashMap.get("contract")+"\"" : "")
			head.append(projectItem.contains("information") ? ",\""+itemHashMap.get("information")+"\"" : "")
			head.append(projectItem.contains("level") ? ",\""+itemHashMap.get("level")+"\"" : "")
			head.append(projectItem.contains("status") ? ",\""+itemHashMap.get("status")+"\"" : "")
			head.append(projectItem.contains("platforms") ? ",\""+itemHashMap.get("platforms")+"\"" : "")
			head.append(projectItem.contains("experiments") ? ",\""+itemHashMap.get("experiments")+"\"" : "")
			head.append(projectItem.contains("species") ? ",\""+itemHashMap.get("species")+"\"" : "")
			head.append(projectItem.contains("samplesize") ? ",\""+itemHashMap.get("samplesize")+"\"" : "")
			head.append(projectItem.contains("k3number") ? ",\""+itemHashMap.get("k3number")+"\"" : "")
			head.append(projectItem.contains("batch") ? ",\""+itemHashMap.get("batch")+"\"" : "")
			head.append(projectItem.contains("analyses") ? ",\""+itemHashMap.get("analyses")+"\"" : "")
			head.append(projectItem.contains("salesman") ? ",\""+itemHashMap.get("salesman")+"\"" : "")
			head.append(projectItem.contains("supervisors") ? ",\""+itemHashMap.get("supervisors")+"\"" : "")
			head.append(projectItem.contains("analysts") ? ",\""+itemHashMap.get("analysts")+"\"" : "")
			head.append(projectItem.contains("sellers") ? ",\""+itemHashMap.get("sellers")+"\"" : "")
			head.append(projectItem.contains("analyStartDate") ? ",\""+itemHashMap.get("analyStartDate")+"\"" : "")
			head.append(projectItem.contains("analySendDate") ? ",\""+itemHashMap.get("analySendDate")+"\"" : "")
			head.append(projectItem.contains("analySendWay") ? ",\""+itemHashMap.get("analySendWay")+"\"" : "")
			head.append(projectItem.contains("manHour") ? ",\""+itemHashMap.get("manHour")+"\"" : "")
			head.append(projectItem.contains("machineHour") ? ",\""+itemHashMap.get("machineHour")+"\"" : "")
			head.append(projectItem.contains("dueTime") ? ",\""+itemHashMap.get("dueTime")+"\"" : "")
			head.append(projectItem.contains("innerDueDate") ? ",\""+itemHashMap.get("innerDueDate")+"\"" : "")
			head.append(projectItem.contains("overdueReason") ? ",\""+itemHashMap.get("overdueReason")+"\"" : "")
			head.append(projectItem.contains("isRemoted") ? ",\""+itemHashMap.get("isRemoted")+"\"" : "")
			head.append(projectItem.contains("backupDate") ? ",\""+itemHashMap.get("backupDate")+"\"" : "")
			head.append(projectItem.contains("backupLocation") ? ",\""+itemHashMap.get("backupLocation")+"\"" : "")
			head.append(projectItem.contains("isControled") ? ",\""+itemHashMap.get("isControled")+",\"" : "")
			head.append(projectItem.contains("comment1") ? ",\""+itemHashMap.get("comment1")+"\"" : "")
			head.append(projectItem.contains("libraryBuildWay") ? ",\""+itemHashMap.get("libraryBuildWay")+"\"" : "")
			head.append(projectItem.contains("readLength") ? ",\""+itemHashMap.get("readLength")+"\"" : "")
			head.append(projectItem.contains("readsNum") ? ",\""+itemHashMap.get("readsNum")+"\"" : "")
			head.append(projectItem.contains("dataSize") ? ",\""+itemHashMap.get("dataSize")+"\"" : "")
			head.append(projectItem.contains("spliters") ? ",\""+itemHashMap.get("spliters")+"\"" : "")
			head.append(projectItem.contains("metaSendData") ? ",\""+itemHashMap.get("metaSendData")+"\"" : "")
			head.append(projectItem.contains("metaSendWay") ? ",\""+itemHashMap.get("metaSendWay")+"\"" : "")
			def headStr=head.toString()
			def start = headStr.indexOf(",");
			def n = headStr.indexOf(",", start + 1);
			if(headStr.startsWith('",') && n>0) {         //n>0判断是否只选择了一列，且这一列不是title,不是开头的
				head.deleteCharAt(1)
				headStr=head.toString()
				head.deleteCharAt(headStr.indexOf('",'))
				//head.deleteCharAt(headStr.indexOf('"'))
			}else if(headStr.startsWith('",')){
				head.deleteCharAt(headStr.indexOf(','))
				head.deleteCharAt(headStr.indexOf('"'))
			}
		}else{
			head.append("项目编号,项目登记日期,合同号,项目信息,优先级,状态,检测平台,检测类型,物种,芯片数/样本数,k3编号,批次,工作性质,销售员,技术支持,数据分析员,审核员,数据分析开始日期,数据分析给出日期,数据分析给出方式,工时(h),机时(h),到期时间,内部周期到期时间,过期原因,是否远程备份,数据备份日期,备份位置,是否管控完毕,备注1,建库方式,测序读长(bp),reads/样本,数据量(G)/样本,CASAVA拆分员,原始数据给出日期,原始数据给出方式")
		}
		head.append("\n");
		return head
	}
	
	public StringBuffer getProjectList(String str,String projectItem){  //得到项目信息
		def projeceId=Integer.valueOf(str).intValue()
		def project=Project.findById(projeceId)
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd")
		def dueTimeStr=""
		if(project.dueTime){
			dueTimeStr="'"+format.format(new Date(project.dueTime.getTime()))
		}
		def dateCreatedStr=""
		if(project.projectCreateDate){
			dateCreatedStr="'"+format.format(new Date(project.projectCreateDate.getTime()))
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
		
		def levelMessage =""
		if(project.level){
			def level = "project.level."+project.level+".label"
			levelMessage = "${message(code: level)}"
		}
		def statusMessage =""
		if(project.status){
			def status = "project.status."+project.status+".label"
			statusMessage = "${message(code: status)}"
		}
		def analySendWayMessage =""
		if(project.analySendWay){
			def analySendWay = "project.way."+project.analySendWay+".label"
			analySendWayMessage = "${message(code: analySendWay)}"
		}
		def metaSendWayMessage =""
		if(project.metaSendWay){
			def metaSendWay = "project.way."+project.metaSendWay+".label"
			metaSendWayMessage = "${message(code: metaSendWay)}"
		}
		def isRemoted = "project.isRemoted."+project.isRemoted+".label"
		def isRemotedMessage = "${message(code: isRemoted)}"
		def isControled = "project.isControled."+project.isControled+".label"
		def isControledMessage = "${message(code: isControled)}"
		
		def bf=new StringBuffer();
		bf.append("\"")
		bf.append(projectItem.contains("title") ? project.title?project.title+"\"":"\"" : "")
		bf.append(projectItem.contains("projectCreateDate") ? ",\""+dateCreatedStr+"\"" : "")
		bf.append(projectItem.contains("contract") ? project.contract?",\""+project.contract+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("information") ? project.information?",\""+project.information+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("level") ? ",\""+levelMessage+"\"" : "")
		bf.append(projectItem.contains("status") ? ",\""+statusMessage+"\"" : "")
		if(projectItem.contains("platforms")){
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
		}
		if(projectItem.contains("experiments")){
			bf.append(",\"")
			if(project.experiments){
				Iterator iterExperiment = project.experiments.iterator()
				def search=0
				while(iterExperiment.hasNext()){
					def strExperiment = (Experiment) iterExperiment.next()
					if(search>0){
					   bf.append("/"+strExperiment.title)
					}else{
					   search++
						bf.append(strExperiment.title)
					}
				}
			}
			bf.append("\"")
		}
		def species=""
		if(project.species){
			species=project.species
			if(species.contains(",")){
        		species=species.replace(","," ");
        	}
			if(species.contains("，")){
				species=species.replace("，"," ");
			}
		}
		bf.append(projectItem.contains("species") ? species?",\""+species+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("samplesize") ? project.samplesize?",\""+project.samplesize+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("k3number") ? project.k3number?",\""+project.k3number+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("batch") ? project.batch?",\""+project.batch+"\"":",\""+""+"\"" : "")
		if(projectItem.contains("analyses")){
			bf.append(",\"")
			if(project.analyses){
				Iterator iterExperiment = project.analyses.iterator()
				def search=0
				while(iterExperiment.hasNext()){
					def strExperiment = (Analysis) iterExperiment.next()
					if(search>0){
					   bf.append("/"+strExperiment.title)
					}else{
					   search++
						bf.append(strExperiment.title)
					}
				}
			}
			bf.append("\"")
		}
		bf.append(projectItem.contains("salesman") ? project.salesman?",\""+project.salesman+"\"":",\""+""+"\"" : "")
		if(projectItem.contains("supervisors")){
			bf.append(",\"")
			if(project.supervisors){
				Iterator iterSupervisor = project.supervisors.iterator()
				def index1=0
				while(iterSupervisor.hasNext()){
					def strExperiment = (User) iterSupervisor.next()
					if(index1>0){
					   bf.append("/"+strExperiment.name)
					}else{
					   index1++
						bf.append(strExperiment.name)
					}
				}
			}
			bf.append("\"")
		}
		if(projectItem.contains("analysts")){
			bf.append(",\"")
			if(project.analysts){
				Iterator iterAnalyst = project.analysts.iterator()
				def index2=0
				while(iterAnalyst.hasNext()){
					def strExperiment = (User) iterAnalyst.next()
					if(index2>0){
					   bf.append("/"+strExperiment.name)
					}else{
					   index2++
						bf.append(strExperiment.name)
					}
				}
			}
			bf.append("\"")
		}
		if(projectItem.contains("sellers")){
			bf.append(",\"")
			if(project.sellers){
				Iterator iterSeller = project.sellers.iterator()
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
		}
		bf.append(projectItem.contains("analyStartDate") ? ",\""+analyStartDateStr+"\"" : "")
		bf.append(projectItem.contains("analySendDate") ? ",\""+analySendDateStr+"\"" : "")
		bf.append(projectItem.contains("analySendWay") ? ",\""+analySendWayMessage+"\"" : "")
		bf.append(projectItem.contains("manHour") ? project.manHour?",\""+project.manHour+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("machineHour") ? project.machineHour?",\""+project.machineHour+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("dueTime") ? ",\""+dueTimeStr+"\"" : "")
		bf.append(projectItem.contains("innerDueDate") ? ",\""+innerDueDateStr+"\"" : "")
		bf.append(projectItem.contains("overdueReason") ? project.overdueReason?",\""+project.overdueReason+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("isRemoted") ? ",\""+isRemotedMessage+"\"" : "")
		bf.append(projectItem.contains("backupDate") ? ",\""+backupDateStr+"\"" : "")
		bf.append(projectItem.contains("backupLocation") ? project.backupLocation?",\""+project.backupLocation+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("isControled") ? ",\""+isControledMessage+"\"" : "")
		bf.append(projectItem.contains("comment1") ? project.comment1?",\""+project.comment1+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("libraryBuildWay") ? project.libraryBuildWay?",\""+project.libraryBuildWay+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("readLength") ? project.readLength?",\""+project.readLength+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("readsNum") ? project.readsNum?",\""+project.readsNum+"\"":",\""+""+"\"" : "")
		bf.append(projectItem.contains("dataSize") ? project.dataSize?",\""+project.dataSize+"\"":",\""+""+"\"" : "")
		if(projectItem.contains("spliters")){
			bf.append(",\"")
			if(project.spliters){
				Iterator iterSeller = project.spliters.iterator()
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
		}
		bf.append(projectItem.contains("metaSendData") ? ",\""+metaSendDataStr+"\"" : "")
		bf.append(projectItem.contains("metaSendWay") ? ",\""+metaSendWayMessage+"\"" : "")
		def bfStr=bf.toString()
		def start = bfStr.indexOf(",");
		def n = bfStr.indexOf(",", start + 1);
		if(bfStr.startsWith('",') && n>0) {       //n>0判断是否只选择了一列，且这一列不是title,不是开头的
			bf.deleteCharAt(1)
			bfStr=bf.toString()
			bf.deleteCharAt(bfStr.indexOf('",'))
		}else if(bfStr.startsWith('",')){
			bf.deleteCharAt(bfStr.indexOf(','))
			bf.deleteCharAt(bfStr.indexOf('"'))
		}
		bf.append("\n");
		return bf
	}
	
	def upload(){                        //文件上传
		def originalFileName
		def fileName
		def filePath
		def f = request.getFile('myFile')
		if(!f.empty) {
			 def webRootDir = servletContext.getRealPath("/")
			 def userDir = new File(webRootDir,"WEB-INF/upload/")
			 if(!userDir.exists()){
				 userDir.mkdirs()
			 }
			 originalFileName=f.originalFilename
			 if(originalFileName instanceof String){
				 fileName=makeFileName(originalFileName)       //防止重名覆盖
			 }else{
				 println "传入了多个文件名"
			 }
			 filePath=makePath(fileName,userDir.toString())      //防止单个文件夹下文件过多
			 f.transferTo(new File(filePath,fileName))
		//	 Date d=new Date();
		//	 String dateString=d.format("yyyy-mm-dd-hh-ss")
		//	 String fileProName=f.getOriginalFilename()
		//	 String extension = fileProName.split('\\.')[-1]    //截取获取文件名的后缀
		//	 fileName=dateString+"."+extension
		//	 filePath="web-app/images/"
		//	 f.transferTo(new File(filePath+fileName))
		}
		return fileName
	}
	
	def download(){
		def fileName
		def rootFilePath
		def filepath
		def realName
		
		def projectInstance =  Project.get(params.id)
		fileName = projectInstance.fileName
		
		def webRootDir = servletContext.getRealPath("/")
		rootFilePath = new File(webRootDir, "WEB-INF/upload")
		if(!rootFilePath.exists()){
			rootFilePath.mkdirs();
		}
		filepath = findFileSavePathByFileName(fileName,rootFilePath.toString())
		
		realName = fileName.substring(fileName.indexOf("_")+1);
		response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(realName, "UTF-8"))
		response.contentType = ""
		
		def out = response.outputStream
		def inputStream = new FileInputStream(filepath + "\\" + fileName)
		byte[] buffer = new byte[1024]
		int i = -1
		while ((i = inputStream.read(buffer)) != -1) {
			out.write(buffer, 0, i)
		}
		out.flush()
		out.close()
		inputStream.close()

	}
	
	private String makeFileName(String filename){ //2.txt
		//为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}
	   /**
	   * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	   * @param filename 文件名，要根据文件名生成存储目录
	   * @param savePath 文件存储路径
	   * @return 新的存储目录
	   */
	private String makePath(String filename,String savePath){
		//得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode&0xf; //0--15
		int dir2 = (hashcode&0xf0)>>4; //0-15
		//构造新的保存目录
		String dir = savePath + "\\" + dir1 + "\\" + dir2 + "\\" + dir1 + "\\" + dir2; //upload\2\3\2\3
		//File既可以代表文件也可以代表目录
		File file = new File(dir);
		//如果目录不存在
		if(!file.exists()){
		//创建目录
		 file.mkdirs();
		}
		return dir;
	}
	// 删除文件
	public static boolean deleteFile(String filename,String saveRootPath) {
		boolean flag = false;
		if(filename){
			String filePath = findFileSavePathByFileName(filename,saveRootPath)
			File file = new File(filePath + "\\" + filename);
			// 路径为文件且不为空则进行删除
			if (file.isFile() && file.exists()) {
				file.delete();
				flag = true;
			}
		}else{
			flag = true;
		}
		
		return flag;
	}
	public static String findFileSavePathByFileName(String filename,String saveRootPath){
	   int hashcode = filename.hashCode();
	   int dir1 = hashcode&0xf; //0--15
	   int dir2 = (hashcode&0xf0)>>4; //0-15
	   String dir = saveRootPath + "\\" + dir1 + "\\" + dir2 + "\\" + dir1 + "\\" + dir2; //upload\2\3\2\3
	   File file = new File(dir);
	   if(!file.exists()){
		   //创建目录
		   file.mkdirs();
	   }
	   return dir;
	}
	public static boolean getFlag(Project projectInstance,User currentUser){
		def flag=false
		def nameStr=new StringBuffer();
		if(projectInstance.supervisors){
			Iterator iterSupervisor = projectInstance.supervisors.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				nameStr.append(strExperiment.username)
			}
		}
		if(projectInstance.analysts){
			Iterator iterSupervisor = projectInstance.analysts.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				nameStr.append(strExperiment.username)
			}
		}
		if(projectInstance.sellers){
			Iterator iterSupervisor = projectInstance.sellers.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				nameStr.append(strExperiment.username)
			}
		}
		if(projectInstance.spliters){
			Iterator iterSupervisor = projectInstance.spliters.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				nameStr.append(strExperiment.username)
			}
		}
		def currentAuthoritiesString=currentUser.getAuthoritiesString()
		if(nameStr.toString().contains(currentUser.username) || currentAuthoritiesString.contains("ROLE_ADMIN")){
			flag=true
		}
		return flag;
	}
	public static List<String> getUserMailLists(Project projectInstance,User currentUser){//获取当前项目所有参与者的email
		def mailList=new ArrayList<String>()
		if(projectInstance.supervisors){
			Iterator iterSupervisor = projectInstance.supervisors.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				mailList.add(strExperiment.email)
			}
		}
		if(projectInstance.analysts){
			Iterator iterSupervisor = projectInstance.analysts.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				mailList.add(strExperiment.email)
			}
		}
		if(projectInstance.sellers){
			Iterator iterSupervisor = projectInstance.sellers.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				mailList.add(strExperiment.email)
			}
		}
		if(projectInstance.spliters){
			Iterator iterSupervisor = projectInstance.spliters.iterator()
			while(iterSupervisor.hasNext()){
				def strExperiment = (User) iterSupervisor.next()
				mailList.add(strExperiment.email)
			}
		}
		for (int i = 0; i < mailList.size(); i++){  //外循环是循环的次数
			for (int j = mailList.size() - 1 ; j > i; j--){  //内循环是 外循环一次比较的次数
				if (mailList[i] == mailList[j]){
					mailList.remove(j);
				}
			}
		}
		return mailList
	}
	
	public static HashMap<String, ArrayList> getUserMailAndRoleMaps(Project projectInstance,User currentUser){
		def userEamilAndRoleMap=new HashMap<String, ArrayList>();
		if(projectInstance.supervisors){
			Iterator iterSupervisor = projectInstance.supervisors.iterator()
			while(iterSupervisor.hasNext()){
				def roleList1=new ArrayList<String>()
				roleList1.add("技术支持")
				def strExperiment = (User) iterSupervisor.next()
				if(userEamilAndRoleMap.get(strExperiment.email)){
					def roleList=userEamilAndRoleMap.get(strExperiment.email)
					roleList.addAll(roleList1)
					userEamilAndRoleMap.put(strExperiment.email, roleList)
				}else{
					userEamilAndRoleMap.put(strExperiment.email, roleList1)
				}
			}
		}
		if(projectInstance.analysts){
			Iterator iterSupervisor = projectInstance.analysts.iterator()
			while(iterSupervisor.hasNext()){
				def roleList2=new ArrayList<String>()
				roleList2.add("数据分析员")
				def strExperiment = (User) iterSupervisor.next()
				if(userEamilAndRoleMap.get(strExperiment.email)){
					def roleList=userEamilAndRoleMap.get(strExperiment.email)
					if(roleList.contains("数据分析员")){
					}else{
						roleList.addAll(roleList2)
						userEamilAndRoleMap.put(strExperiment.email, roleList)
					}
				}else{
					userEamilAndRoleMap.put(strExperiment.email, roleList2)
				}
			}
		}
		if(projectInstance.sellers){
			Iterator iterSupervisor = projectInstance.sellers.iterator()
			while(iterSupervisor.hasNext()){
				def roleList3=new ArrayList<String>()
				roleList3.add("审核员")
				def strExperiment = (User) iterSupervisor.next()
				if(userEamilAndRoleMap.get(strExperiment.email)){
					def roleList=userEamilAndRoleMap.get(strExperiment.email)
					if(roleList.contains("审核员")){
					}else{
						roleList.addAll(roleList3)
					userEamilAndRoleMap.put(strExperiment.email, roleList)
					}
				}else{
					userEamilAndRoleMap.put(strExperiment.email, roleList3)
				}
			}
		}
		if(projectInstance.spliters){
			Iterator iterSupervisor = projectInstance.spliters.iterator()
			while(iterSupervisor.hasNext()){
				def roleList4=new ArrayList<String>()
				roleList4.add("CASAVA拆分员")
				def strExperiment = (User) iterSupervisor.next()
				if(userEamilAndRoleMap.get(strExperiment.email)){
					def roleList=userEamilAndRoleMap.get(strExperiment.email)
					if(roleList.contains("CASAVA拆分员")){
					}else{
						roleList.addAll(roleList4)
						userEamilAndRoleMap.put(strExperiment.email, roleList)
					}
				}else{
					userEamilAndRoleMap.put(strExperiment.email, roleList4)
				}
			}
		}
		if(userEamilAndRoleMap.containsKey(currentUser.email)){
			userEamilAndRoleMap.remove(currentUser.email)
		}
		return userEamilAndRoleMap
	}
}

package com.capitalbiotech.bpms

import groovy.io.FileType
import java.io.File
import grails.converters.JSON
import groovy.json.JsonOutput
import java.util.concurrent.Callable

class UpdateJob {
    static triggers = {//毫秒为单位
       // simple name: 'updateNoticeUnreadTrigger', startDelay: 3600000, repeatInterval: 3600000
    }
	def grailsApplication
    def concurrent = false
    def group = "default"
    def description = "Update notice unread"
    def execute(){
		//Util.backupSql(grailsApplication)
		
        log.debug("Updating notice unread...")
       
        User.list().each { userInstance ->
            userInstance.unread = 0
            userInstance.save(flush: true)
        }
    }
}

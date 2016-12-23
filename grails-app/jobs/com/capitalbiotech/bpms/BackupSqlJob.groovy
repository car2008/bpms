package com.capitalbiotech.bpms



class BackupSqlJob {
    static triggers = {
        //simple name: 'backupSqlTrigger', startDelay: 10000, repeatInterval: 60000
		cron name: 'backupSqlTrigger', cronExpression: "0 59 23 * * ?"
    }
	def grailsApplication
    def concurrent = false
    def group = "default"
    def description = "Backup mysql database"
    def execute(){
		log.debug("Automatic Backuping database...")
		Util.backupSql(grailsApplication)
		/*println "Updating..."
        log.debug("Updating notice unread...")
        
        User.list().each { userInstance ->
            userInstance.unread = 2
            userInstance.save(flush: true)
        }*/
    }
}

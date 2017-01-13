import com.capitalbiotech.bpms.Platform
import com.capitalbiotech.bpms.Preference
import com.capitalbiotech.bpms.Role
import com.capitalbiotech.bpms.Experiment
import com.capitalbiotech.bpms.Analysis
import com.capitalbiotech.bpms.Project
import com.capitalbiotech.bpms.Message
import com.capitalbiotech.bpms.Workcontent
import com.capitalbiotech.bpms.Track
import com.capitalbiotech.bpms.User
import com.capitalbiotech.bpms.UserRole
import com.capitalbiotech.bpms.Util
import com.capitalbiotech.bpms.Worktime

class BootStrap {
    def springSecurityService

    def init = { /*servletContext ->
        if (0 == Preference.count()) {
            createPreferences()
        }
        if(0 == User.count()) {
            createUsersAndRoles()
        }
		if(0 == Platform.count()) {
			createPlatforms()
		}
        if(0 == Experiment.count()) {
            createExperiments()
        }
		if(0 == Workcontent.count()) {
			createWorkcontents()
		}
        if(0 == Analysis.count()) {
            createAnalyses()
        }
        if(0 == Project.count()) {
            createProjects()
        }
        if(0 == Message.count()) {
            createMessages()
        }
		if(0 == Worktime.count()) {
			createWorktimes()
		}
        if(0 == Track.count()) {
            createTracks()
        }*/
    }

    def destroy = {
        
    }
    
    def createPreferences() {
        log.info "Creating default preferences"
        
        def propertiesList = readRawFile("preferences")
        propertiesList?.each { properties ->
            def preferenceInstance = new Preference(properties)
            if (preferenceInstance.hasErrors() || !preferenceInstance.save(flush: true)) {
                preferenceInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }

    def createUsersAndRoles(){
        log.info "Creating default users and roles"
        
        def authorityRoleMap = [:]
        def propertiesList = readRawFile("roles")
        propertiesList?.each { properties ->
            def roleInstance = new Role(properties)
            if (roleInstance.hasErrors() || !roleInstance.save(flush: true)) {
                roleInstance.errors?.each { error ->
                    log.error error
                }
            }
            
            authorityRoleMap[(roleInstance?.authority)] = roleInstance
        }
        
        propertiesList = readRawFile("users")
        propertiesList?.each { properties ->
            properties['password'] = springSecurityService.encodePassword(properties['password'])
            
            def userInstance = new User(properties)
            if (userInstance.hasErrors() || !userInstance.save(flush: true)) {
                userInstance.errors?.each { error ->
                    log.error error
                }
            }
            
            properties['roles'].split(",")?.each { authority ->
                if (authorityRoleMap[authority] != null) {
                    UserRole.create(userInstance, authorityRoleMap[authority], true)
                }
                else {
                    log.error "Role not found: ${authority}"
                }
            }
        }
    }
    
    def createExperiments(){
        log.info "Creating sample experiments"
        
        def propertiesList = readRawFile("experiments")
        propertiesList?.each { properties ->
            if (!Util.isEmpty(properties['dateCreated'])) {
                properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
            }
            else {
                properties['dateCreated'] = null
            }
            
            if (!Util.isEmpty(properties['lastUpdated'])) {
                properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
            }
            else {
                properties['lastUpdated'] = null
            }

            def experimentInstance = new Experiment(properties)
            if (experimentInstance.hasErrors() || !experimentInstance.save(flush: true)) {
                experimentInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }
	
	def createWorkcontents(){
		log.info "Creating sample workcontents"
		
		def propertiesList = readRawFile("workcontents")
		propertiesList?.each { properties ->
			if (!Util.isEmpty(properties['dateCreated'])) {
				properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
			}
			else {
				properties['dateCreated'] = null
			}
			
			if (!Util.isEmpty(properties['lastUpdated'])) {
				properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
			}
			else {
				properties['lastUpdated'] = null
			}

			def workcontentInstance = new Workcontent(properties)
			if (workcontentInstance.hasErrors() || !workcontentInstance.save(flush: true)) {
				workcontentInstance.errors?.each { error ->
					log.error error
				}
			}
		}
	}
    
	def createPlatforms(){
		log.info "Creating sample platforms"
		
		def propertiesList = readRawFile("platforms")
		propertiesList?.each { properties ->
			if (!Util.isEmpty(properties['dateCreated'])) {
				properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
			}
			else {
				properties['dateCreated'] = null
			}
			
			if (!Util.isEmpty(properties['lastUpdated'])) {
				properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
			}
			else {
				properties['lastUpdated'] = null
			}

			def platformInstance = new Platform(properties)
			if (platformInstance.hasErrors() || !platformInstance.save(flush: true)) {
				platformInstance.errors?.each { error ->
					log.error error
				}
			}
		}
	}
	
    def createAnalyses(){
        log.info "Creating sample analyses"
        
        def propertiesList = readRawFile("analyses")
        propertiesList?.each { properties ->
            if (!Util.isEmpty(properties['dateCreated'])) {
                properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
            }
            else {
                properties['dateCreated'] = null
            }
            
            if (!Util.isEmpty(properties['lastUpdated'])) {
                properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
            }
            else {
                properties['lastUpdated'] = null
            }

            def analysisInstance = new Analysis(properties)
            if (analysisInstance.hasErrors() || !analysisInstance.save(flush: true)) {
                analysisInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }

    def createProjects(){
        log.info "Creating sample projects"
        
        def propertiesList = readRawFile("projects")
        propertiesList?.each { properties ->
            println properties['title']
			if (!Util.isEmpty(properties['dueTime'])) {
                properties['dueTime'] = Util.parseSimpleDate(properties['dueTime'])
            }
            else {
                properties['dueTime'] = null
            }
			
			if (!Util.isEmpty(properties['projectCreateDate'])) {
				properties['projectCreateDate'] = Util.parseSimpleDate(properties['projectCreateDate'])
			}
			else {
				properties['projectCreateDate'] = null
			}
            
            if (!Util.isEmpty(properties['dateCreated'])) {
                properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
            }
            else {
                properties['dateCreated'] = null
            }
            
            if (!Util.isEmpty(properties['lastUpdated'])) {
                properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
            }
            else {
                properties['lastUpdated'] = null
            }
			
			if (!Util.isEmpty(properties['metaSendData'])) {
				properties['metaSendData'] = Util.parseSimpleDate(properties['metaSendData'])
			}
			else {
				properties['metaSendData'] = null
			}
			
			if (!Util.isEmpty(properties['analyStartDate'])) {
				properties['analyStartDate'] = Util.parseSimpleDate(properties['analyStartDate'])
			}
			else {
				properties['analyStartDate'] = null
			}
			
			if (!Util.isEmpty(properties['analySendDate'])) {
				properties['analySendDate'] = Util.parseSimpleDate(properties['analySendDate'])
			}
			else {
				properties['analySendDate'] = null
			}
            
			if (!Util.isEmpty(properties['innerDueDate'])) {
				properties['innerDueDate'] = Util.parseSimpleDate(properties['innerDueDate'])
			}
			else {
				properties['innerDueDate'] = null
			}
			
			if (!Util.isEmpty(properties['backupDate'])) {
				properties['backupDate'] = Util.parseSimpleDate(properties['backupDate'])
			}
			else {
				properties['backupDate'] = null
			}
			
            def analyses = properties['analyses']
            properties['analyses'] = null
            
			def platforms = properties['platforms']
			properties['platforms'] = null
			
            def experiments = properties['experiments']
            properties['experiments'] = null
			
			def analysts = properties['analysts']
			properties['analysts'] = null
			
			def sellers = properties['sellers']
			properties['sellers'] = null
			
			def supervisors = properties['supervisors']
			properties['supervisors'] = null
			
			def spliters = properties['spliters']
			properties['spliters'] = null
			
            def projectInstance = new Project(properties)
			
			/*properties['supervisors'] = User.findByUsername(properties['supervisors'])
			properties['spliters'] = User.findByUsername(properties['spliters'])*/
            
			spliters.split(",").each { splitersUsername ->
				def splitersInstance = User.findByUsername(splitersUsername)
				if(splitersInstance){
					projectInstance.addToSpliters(splitersInstance)
				}
			}
			
			supervisors.split(",").each { supervisorsUsername ->
				def supervisorsInstance = User.findByUsername(supervisorsUsername)
				if(supervisorsInstance){
					projectInstance.addToSupervisors(supervisorsInstance)
				}
			}
			
			sellers.split(",").each { sellersUsername ->
				def sellersInstance = User.findByUsername(sellersUsername)
				if(sellersInstance){
					projectInstance.addToSellers(sellersInstance)
				}
			}
			
			analysts.split(",").each { analystsUsername ->
				def analystsInstance = User.findByUsername(analystsUsername)
				if(analystsInstance){
					projectInstance.addToAnalysts(analystsInstance)
				}
			}
			
			platforms.split(",").each { platformCode ->
				def platformInstance = Platform.findByCode(platformCode)
				if(platformInstance){
					projectInstance.addToPlatforms(platformInstance)
				}
			}
			
            experiments.split(",").each { experimentCode ->
                def experimentInstance = Experiment.findByCode(experimentCode)
				if(experimentInstance){
					projectInstance.addToExperiments(experimentInstance)
				}
            }
			
            analyses.split(",").each { analysisCode ->
                def analysisInstance = Analysis.findByCode(analysisCode)
				if(analysisInstance){
					projectInstance.addToAnalyses(analysisInstance)
				}
            }
            
            if (projectInstance.hasErrors() || !projectInstance.save(flush: true)) {
                projectInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }
    
    def createMessages(){
        log.info "Creating sample messages"
        
        def propertiesList = readRawFile("messages")
        propertiesList?.each { properties ->
            properties['owner'] = User.findByUsername(properties['owner'])
            properties['project'] = Project.findByTitle(properties['project'])
            
            if (!Util.isEmpty(properties['dateCreated'])) {
                properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
            }
            else {
                properties['dateCreated'] = null
            }
            
            if (!Util.isEmpty(properties['lastUpdated'])) {
                properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
            }
            else {
                properties['lastUpdated'] = null
            }

            def messageInstance = new Message(properties)
            if (messageInstance.hasErrors() || !messageInstance.save(flush: true)) {
                messageInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }
	
	def createWorktimes(){
		log.info "Creating sample worktimes"
		
		def propertiesList = readRawFile("worktimes")
		propertiesList?.each { properties ->
			//properties['completer'] = User.findByUsername(properties['completer'])
			properties['project'] = Project.findByTitle(properties['project'])
			//properties['workcontents'] = Workcontent.findByCode(properties['workcontents'])
			//properties['platforms'] = Workcontent.findByCode(properties['platforms'])
			
			if (!Util.isEmpty(properties['dateCreated'])) {
				properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
			}
			else {
				properties['dateCreated'] = null
			}
			
			if (!Util.isEmpty(properties['lastUpdated'])) {
				properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
			}
			else {
				properties['lastUpdated'] = null
			}
			
			if (!Util.isEmpty(properties['finishedDate'])) {
				properties['finishedDate'] = Util.parseSimpleDateTime(properties['finishedDate'])
			}
			else {
				properties['finishedDate'] = null
			}
			def workcontents = properties['workcontents']
			properties['workcontents'] = null
			
			def completers = properties['completers']
			properties['completers'] = null
			
			def platforms = properties['platforms']
			properties['platforms'] = null
			
			def worktimeInstance = new Worktime(properties)
			
			workcontents.split(",").each { workcontentCode ->
				def workcontentInstance = Workcontent.findByCode(workcontentCode)
				if(workcontentInstance){
					worktimeInstance.addToWorkcontents(workcontentInstance)
				}
			}
			
			completers.split(",").each { splitersUsername ->
				def splitersInstance = User.findByUsername(splitersUsername)
				if(splitersInstance){
					worktimeInstance.addToCompleters(splitersInstance)
				}
			}
			
			platforms.split(",").each { platformCode ->
				def platformInstance = Platform.findByCode(platformCode)
				if(platformInstance){
					worktimeInstance.addToPlatforms(platformInstance)
				}
			}
			
			if (worktimeInstance.hasErrors() || !worktimeInstance.save(flush: true)) {
				worktimeInstance.errors?.each { error ->
					log.error error
				}
			}
		}
		
		
		
	}
    
    def createTracks(){
        log.info "Creating sample tracks"
        
        def propertiesList = readRawFile("tracks")
        propertiesList?.each { properties ->
            properties['operator'] = User.findByUsername(properties['operator'])
            properties['project'] = Project.findByTitle(properties['project'])
            
            if (!Util.isEmpty(properties['dateCreated'])) {
                properties['dateCreated'] = Util.parseSimpleDateTime(properties['dateCreated'])
            }
            else {
                properties['dateCreated'] = null
            }
            
            if (!Util.isEmpty(properties['lastUpdated'])) {
                properties['lastUpdated'] = Util.parseSimpleDateTime(properties['lastUpdated'])
            }
            else {
                properties['lastUpdated'] = null
            }

            def trackInstance = new Track(properties)
            if (trackInstance.hasErrors() || !trackInstance.save(flush: true)) {
                trackInstance.errors?.each { error ->
                    log.error error
                }
            }
        }
    }
    
    def readRawFile(fileName) {
        def rawLines = new File(BootStrap.class.getResource("raw/${fileName}").getFile()).readLines()
        if (rawLines.size() < 2) {
            return
        }
        
        def propertiesList = []
        def keys = rawLines.get(0).split("\t")
        def values = null
        def line = null
        for (int i = 1; i < rawLines.size(); i++) {
            def properties = [:]
            
            line = rawLines.get(i)
            if (Util.isEmpty(line)) {
                continue
            }
            
            values = line.split("\t")
            for (int j = 0; j < values.size(); j++) {
                properties[(keys[j])] = values[j]
            }
            
            propertiesList << properties
        }
        
        return propertiesList
    }
}

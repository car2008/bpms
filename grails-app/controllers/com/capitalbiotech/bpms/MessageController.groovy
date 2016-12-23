package com.capitalbiotech.bpms

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class MessageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def springSecurityService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def save = {
        def messageInstance = new Message(params)
        def projectInstance = Project.get(params.projectId)
        messageInstance.project = projectInstance
        
        messageInstance.owner = springSecurityService.currentUser
        if (!messageInstance.hasErrors() && messageInstance.save(flush: true)) {
            //save unread notice count
            def usersToNotify = projectInstance.supervisors + projectInstance.analysts + projectInstance.sellers
            usersToNotify.each { userInstance ->
                def noticeInstance = Notice.findByProjectAndUser(projectInstance, userInstance)
                if (noticeInstance) {
                    noticeInstance.unread = noticeInstance.unread + 1
                }
                else {
                    noticeInstance = new Notice(project: projectInstance, user: userInstance, unread: 1)
                }
                noticeInstance.save(flush: true)
            }
            
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), ''])}"
            redirect(controller: "project", action: "show", id: projectInstance.id)
        }
        else {
            flash.error = renderErrors(bean: messageInstance, as: "list")
            redirect(controller: "project", action: "show", id: projectInstance.id, model: [messageInstance: messageInstance])
        }
    }

    def edit = {
        def messageInstance = Message.get(params.id)
        if (!messageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [messageInstance: messageInstance]
        }
    }

    def update = {
        def messageInstance = Message.get(params.id)
        if (messageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (messageInstance.version > version) {
                    
                    messageInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'message.label', default: 'Message')] as Object[], "Another user has updated this Message while you were editing")
                    render(view: "edit", model: [messageInstance: messageInstance])
                    return
                }
            }
            messageInstance.properties = params
            if (!messageInstance.hasErrors() && messageInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])}"
                redirect(action: "show", id: messageInstance.id)
            }
            else {
                render(view: "edit", model: [messageInstance: messageInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def messageInstance = Message.get(params.id)
        if (messageInstance) {
            try {
                messageInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])}"
            redirect(action: "list")
        }
    }
}

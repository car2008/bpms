package com.capitalbiotech.bpms

import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class AnalysisController {
	def springSecurityService
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	@Secured(['ROLE_ADMIN'])
	def index = {
		redirect(action: "list", params: params)
	}

	@Secured(['ROLE_ADMIN'])
	def list = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[analysisInstanceList: Analysis.list(params), analysisInstanceTotal: Analysis.count()]
	}

	@Secured(['ROLE_USER'])
	def show = {
		def analysisInstance = Analysis.get(params.id)
		if (analysisInstance) {
			[analysisInstance: analysisInstance]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'analysis.label', default: 'Analysis'), params.id])}"
			redirect(action: "list")
		}
	}
}

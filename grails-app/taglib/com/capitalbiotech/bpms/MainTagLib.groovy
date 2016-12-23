package com.capitalbiotech.bpms

import grails.converters.JSON
import org.springframework.web.servlet.support.RequestContextUtils as RCU

class MainTagLib {
	
	static namespace = "bpms"
	
    def springSecurityService
    
    def setLoggedInUser = { attrs ->
        if (springSecurityService.loggedIn) {
            
            if (attrs?.var != null) {
                this.pageScope."${attrs.var}" = springSecurityService.currentUser
            }
        }
    }
	
	/**
	 * Creates next/previous links to support pagination for the current controller.<br/>
	 *
	 * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
	 *
	 * @attr total REQUIRED The total number of results to paginate
	 * @attr action the name of the action to use in the link, if not specified the default action will be linked
	 * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
	 * @attr id The id to use in the link
	 * @attr params A map containing request parameters
	 * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
	 * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
	 * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
	 * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
	 * @attr offset Used only if params.offset is empty
	 * @attr fragment The link fragment (often called anchor tag) to use
	 */
	def paginate = { attrs ->
		if (attrs.total == null) {
			throwTagError("Tag [paginate] is missing required attribute [total]")
		}

		def messageSource = grailsAttributes.messageSource
		def locale = RCU.getLocale(request)

		def total = attrs.int('total') ?: 0
		def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
		def offset = params.int('offset') ?: 0
		def max = params.int('max')
		def maxsteps = (attrs.int('maxsteps') ?: 10)

		if (!offset) offset = (attrs.int('offset') ?: 0)
		if (!max) max = (attrs.int('max') ?: 10)

		def linkParams = [:]
		if (attrs.params) linkParams.putAll(attrs.params)
		linkParams.offset = offset - max
		linkParams.max = max
		if (params.sort) linkParams.sort = params.sort
		if (params.order) linkParams.order = params.order

		def linkTagAttrs = [action:action]
		if (attrs.controller) {
			linkTagAttrs.controller = attrs.controller
		}
		if (attrs.id != null) {
			linkTagAttrs.id = attrs.id
		}
		if (attrs.fragment != null) {
			linkTagAttrs.fragment = attrs.fragment
		}
		linkTagAttrs.params = linkParams
		
		attrs.prev = attrs.prev ? attrs.prev : messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, locale), locale)
		attrs.next = attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, locale), locale)
		
		// determine paging variables
		def steps = maxsteps > 0
		int currentstep = (offset / max) + 1
		int firststep = 1
		int laststep = Math.round(Math.ceil(total / max))

		// display steps when steps are enabled and laststep is not firststep
		if (steps && laststep > firststep) {
			out << '<ul>'
			
			// display previous link
			if (currentstep <= firststep) {
				out << '<li class="disabled"><span>'
				out << attrs.prev
				out << '</span>'
			}
			else {
				out << '<li>'
				linkParams.offset = offset - max
				out << link(linkTagAttrs.clone()) {
					attrs.prev
				}
			}
			out << '</li>'
	
			// determine begin and endstep paging variables
			int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
			int endstep = currentstep + Math.round(maxsteps / 2) - 1

			if (beginstep < firststep) {
				beginstep = firststep
				endstep = maxsteps
			}
			if (endstep > laststep) {
				beginstep = laststep - maxsteps + 1
				if (beginstep < firststep) {
					beginstep = firststep
				}
				endstep = laststep
			}

			// display firststep link when beginstep is not firststep
			if (beginstep > firststep) {
				linkParams.offset = 0
				out << link(linkTagAttrs.clone()) {firststep.toString()}
				out << '<li class="disabled"><span>..</span></li>'
			}

			// display paginate steps
			(beginstep..endstep).each { i ->
				if (currentstep == i) {
					out << "<li class=\"active\"><span>${i}</span></li>"
				}
				else {
					out << '<li>'
					linkParams.offset = (i - 1) * max
					out << link(linkTagAttrs.clone()) {i.toString()}
					out << '</li>'
				}
			}

			// display laststep link when endstep is not laststep
			if (endstep < laststep) {
				out << '<li class=\"disabled\"><span>${i}</span></li>'
				linkParams.offset = (laststep -1) * max
				out << link(linkTagAttrs.clone()) { laststep.toString() }
			}
			
			// display next link
			if (currentstep >= laststep) {
				out << '<li class="disabled"><span>'
				out << attrs.next
				out << '</span>'
			}
			else {
				out << '<li>'
				linkParams.offset = offset + max
				out << link(linkTagAttrs.clone()) {
					attrs.next
				}
			}
			
			out << '</li>'
			
			out << '</ul>'
		}
	}

	def maybeEmpty = { attrs ->
		if (attrs.value == null || String.valueOf(attrs.value).trim() == "") {
			out << '-'
		}
		else {
			out << attrs.value
		}
	}
	
	def favorite = { attrs ->
		def messageSource = grailsAttributes.messageSource
		def locale = RCU.getLocale(request)
		
		def favoriteId = "favorite${attrs.type}${attrs.id}";
		def titleAdd = messageSource.getMessage('favorite.add.label', null, locale)
		def titleRemove = messageSource.getMessage('favorite.remove.label', null, locale)
		
		if (attrs.favorite == null || attrs.id == null || attrs.type == null) {
			throwTagError("Tag [paginate] is missing required attribute [favorite, type, or id]")
		}
		
		if (attrs.favorite == true) {
			out << "<i id='${favoriteId}' title='${titleRemove}' class='icon-blue-star icon-beigene' style='cursor:pointer;' onclick='javascript:${favoriteId}();'></i>"
		}
		else {
			out << "<i id='${favoriteId}' title='${titleAdd}' class='icon-blue-star-empty icon-beigene' style='cursor:pointer;' onclick='javascript:${favoriteId}();'></i>"
		}
		
		out << "<script type='text/javascript'>function ${favoriteId}(){\$.ajax({type:'POST',url:'"
		out << createLink(controller: 'favorite', action: 'toggle', id: attrs.id)
		out << "',data:{type:'${attrs.type}'},dataType:'json',success:function(result){if(result.success){if(result.favorite){\$('#${favoriteId}').attr('class', 'icon-blue-star icon-beigene');\$('#${favoriteId}').attr('title', '${titleRemove}');}else{\$('#${favoriteId}').attr('class', 'icon-blue-star-empty icon-beigene');\$('#${favoriteId}').attr('title', '${titleAdd}');}}}});};</script>"
		
	}

	def projectLevelLabel = { attrs ->
		def messageSource = grailsAttributes.messageSource
		def locale = RCU.getLocale(request)
		
		
		if (attrs.level == null) {
			throwTagError("Tag [projectLevelLabel] is missing required attribute [level]")
		}
		
		if (attrs.level == Project.LEVEL_HIGH) {
			out << '<span class="label label-important">'
		}
		else if (attrs.level == Project.LEVEL_NORMAL) {
			out << '<span class="label">'
		}
		else if (attrs.level == Project.LEVEL_LOW) {
			out << '<span class="label label-success">'
		}
		
		out << messageSource.getMessage("project.level.${attrs.level}.label", null, locale)
		out << '</span>'
	}
}

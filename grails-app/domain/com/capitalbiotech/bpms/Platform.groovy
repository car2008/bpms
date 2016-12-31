package com.capitalbiotech.bpms

import java.util.Date;
import java.util.Set;

class Platform {
	
	String code
	String title
	String description
	Date dateCreated
	Date lastUpdated
	Set<Project> projects
	Set<Worktime> worktimes
	
	static constraints = {
		code blank: false, unique: true
		title blank: false, unique: true
		description nullable: true
	}
	
	static hasMany = [projects: Project,worktimes: Worktime]
	static belongsTo =[Project,Worktime]
	
	static mapping = {
		description type:'text'
	}
}

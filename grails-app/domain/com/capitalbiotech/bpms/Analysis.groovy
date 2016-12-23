package com.capitalbiotech.bpms
/**
 * 原来作为“数据分析”字段来使用，现在改为“工作性质”字段--2016/10/27
 * @author czp
 *
 */
class Analysis {
	
	String code
    String title
	String description
	Date dateCreated
	Date lastUpdated
    Set<Project> projects
    
    static constraints = {
		code blank: false, unique: true
        title blank: false, unique: true
        description nullable: true
    }
    
    static hasMany = [projects: Project]
    static belongsTo = Project
    
    static mapping = {
        description type:'text'
    }
}

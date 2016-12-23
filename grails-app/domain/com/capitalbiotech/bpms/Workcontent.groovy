package com.capitalbiotech.bpms

import java.util.Date;
import java.util.Set;
/**
 * 原来作为“物种”字段来使用，现在改为“工作内容”字段--2016/10/27
 * @author czp
 *
 */
class Workcontent {
	String code
	String title
	String description
	Date dateCreated
	Date lastUpdated
    Set<Worktime> worktimes
    
    static constraints = {
        code blank: false, unique: true
		title blank: false, unique: true
        description nullable: true
    }
    
    static hasMany = [worktimes: Worktime]
    static belongsTo = Worktime
    
    static mapping = {
        description type:'text'
    }
}

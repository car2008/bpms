package com.capitalbiotech.bpms

class Message {
	
    Project project
	String content
	User owner
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		content blank:false
    }
	static mapping = {
		content type:'text'
	}
}

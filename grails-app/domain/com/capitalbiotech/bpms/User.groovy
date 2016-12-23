package com.capitalbiotech.bpms

class User {
	String username
	String name
	String email 
	String password
    String avatar
    Integer unread = 0
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false

	static constraints = {
		username blank: false, unique: true
		password blank: false
		name blank: false
		email email:true, unique: true
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
	
	def getAuthoritiesString() {
		getAuthorities()?.collect{it.authority}?.join("|")
	}
	
	String toString() {
		username
	}
}

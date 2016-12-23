package com.capitalbiotech.bpms

class BackupRecord {

    def springSecurityService
    String fileName
    Integer fileSize = 0
    Date dateCreated
    Date lastUpdated

    String toString() {
        "Backup Record ${id}"
    }

    static constraints = {
        fileName nullable: false, blank: false
    }

    static mapping = {
    }
}

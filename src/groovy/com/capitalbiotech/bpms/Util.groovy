package com.capitalbiotech.bpms


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

public class Util {
    static SimpleDateFormat dateParser1 = new SimpleDateFormat("yyyy-MM-dd")
    static SimpleDateFormat dateParser2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    
    public static Date parseSimpleDate(String simpleDate) throws ParseException {
        return dateParser1.parse(simpleDate)
    }

    public static Date parseSimpleDateTime(String simpleDateTime) throws ParseException {
        return dateParser2.parse(simpleDateTime)
    }
	
	public static String getCurrentTimeString() throws ParseException {
		def timestamp = System.currentTimeMillis()
		return dateParser2.format(new Date(timestamp))
	}

	public static String getCurrentDateString() throws ParseException {
		def timestamp = System.currentTimeMillis()
		return dateParser1.format(new Date(timestamp))
	}
	
    public static boolean isEmpty(String value) {
        if (value == null || value.equals("")) {
            return true
        }
        return false
    }

    public static boolean hasEmpty(String... values) {
        def result = false
        values?.each { value ->
            if (value == null || value.equals("")) {
                result = true
            }
        }
        return result
    }
	public static boolean backupSql(Object grailsApplication){
		def success = false
		println "Backup database now..."
		def timestamp = System.currentTimeMillis()
		def fileName = "${timestamp}.sql"
		def file = new File("${grailsApplication.config.example.dir.backup}/${fileName}")
		def cmd = "${grailsApplication.config.example.backup.cmd}"
		println cmd

		file.withWriter {writer ->
			def proc = cmd.execute()
			proc.in.eachLine {line ->
				writer.write("${line}\n")
			}
			proc.out.close()
			proc.waitFor()
			if (!proc.exitValue()) {
				def backupRecord = new BackupRecord(
					fileName: fileName,
					fileSize: file.size()
				)
				if (backupRecord.save(flush: true)) {
					println "Backup success"
					success = true
				}
				else {
					println "Backup failed"
				}
			}
			else {
				println "Backup failed, exit code: ${proc.exitValue()}"
			}
		}
		return success
	}
	
}

package com.capitalbiotech.bpms

class BackupController {
	def grailsApplication
    def index() { }
	def backup(){
		println "-->Manual Backup Time:"+Util.getCurrentTimeString()
		return Util.backupSql(grailsApplication)
	}

	def restore() {
		def maxId = BackupRecord.executeQuery("SELECT max(id) FROM BackupRecord backupRecord")
		def maxIdString=maxId.toString().replace("[","")
		maxIdString=maxIdString.replace("]","")
		//def backupRecord=BackupRecord.executeQuery("SELECT backupRecord from BackupRecord backupRecord WHERE id='"+maxIdString+"'")
		BackupRecord backupRecord=BackupRecord.findById(maxIdString);
		println backupRecord
		if (!backupRecord) {
			return false
		}
		println "Restore database using file ${backupRecord.fileName} now..."
		//def filePath ="/data/bpmsczp/backupfile/2.sql"
		//def filePath="D:/chengzhipeng/2.sql"
		def filePath = "${grailsApplication.config.example.dir.backup}/${backupRecord.fileName}"
		//def cmd1 = ["${grailsApplication.config.example.restore.cmd}", "${filePath}"]
		def cmd1 = "${grailsApplication.config.example.restore.cmd1}"
		def cmd2 = "${grailsApplication.config.example.restore.cmd2}"
		def cmd3 = "${grailsApplication.config.example.restore.cmd3}"+"${filePath}"+";"
		println cmd1
		println cmd2
		println cmd3
		/*def cmdlist=cmd1+ "\r\n" +cmd2+ "\r\n" +cmd3+ "\r\n"
		println cmdlist
		def proc = cmdlist.execute()
		proc.in.eachLine {line ->
			println(line)
		}
		proc.waitFor()*/
		Process process =null;
		OutputStream os =null;
		OutputStreamWriter writer =null;
		try{
			Runtime runtime = Runtime.getRuntime();
			//因为在命令窗口进行mysql数据库的导入一般分三步走，所以所执行的命令将以字符串数组的形式出现
			//runtime.exec(cmd);//这里也是简单的直接抛出异常
			process = runtime.exec(cmd1);
			//执行了第一条命令以后已经登录到mysql了，所以之后就是利用mysql的命令窗口
			//进程执行后面的代码
			os = process.getOutputStream();
			writer = new OutputStreamWriter(os);
			//命令2和命令3要放在一起执行
			writer.write(cmd2 + "\r\n" + cmd3+ "\r\n");
			writer.flush();
			println "Restore success"
			return true
		}catch(Exception e){
			e.printStackTrace();
			println "Restore failed, exit code: ${process.exitValue()}"
			return false
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/*if (!proc.exitValue()) {
			println "Restore success"
			return true
		}
		else {
			println "Restore failed, exit code: ${proc.exitValue()}"
			return false
		}*/
	}
}

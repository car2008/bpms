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
		//def cmd4= "${grailsApplication.config.example.backup.cmd}"+"/backupfile1/${fileName}"
		println cmd
		
		Process process =null;
		InputStream inputStream =null;
		InputStreamReader reader =null;
		BufferedReader br =null;
		FileOutputStream fileOutputStream =null;
		try {
			Runtime runtime = Runtime.getRuntime();
			//-u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字
			process = runtime.exec(cmd);
			inputStream = process.getInputStream();//得到输入流，写成.sql文件
			reader = new InputStreamReader(inputStream,"utf-8");
			br = new BufferedReader(reader);
			String s = null;
			StringBuffer sb = new StringBuffer();
			while((s = br.readLine()) != null){
				sb.append(s+"\r\n");
			}
			s = sb.toString();
			//System.out.println(s);
			//File file = new File(filePath);
			file.getParentFile().mkdirs();
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(s.getBytes("utf-8"));
			if (!process.exitValue()) {
				def backupRecord = new BackupRecord(
					fileName: fileName,
					fileSize: file.size()
				)
				if (backupRecord.save(flush: true)) {
					println "Backup success"
					success = true
					//downloadFile("192.168.2.23:28903/data/bpmsczp/backupfile/1.sql","C:/Users/czp/examples/example/static/backup/111.sql")
				}else{
					println "Backup failed"
				}
			}
			else {
				println "Backup failed, exit code: ${process.exitValue()}"
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (reader != null) {
					reader.close();
				}
				if (br != null) {
					br.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*file.withWriter {writer ->
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
		}*/
		return success
	}
	public static void downloadFile(String remoteFilePath, String localFilePath){
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File f = new File(localFilePath);
		try {
			urlfile = new URL(remoteFilePath);
			httpUrl = (HttpURLConnection)urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1)
			{
				bos.write(b, 0, len);
			}
			bos.flush();
			bis.close();
			httpUrl.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bis.close();
				bos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}

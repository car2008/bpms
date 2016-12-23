package com.capitalbiotech.bpms

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.core.task.TaskExecutor;

class SendEmailAsynchronously {
	static transactional = true
	private static TaskExecutor taskExecutor
	 
	def ctx = ApplicationHolder.getApplication().getMainContext();
	def mailService = ctx.getBean("mailService");
	def messageSource = ctx.messageSource
	
	def fromEmail
	def toEmail
	def mailObject
	def emailBody
	def attach
	public SendEmailAsynchronously() {
	}
	public SendEmailAsynchronously(def toEmail ,def mailObject,def emailBody) {
		this.fromEmail = 'capitalbiotech@qq.com'
		this.toEmail = toEmail
		this.mailObject = mailObject
		this.emailBody = generateContent(emailBody)
		
	}
	 
	public SendEmailAsynchronously(def toEmail ,def mailObject,def emailBody,def attach) {
		this.fromEmail = 'capitalbiotech@qq.com'
		this.toEmail = toEmail
		this.mailObject = mailObject
		this.emailBody = generateContent(emailBody)
		this.attach = attach
		
	}
 
	public void sendMail() {
		taskExecutor.execute(new Runnable(){
			@Override
			public void run() {
				try {
					println "start send mail"
						mailService.sendMail {
							from fromEmail
							to toEmail
							subject mailObject
							html emailBody
						}
					println "end send mail"
				}catch(Exception e){
					println e
					try{
						//User.findByEmail(toEmail).delete()
					}catch (Exception ue){
						print "User=="+ue
					}
 
				}
			}
		}
		)
	}
	 
	 
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor
	}
	private String generateContent(bodyContent) {
		if(bodyContent instanceof String) {
			return bodyContent
		}else if(bodyContent instanceof Closure) {
			//bodyContent is a closure
			def sw = new StringWriter()
			def mkp = new groovy.xml.MarkupBuilder(new PrintWriter(sw))
			mkp.html {
				body(bodyContent)
			}
			return sw.toString()
		}
	}
}

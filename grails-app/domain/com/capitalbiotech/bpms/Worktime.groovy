package com.capitalbiotech.bpms

import java.math.BigDecimal;
import java.util.Set;

/**
 * 单个合同工时表
 * @author czp
 *
 */
class Worktime {
	//单个合同成本核算信息
	Project project
	Set<Workcontent> workcontents	    //工作内容
	String workWay = WAY_USUAL      //工作方式(一般，邮件，外出，电话)
	BigDecimal manHour				//工时
	BigDecimal machineHour			//机时
	String comment2					//备注2
	Date dateCreated
	Date lastUpdated
	Date finishedDate               //完成日期
	Set<User> completers             //完成人员
	Set<Platform> platforms 		//项目平台
	String contract
	String information      		//项目信息
    
	public static final String WAY_USUAL = "WAY_USUAL"
	public static final String WAY_MAIL = "WAY_MAIL"
	public static final String WAY_OUT = "WAY_OUT"
	public static final String WAY_PHONE = "WAY_PHONE"
	
	static hasMany = [
		platforms:Platform,
		workcontents:Workcontent,
		completers:User
	]
	
    static constraints = {
		workWay inList: [
			WAY_USUAL,
			WAY_MAIL,
			WAY_OUT,
			WAY_PHONE,
		]
		//code blank: false, unique: true
        //title blank: false, unique: true
		finishedDate nullable: true,blank :true
        comment2 nullable: true,blank :true
		completers nullable: true,blank :true
		manHour nullable: true,blank :true
		machineHour nullable: true,blank :true
		completers nullable: true,blank :true
		platforms nullable: true,blank :true
		workcontents nullable: true,blank :true
		information nullable: true,blank :true
		contract nullable: true,blank :true
    }
    
    static mapping = {
        comment2 type:'text'
    }
	
}

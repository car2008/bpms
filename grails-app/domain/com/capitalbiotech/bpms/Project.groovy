package com.capitalbiotech.bpms

class Project {
	
    String title                   //原字段为“标题”，现改为“编号”字段
    String contract
    String level = LEVEL_LOW
    Date dateCreated
    Date lastUpdated
    Date dueTime
	Date projectCreateDate
    Set<User> supervisors
    Set<User> analysts
    Set<User> sellers				//原字段为“销售”，现作为字段“审核员”使用--2016/10/27
    String status = STATUS_WAIT
    List<Message> messages
    Set<Experiment> experiments
    Set<Analysis> analyses			//原字段为：数据分析，现改为字段：工作性质
	//Set<Categories> categories	//工作性质
	/**以下均为新增字段**/
	String salesman					//销售员
	String customerUnit				//客户单位
	String customerName				//客户名
	Set<Platform> platforms 		//项目平台
	String information      		//项目信息
	
	String species                  //物种
	Long samplesize					//芯片数或者样本数
	String k3number					//k3编号
	String batch					//批次
	Date metaSendData				//原始数据发送时间
	Date analyStartDate				//分析开始时间
	Date analySendDate 				//分析发出时间
	Date innerDueDate				//内部到期日期
	Date backupDate					//备份时间按
	String metaSendWay 	     //原始数据给出方式:u盘，硬盘
	String analySendWay		//分析数据给出方式:u盘，硬盘，光盘，共享
	String routineAnalysis=ROUTINE_ANALYSIS
	//分析相关信息
	BigDecimal manHour 				//工时
	BigDecimal machineHour			//机时
	String comment1					//备注1
	String overdueReason			//过期原因
	String backupLocation			//备份位置
	boolean isRemoted				//是否远程备份
	boolean isControled				//是否管控完毕
	
	//单个合同成本核算信息
	List<Worktime> worktimes
	//测序相关信息
	String libraryBuildWay	        //建库方式
	Long readLength					//测序读长
	Long readsNum					//每个样本的reads
	Long dataSize					//每个样本的数据量
	Set<User> spliters				//CASAVA拆分员
	String fileName                 //上传文件名
	String filePath                 //上传文件的相对路径

	public static final String ROUTINE_ANALYSIS = "ROUTINE_ANALYSIS"

	public static final String LEVEL_LOW = "LEVEL_LOW"
    public static final String LEVEL_NORMAL = "LEVEL_NORMAL"
    public static final String LEVEL_HIGH = "LEVEL_HIGH"

    public static final String STATUS_WAIT = "STATUS_WAIT"
    public static final String STATUS_DATA_ACCEPTED = "STATUS_DATA_ACCEPTED"
    public static final String STATUS_STARTED = "STATUS_STARTED"
    public static final String STATUS_COMPLETED = "STATUS_COMPLETED"
    public static final String STATUS_RELEASED = "STATUS_RELEASED"
    public static final String STATUS_ARCHIVED = "STATUS_ARCHIVED"
    
	public static final String WAY_UDISK = "WAY_UDISK"
	public static final String WAY_HDISK = "WAY_HDISK"
	public static final String WAY_LDISK = "WAY_LDISK"
	public static final String WAY_CDISK = "WAY_CDISK"
	public static final String WAY_ADISK = "WAY_ADISK"
	public static final String WAY_LCDISK = "WAY_LCDISK"
	public static final String WAY_UCDISK = "WAY_UCDISK"
	public static final String WAY_HCDISK = "WAY_HCDISK"
	
	
	public static final String PAIR_END = "PAIR_END"
	public static final String SINGLE_READ = "SINGLE_READ"
	
    def getFollowingStatus() {
        def followingStatus = [status]
        switch(status) {
            case 'STATUS_WAIT':
                followingStatus << STATUS_DATA_ACCEPTED
            case 'STATUS_DATA_ACCEPTED':
                followingStatus << STATUS_STARTED
            case 'STATUS_STARTED':
                followingStatus << STATUS_COMPLETED
            case 'STATUS_COMPLETED':
                followingStatus << STATUS_RELEASED
            case 'STATUS_RELEASED':
                followingStatus << STATUS_ARCHIVED
        }
        return followingStatus
    }

    static transients = [
        'messages',
        'tracks',
		'worktimes'
    ]
    
    static hasMany = [
        analyses: Analysis,
		platforms:Platform,
        experiments: Experiment,
        supervisors: User,
        analysts: User,
        sellers: User,
		spliters:User,
    ]

    List<Message> getMessages() {
        return Message.findAllByProject(this, [sort: 'dateCreated', order: 'desc'])
    }

    List<Track> getTracks() {
        return Track.findAllByProject(this, [sort: 'dateCreated', order: 'desc'])
    }
	
	List<Worktime> getWorktimes() {
		return Worktime.findAllByProject(this, [sort: 'dateCreated', order: 'desc'])
	}

    static constraints = {
        level inList: [
            LEVEL_LOW,
            LEVEL_NORMAL,
            LEVEL_HIGH,
        ]
        status inList: [
            STATUS_WAIT,
            STATUS_DATA_ACCEPTED,
            STATUS_STARTED,
            STATUS_COMPLETED,
            STATUS_RELEASED,
            STATUS_ARCHIVED,
        ]
		metaSendWay inList: [
			"null",
			WAY_UDISK,
			WAY_HDISK,
			WAY_CDISK,
			WAY_UCDISK,
			WAY_HCDISK,
			WAY_ADISK,
		]
		analySendWay inList: [
			"null",
			WAY_UDISK,
			WAY_HDISK,
			WAY_LDISK,
			WAY_CDISK,
			WAY_UCDISK,
			WAY_LCDISK,
			WAY_HCDISK,
			WAY_ADISK,
		]
		libraryBuildWay inList:[
			"null",
			PAIR_END,
			SINGLE_READ,
		]
        /*title blank: false, unique: true
        contract blank: false
		customerUnit blank: false
		customerName blank: false
		information blank: false
        dueTime nullable: false
		metaSendData nullable: true
		analyStartDate nullable: true
		analySendDate nullable: true
		innerDueDate nullable: false
		backupDate nullable: true
		isControled nullable: true
		isRemoted nullable: true
		species blank:false
		samplesize blank: true
		k3number blank: true,nullable: true
		batch blank: true,nullable: true
		readLength blank: true,nullable: true
		readsNum blank: true,nullable: true
		dataSize blank: true,nullable: true
		backupLocation blank: true,nullable: true
		comment1 blank: true,nullable: true
		overdueReason blank: true,nullable: true
		manHour blank: true,nullable: true,default:0
		machineHour blank: true,nullable: true,default:0
		fileName blank: true,nullable: true
		filePath blank: true,nullable: true
		platforms blank: true,nullable: true*/
		title blank: false, unique: true
		contract blank: true,nullable: true
		customerUnit blank: true,nullable: true
		customerName blank: true,nullable: true
		information blank: true,nullable: true
		projectCreateDate blank: true,nullable: true
		dueTime blank: true,nullable: true
		metaSendData blank: true,nullable: true
		analyStartDate blank: true,nullable: true
		analySendDate blank: true,nullable: true
		innerDueDate blank: true,nullable: true
		backupDate blank: true,nullable: true
		isControled blank: true,nullable: true
		isRemoted blank: true,nullable: true
		species blank: true,nullable: true
		samplesize blank: true,nullable: true
		k3number blank: true,nullable: true
		batch blank: true,nullable: true
		readLength blank: true,nullable: true
		readsNum blank: true,nullable: true
		dataSize blank: true,nullable: true
		backupLocation blank: true,nullable: true
		comment1 blank: true,nullable: true
		overdueReason blank: true,nullable: true
		manHour blank: true,nullable: true,default:0
		machineHour blank: true,nullable: true,default:0
		fileName blank: true,nullable: true
		filePath blank: true,nullable: true
		platforms blank: true,nullable: true
		salesman blank: true,nullable: true
		level blank: true,nullable: true
		status blank: true,nullable: true
		metaSendWay blank: true,nullable: true
		analySendWay blank: true,nullable: true
		libraryBuildWay blank: true,nullable: true
		
    }
    
    static mapping = {
        
    }
}
